package chromahub.feel.app.ui.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.coroutineScope
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import java.security.MessageDigest
import android.content.ActivityNotFoundException

data class UpdateState(
    val isUpdateAvailable: Boolean = false,
    val isChecking: Boolean = false,
    val currentVersion: String = "1.5",
    val latestVersion: String = "",
    val releaseNotes: String = "",
    val downloadUrl: String = "",
    val publishDate: String = "May 27, 2025",
    val downloadProgress: Float = 0f,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 0L,
    val isDownloading: Boolean = false,
    val isDownloadComplete: Boolean = false,
    val downloadedFilePath: String = "",
    val error: String? = null
)

interface GitHubApi {
    @GET("repos/{owner}/{repo}/releases/latest")
    suspend fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): GitHubRelease
}

data class GitHubRelease(
    val tag_name: String,
    val name: String,
    val body: String,
    val published_at: String,
    val assets: List<GitHubAsset>,
    val html_url: String
)

data class GitHubAsset(
    val browser_download_url: String,
    val name: String,
    val size: Long,
    val download_count: Int
)

class UpdateViewModel : ViewModel() {
    private val _updateState = MutableStateFlow(UpdateState())
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()
    private var downloadId: Long = -1
    private val handler = Handler(Looper.getMainLooper())
    private var progressUpdateJob: Runnable? = null

    private val gitHubApi = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubApi::class.java)

    suspend fun checkForUpdates() {
        _updateState.value = _updateState.value.copy(
            isChecking = true,
            error = null,
            isUpdateAvailable = false
        )

        try {
            val latestRelease = gitHubApi.getLatestRelease(
                owner = "cromaguy",
                repo = "Feel-Android-App"
            )
            
            val currentVersion = _updateState.value.currentVersion
            val latestVersion = latestRelease.tag_name.removePrefix("v")
            val hasUpdate = isNewerVersion(latestVersion, currentVersion)
            
            // Format the publish date
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)
            val publishDate = try {
                val date = inputFormat.parse(latestRelease.published_at)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                latestRelease.published_at
            }

            _updateState.value = _updateState.value.copy(
                isUpdateAvailable = hasUpdate,
                latestVersion = latestVersion,
                releaseNotes = latestRelease.body,
                downloadUrl = latestRelease.assets.firstOrNull { it.name.endsWith(".apk") }?.browser_download_url ?: "",
                publishDate = publishDate,
                isChecking = false,
                error = null
            )
        } catch (e: Exception) {
            _updateState.value = _updateState.value.copy(
                isChecking = false,
                error = "Failed to check for updates: ${e.message}",
                isUpdateAvailable = false
            )
        }
    }

    fun downloadUpdate(context: Context) {
        val downloadUrl = _updateState.value.downloadUrl
        if (downloadUrl.isEmpty()) {
            _updateState.value = _updateState.value.copy(
                error = "No download URL available"
            )
            return
        }

        try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(downloadUrl)
            val fileName = "Feel-${_updateState.value.latestVersion}.apk"
            
            // Cancel any existing download
            if (downloadId != -1L) {
                downloadManager.remove(downloadId)
            }

            val request = DownloadManager.Request(uri).apply {
                setTitle("Feel App Update")
                setDescription("Downloading version ${_updateState.value.latestVersion}")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                setMimeType("application/vnd.android.package-archive")
                addRequestHeader("Content-Type", "application/vnd.android.package-archive")
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setRequiresCharging(false)
                    setRequiresDeviceIdle(false)
                }
            }
            
            downloadId = downloadManager.enqueue(request)
            _updateState.value = _updateState.value.copy(
                isDownloading = true,
                isDownloadComplete = false,
                downloadProgress = 0f,
                downloadedBytes = 0,
                totalBytes = 0,
                error = null
            )

            // Start progress monitoring
            startProgressMonitoring(context, downloadManager, downloadId)
        } catch (e: Exception) {
            _updateState.value = _updateState.value.copy(
                error = "Failed to start download: ${e.message}",
                isDownloading = false
            )
        }
    }

    private fun startProgressMonitoring(context: Context, downloadManager: DownloadManager, downloadId: Long) {
        progressUpdateJob?.let { handler.removeCallbacks(it) }
        
        progressUpdateJob = object : Runnable {
            override fun run() {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    val bytesDownloaded = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    val bytesTotal = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            val filePath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/Feel-${_updateState.value.latestVersion}.apk"
                            if (verifyDownload(filePath)) {
                                _updateState.value = _updateState.value.copy(
                                    isDownloading = false,
                                    isDownloadComplete = true,
                                    downloadProgress = 1f,
                                    downloadedBytes = bytesTotal,
                                    totalBytes = bytesTotal,
                                    downloadedFilePath = filePath,
                                    error = null
                                )
                            } else {
                                _updateState.value = _updateState.value.copy(
                                    isDownloading = false,
                                    error = "Download verification failed",
                                    downloadProgress = 0f
                                )
                                File(filePath).delete()
                            }
                            progressUpdateJob = null
                        }
                        DownloadManager.STATUS_FAILED -> {
                            val reason = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))
                            _updateState.value = _updateState.value.copy(
                                isDownloading = false,
                                error = "Download failed: ${getErrorMessage(reason)}",
                                downloadProgress = 0f
                            )
                            progressUpdateJob = null
                        }
                        DownloadManager.STATUS_RUNNING -> {
                            if (bytesTotal > 0) {
                                val progress = bytesDownloaded.toFloat() / bytesTotal.toFloat()
                                _updateState.value = _updateState.value.copy(
                                    downloadProgress = progress,
                                    downloadedBytes = bytesDownloaded,
                                    totalBytes = bytesTotal
                                )
                            }
                            handler.postDelayed(this, 100)
                        }
                        DownloadManager.STATUS_PAUSED -> {
                            val reason = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))
                            _updateState.value = _updateState.value.copy(
                                error = "Download paused: ${getErrorMessage(reason)}"
                            )
                            handler.postDelayed(this, 1000)
                        }
                    }
                }
                cursor.close()
            }
        }.also {
            handler.post(it)
        }
    }

    private fun verifyDownload(filePath: String): Boolean {
        return try {
            val file = File(filePath)
            // Basic verification: check if file exists and size is reasonable
            if (!file.exists() || file.length() < 1024 * 1024) { // Less than 1MB is suspicious
                return false
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getErrorMessage(reason: Int): String {
        return when (reason) {
            DownloadManager.ERROR_CANNOT_RESUME -> "Cannot resume download"
            DownloadManager.ERROR_DEVICE_NOT_FOUND -> "Storage device not found"
            DownloadManager.ERROR_FILE_ALREADY_EXISTS -> "File already exists"
            DownloadManager.ERROR_FILE_ERROR -> "File error"
            DownloadManager.ERROR_HTTP_DATA_ERROR -> "Network data error"
            DownloadManager.ERROR_INSUFFICIENT_SPACE -> "Insufficient storage space"
            DownloadManager.ERROR_TOO_MANY_REDIRECTS -> "Too many redirects"
            DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> "Unhandled HTTP code"
            DownloadManager.ERROR_UNKNOWN -> "Unknown error"
            DownloadManager.PAUSED_QUEUED_FOR_WIFI -> "Waiting for Wi-Fi"
            DownloadManager.PAUSED_UNKNOWN -> "Paused for unknown reason"
            DownloadManager.PAUSED_WAITING_FOR_NETWORK -> "Waiting for network"
            DownloadManager.PAUSED_WAITING_TO_RETRY -> "Waiting to retry"
            else -> "Error code: $reason"
        }
    }

    fun installUpdate(context: Context) {
        val file = File(_updateState.value.downloadedFilePath)
        if (!file.exists()) {
            _updateState.value = _updateState.value.copy(
                error = "Installation file not found"
            )
            return
        }

        try {
            // Verify file size and extension
            if (!verifyDownloadedFile(file)) {
                _updateState.value = _updateState.value.copy(
                    error = "Invalid update file"
                )
                return
            }

            // Create content URI using FileProvider
            val uri = try {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
            } catch (e: IllegalArgumentException) {
                _updateState.value = _updateState.value.copy(
                    error = "Failed to access update file: ${e.message}"
                )
                return
            }

            // Create and start installation intent
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                _updateState.value = _updateState.value.copy(
                    error = "No app found to install the update"
                )
            } catch (e: Exception) {
                _updateState.value = _updateState.value.copy(
                    error = "Failed to start installation: ${e.message}"
                )
            }
        } catch (e: Exception) {
            _updateState.value = _updateState.value.copy(
                error = "Installation error: ${e.message}"
            )
        }
    }

    private fun verifyDownloadedFile(file: File): Boolean {
        return try {
            // Check file size (minimum 1MB, maximum 100MB)
            val fileSize = file.length()
            if (fileSize < 1024 * 1024 || fileSize > 100 * 1024 * 1024) {
                return false
            }

            // Check file extension
            if (!file.name.endsWith(".apk", ignoreCase = true)) {
                return false
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun isNewerVersion(version1: String, version2: String): Boolean {
        val v1Parts = version1.split(".").map { it.toIntOrNull() ?: 0 }
        val v2Parts = version2.split(".").map { it.toIntOrNull() ?: 0 }
        
        for (i in 0 until maxOf(v1Parts.size, v2Parts.size)) {
            val v1 = v1Parts.getOrNull(i) ?: 0
            val v2 = v2Parts.getOrNull(i) ?: 0
            
            if (v1 > v2) return true
            if (v1 < v2) return false
        }
        
        return false // Equal versions are not considered newer
    }

    override fun onCleared() {
        super.onCleared()
        progressUpdateJob?.let { handler.removeCallbacks(it) }
    }
} 
package chromahub.feel.app.ui.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
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
import java.text.SimpleDateFormat
import java.util.Locale

data class UpdateState(
    val isUpdateAvailable: Boolean = false,
    val isChecking: Boolean = false,
    val currentVersion: String = "1.0.0",
    val latestVersion: String = "",
    val releaseNotes: String = "",
    val downloadUrl: String = "",
    val publishDate: String = "",
    val downloadProgress: Float = 0f,
    val isDownloading: Boolean = false,
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

    private val gitHubApi = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubApi::class.java)

    suspend fun checkForUpdates() {
        _updateState.value = _updateState.value.copy(
            isChecking = true,
            error = null
        )

        try {
            val latestRelease = gitHubApi.getLatestRelease(
                owner = "YOUR_GITHUB_USERNAME", // Replace with your GitHub username
                repo = "Feel"
            )
            
            val currentVersion = _updateState.value.currentVersion
            val latestVersion = latestRelease.tag_name.removePrefix("v")
            
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
                isUpdateAvailable = isNewerVersion(currentVersion, latestVersion),
                latestVersion = latestVersion,
                releaseNotes = latestRelease.body,
                downloadUrl = latestRelease.assets.firstOrNull { it.name.endsWith(".apk") }?.browser_download_url ?: "",
                publishDate = publishDate,
                isChecking = false
            )
        } catch (e: Exception) {
            _updateState.value = _updateState.value.copy(
                isChecking = false,
                error = "Failed to check for updates: ${e.message}"
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
            val request = DownloadManager.Request(uri).apply {
                setTitle("Feel App Update")
                setDescription("Downloading version ${_updateState.value.latestVersion}")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "Feel-${_updateState.value.latestVersion}.apk"
                )
            }
            
            downloadManager.enqueue(request)
            _updateState.value = _updateState.value.copy(
                isDownloading = true,
                error = null
            )
        } catch (e: Exception) {
            _updateState.value = _updateState.value.copy(
                error = "Failed to start download: ${e.message}",
                isDownloading = false
            )
        }
    }

    private fun isNewerVersion(currentVersion: String, latestVersion: String): Boolean {
        val current = currentVersion.split(".").map { it.toInt() }
        val latest = latestVersion.split(".").map { it.toInt() }
        
        for (i in 0 until minOf(current.size, latest.size)) {
            if (latest[i] > current[i]) return true
            if (latest[i] < current[i]) return false
        }
        
        return latest.size > current.size
    }
} 
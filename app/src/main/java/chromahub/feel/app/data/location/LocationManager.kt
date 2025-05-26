package chromahub.feel.app.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Manager class for handling location operations
 */
class LocationManager(private val context: Context) {
    
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    
    /**
     * Checks if the app has the necessary location permissions
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || 
        ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Gets the last known location or a current location fix if possible
     * @return Location object containing coordinates or null if not available
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) {
            return null
        }
        
        // First try to get last location as it's faster
        val lastLocation = try {
            withTimeoutOrNull(1000) { // 1 second timeout for last location
                fusedLocationClient.lastLocation.await()
            }
        } catch (e: Exception) {
            null
        }
        
        // If last location is available and recent enough (within last 5 minutes), use it
        if (lastLocation != null && isLocationRecent(lastLocation)) {
            return lastLocation
        }
        
        // Otherwise request a new location fix with high accuracy
        return try {
            val cancellationToken = CancellationTokenSource()
            withTimeoutOrNull(10000) { // 10 seconds timeout for new location
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationToken.token
                ).await()
            }
        } catch (e: Exception) {
            // If high accuracy fails, try with balanced power/accuracy
            try {
                val cancellationToken = CancellationTokenSource()
                withTimeoutOrNull(5000) { // 5 seconds timeout for balanced accuracy
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        cancellationToken.token
                    ).await()
                }
            } catch (e: Exception) {
                null
            }
        }
    }
    
    /**
     * Checks if the location is recent (within last 5 minutes)
     */
    private fun isLocationRecent(location: Location): Boolean {
        val fiveMinutesAgo = System.currentTimeMillis() - (5 * 60 * 1000)
        return location.time > fiveMinutesAgo
    }
    
    /**
     * Converts a Location object to a location string that can be used with the weather API
     * @param location The location to convert
     * @return A string in the format "latitude,longitude"
     */
    fun locationToQueryString(location: Location): String {
        return "${location.latitude},${location.longitude}"
    }
} 

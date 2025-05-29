package chromahub.feel.app

import android.app.Application
import chromahub.feel.app.data.location.LocationManager

/**
 * Main Application class for the Weather Forecast app
 */
class WeatherApplication : Application() {
    
    // Lazy initialization of the location manager
    val locationManager: LocationManager by lazy {
        LocationManager(applicationContext)
    }
    
    override fun onCreate() {
        super.onCreate()
        // Initialize app-wide components here
    }
    
    companion object {
        private lateinit var instance: WeatherApplication
        
        /**
         * Get the application instance
         */
        fun getInstance(): WeatherApplication {
            return instance
        }
    }
    
    init {
        instance = this
    }
} 

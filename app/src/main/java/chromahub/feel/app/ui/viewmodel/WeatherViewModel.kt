package chromahub.feel.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chromahub.feel.app.WeatherApplication
import chromahub.feel.app.data.location.LocationManager
import chromahub.feel.app.data.model.WeatherResponse
import chromahub.feel.app.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    
    private val repository = WeatherRepository()
    private val locationManager: LocationManager = WeatherApplication.getInstance().locationManager
    
    // UI state
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    // Search query for location
    var searchQuery by mutableStateOf("")
        private set
    
    // Current selected location
    var currentLocation by mutableStateOf("London")
        private set
    
    // Selected temperature unit (Celsius or Fahrenheit)
    var useCelsius by mutableStateOf(true)
        private set
    
    // Location permission state
    var hasLocationPermission by mutableStateOf(locationManager.hasLocationPermission())
        private set
    
    // Initialization block to fetch data when ViewModel is created
    init {
        // Try to use device location first if permission is granted
        if (locationManager.hasLocationPermission()) {
            useDeviceLocation()
        } else {
            // Fallback to default location if no permission
            fetchWeatherData()
        }
    }
    
    /**
     * Updates the search query
     */
    fun updateSearchQuery(query: String) {
        searchQuery = query
    }
    
    /**
     * Updates the current location and fetches new weather data
     */
    fun updateLocation(location: String) {
        currentLocation = location
        fetchWeatherData()
    }
    
    /**
     * Toggles between Celsius and Fahrenheit
     */
    fun toggleTemperatureUnit() {
        useCelsius = !useCelsius
    }
    
    /**
     * Updates the location permission state
     */
    fun updateLocationPermission() {
        hasLocationPermission = locationManager.hasLocationPermission()
    }
    
    /**
     * Uses device location for weather data
     */
    fun useDeviceLocation() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            
            if (!locationManager.hasLocationPermission()) {
                _uiState.value = WeatherUiState.Error("Location permission not granted")
                return@launch
            }
            
            try {
                val location = locationManager.getCurrentLocation()
                if (location != null) {
                    // Update current location with the city name if available
                    val geocoder = android.location.Geocoder(WeatherApplication.getInstance())
                    try {
                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1,
                            object : android.location.Geocoder.GeocodeListener {
                                override fun onGeocode(addresses: List<android.location.Address>) {
                                    if (addresses.isNotEmpty()) {
                                        val cityName = addresses[0].locality ?: addresses[0].subAdminArea
                                        viewModelScope.launch {
                                            if (!cityName.isNullOrEmpty()) {
                                                currentLocation = cityName
                                            } else {
                                                currentLocation = locationManager.locationToQueryString(location)
                                            }
                                            fetchWeatherData()
                                        }
                                    } else {
                                        viewModelScope.launch {
                                            currentLocation = locationManager.locationToQueryString(location)
                                            fetchWeatherData()
                                        }
                                    }
                                }

                                override fun onError(errorMessage: String?) {
                                    viewModelScope.launch {
                                        currentLocation = locationManager.locationToQueryString(location)
                                        fetchWeatherData()
                                    }
                                }
                            }
                        )
                    } catch (e: Exception) {
                        // Fallback to coordinates if geocoding fails
                        currentLocation = locationManager.locationToQueryString(location)
                        fetchWeatherData()
                    }
                } else {
                    _uiState.value = WeatherUiState.Error("Could not get device location")
                }
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error("Error accessing location: ${e.message}")
            }
        }
    }
    
    /**
     * Fetches weather data for the current location
     */
    fun fetchWeatherData() {
        _uiState.value = WeatherUiState.Loading
        
        viewModelScope.launch {
            repository.getWeatherForecast(currentLocation)
                .onSuccess { weatherResponse ->
                    _uiState.value = WeatherUiState.Success(weatherResponse)
                }
                .onFailure { error ->
                    _uiState.value = WeatherUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
    
    /**
     * Retries the last weather data fetch
     */
    fun retryFetch() {
        fetchWeatherData()
    }
}

/**
 * Sealed class representing different UI states
 */
sealed class WeatherUiState {
    data object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
} 

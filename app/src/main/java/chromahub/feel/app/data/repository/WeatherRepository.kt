package chromahub.feel.app.data.repository

import chromahub.feel.app.data.model.WeatherResponse
import chromahub.feel.app.data.network.RetrofitClient
import chromahub.feel.app.data.network.WeatherApiService

/**
 * Repository class that handles weather data operations
 */
class WeatherRepository {
    
    private val weatherApiService = RetrofitClient.weatherApiService
    
    /**
     * Fetches weather forecast for the given location
     * @param location The location to get weather for (can be city name, zip, coordinates, etc.)
     * @param days Number of days to forecast (max 14)
     * @return API response with weather data or null if the request failed
     */
    suspend fun getWeatherForecast(location: String, days: Int = 7): Result<WeatherResponse> {
        return try {
            val response = weatherApiService.getWeatherForecast(
                apiKey = WeatherApiService.API_KEY,
                location = location,
                days = days
            )
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error fetching weather data: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 

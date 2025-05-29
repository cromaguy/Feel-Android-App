package chromahub.feel.app.data.network

import chromahub.feel.app.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API Service for WeatherAPI.com
 * API documentation: https://www.weatherapi.com/docs/
 */
interface WeatherApiService {
    
    @GET("v1/forecast.json")
    suspend fun getWeatherForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 7,
        @Query("aqi") airQualityIndex: String = "no",
        @Query("alerts") alerts: String = "no"
    ): Response<WeatherResponse>
    
    companion object {
        // WeatherAPI.com - Sign up for free API key at https://www.weatherapi.com/
        const val BASE_URL = "https://api.weatherapi.com/"
        
        // IMPORTANT: Replace this with your actual API key for production use
        // This demo key has limited usage and may stop working - sign up for a free key
        const val API_KEY = "41959fe355f944c68f3132258251405"
    }
} 

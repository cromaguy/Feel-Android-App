package chromahub.feel.app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("localtime") val localTime: String
)

data class CurrentWeather(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    @SerializedName("is_day") val isDay: Int,
    val condition: WeatherCondition,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_dir") val windDir: String,
    @SerializedName("precip_mm") val precipMm: Double,
    @SerializedName("precip_in") val precipIn: Double,
    val humidity: Int,
    @SerializedName("feelslike_c") val feelslikeC: Double,
    @SerializedName("feelslike_f") val feelslikeF: Double,
    val uv: Double
)

data class WeatherCondition(
    val text: String,
    val icon: String,
    val code: Int
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    @SerializedName("date_epoch") val dateEpoch: Long,
    val day: Day,
    val astro: Astro,
    val hour: List<Hour>
)

data class Day(
    @SerializedName("maxtemp_c") val maxtempC: Double,
    @SerializedName("maxtemp_f") val maxtempF: Double,
    @SerializedName("mintemp_c") val mintempC: Double,
    @SerializedName("mintemp_f") val mintempF: Double,
    @SerializedName("avgtemp_c") val avgtempC: Double,
    @SerializedName("avgtemp_f") val avgtempF: Double,
    @SerializedName("maxwind_mph") val maxwindMph: Double,
    @SerializedName("maxwind_kph") val maxwindKph: Double,
    @SerializedName("totalprecip_mm") val totalprecipMm: Double,
    @SerializedName("totalprecip_in") val totalprecipIn: Double,
    @SerializedName("totalsnow_cm") val totalsnowCm: Double,
    @SerializedName("daily_chance_of_rain") val dailyChanceOfRain: Int,
    @SerializedName("daily_chance_of_snow") val dailyChanceOfSnow: Int,
    val condition: WeatherCondition,
    val uv: Double
)

data class Astro(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    @SerializedName("moon_phase") val moonPhase: String,
    @SerializedName("moon_illumination") val moonIllumination: String
)

data class Hour(
    @SerializedName("time_epoch") val timeEpoch: Long,
    val time: String,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    @SerializedName("is_day") val isDay: Int,
    val condition: WeatherCondition,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_dir") val windDir: String,
    @SerializedName("precip_mm") val precipMm: Double,
    @SerializedName("precip_in") val precipIn: Double,
    val humidity: Int,
    @SerializedName("chance_of_rain") val chanceOfRain: Int,
    @SerializedName("chance_of_snow") val chanceOfSnow: Int,
    @SerializedName("feelslike_c") val feelslikeC: Double,
    @SerializedName("feelslike_f") val feelslikeF: Double
) 

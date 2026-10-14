package uz.ttpu.weatherpatterns.mvc

import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

class WeatherModel(private val repository: WeatherRepository) {
    var status: String = "Select a city"
        private set

    suspend fun fetchWeather(city: String): String {
        status = "Loading weather for $city..."
        val result = repository.getWeather(city)
        status = result.fold(
            onSuccess = { "${it.city}: ${it.temperatureC}°C, ${it.condition}" },
            onFailure = { "Error: ${it.message ?: "Failed to fetch weather"}" }
        )
        return status
    }
}

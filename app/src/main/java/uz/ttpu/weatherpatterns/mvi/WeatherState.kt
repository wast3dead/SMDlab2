package uz.ttpu.weatherpatterns.mvi

import uz.ttpu.weatherpatterns.data.model.Weather

data class WeatherState(
    val isLoading: Boolean = false,
    val city: String? = null,
    val weather: Weather? = null,
    val error: String? = null
)

fun WeatherState.toStatusText(): String {
    return when {
        isLoading -> "Loading weather for ${city ?: "city"}..."
        error != null -> "Error: $error"
        weather != null -> "${weather.city}: ${weather.temperatureC}°C, ${weather.condition}"
        else -> "Select a city"
    }
}

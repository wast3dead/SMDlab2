package uz.ttpu.weatherpatterns.data.api

import kotlinx.coroutines.delay
import uz.ttpu.weatherpatterns.data.model.Weather

interface WeatherApi {
    suspend fun fetchWeather(city: String): Weather
}

class FakeWeatherApi(private val delayMs: Long = 1500L) : WeatherApi {
    override suspend fun fetchWeather(city: String): Weather {
        delay(delayMs)
        return when (city.trim().lowercase()) {
            "tashkent" -> Weather("Tashkent", 28, "Sunny")
            "london" -> Weather("London", 15, "Rainy")
            "tokyo" -> Weather("Tokyo", 22, "Cloudy")
            "new york" -> Weather("New York", 18, "Windy")
            else -> throw IllegalArgumentException("Invalid city: $city")
        }
    }
}

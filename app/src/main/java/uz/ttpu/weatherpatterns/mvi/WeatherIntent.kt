package uz.ttpu.weatherpatterns.mvi

sealed interface WeatherIntent {
    data class LoadWeather(val city: String) : WeatherIntent
}

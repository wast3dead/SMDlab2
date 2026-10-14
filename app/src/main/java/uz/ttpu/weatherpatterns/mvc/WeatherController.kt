package uz.ttpu.weatherpatterns.mvc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class WeatherController(
    private val model: WeatherModel,
    private val view: WeatherView,
    private val scope: CoroutineScope
) {
    fun onCitySelected(city: String) {
        view.render("Loading weather for $city...")
        scope.launch {
            val status = model.fetchWeather(city)
            view.render(status)
        }
    }
}

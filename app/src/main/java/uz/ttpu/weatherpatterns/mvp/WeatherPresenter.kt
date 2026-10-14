package uz.ttpu.weatherpatterns.mvp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

class WeatherPresenter(
    private val repository: WeatherRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : WeatherContract.Presenter {

    private var view: WeatherContract.View? = null
    private var fetchJob: Job? = null

    override fun attachView(view: WeatherContract.View) {
        this.view = view
    }

    override fun detachView() {
        fetchJob?.cancel()
        this.view = null
    }

    override fun loadWeather(city: String) {
        fetchJob?.cancel()
        view?.showLoading(city)
        view?.renderStatus("Loading weather for $city...")
        fetchJob = scope.launch {
            val result = repository.getWeather(city)
            result.fold(
                onSuccess = { weather ->
                    view?.showWeather(weather)
                    view?.renderStatus("${weather.city}: ${weather.temperatureC}°C, ${weather.condition}")
                },
                onFailure = { error ->
                    val errorMsg = error.message ?: "Failed to fetch weather"
                    view?.showError(errorMsg)
                    view?.renderStatus("Error: $errorMsg")
                }
            )
        }
    }
}

package uz.ttpu.weatherpatterns.mvp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import uz.ttpu.weatherpatterns.data.model.Weather
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.ui.WeatherScreen
import uz.ttpu.weatherpatterns.ui.theme.WeatherPatternsTheme

class MvpActivity : ComponentActivity(), WeatherContract.View {
    private lateinit var presenter: WeatherPresenter
    private var statusText by mutableStateOf("Select a city")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = WeatherPresenter(WeatherModule.repository, lifecycleScope)
        presenter.attachView(this)

        setContent {
            WeatherPatternsTheme {
                WeatherScreen(
                    status = statusText,
                    onCitySelected = { city -> presenter.loadWeather(city) },
                    title = "MVP Weather"
                )
            }
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showLoading(city: String) {
        // Handled via renderStatus or custom UI if needed
    }

    override fun showWeather(weather: Weather) {
        // Handled via renderStatus or custom UI if needed
    }

    override fun showError(message: String) {
        // Handled via renderStatus or custom UI if needed
    }

    override fun renderStatus(status: String) {
        statusText = status
    }
}

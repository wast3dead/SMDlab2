package uz.ttpu.weatherpatterns.mvc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.ui.WeatherScreen
import uz.ttpu.weatherpatterns.ui.theme.WeatherPatternsTheme

class MvcActivity : ComponentActivity(), WeatherView {
    private lateinit var model: WeatherModel
    private lateinit var controller: WeatherController
    private var currentStatus by mutableStateOf("Select a city")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = WeatherModel(WeatherModule.repository)
        controller = WeatherController(model, this, lifecycleScope)

        setContent {
            WeatherPatternsTheme {
                WeatherScreen(
                    status = currentStatus,
                    onCitySelected = { city -> controller.onCitySelected(city) },
                    title = "MVC Weather"
                )
            }
        }
    }

    override fun render(status: String) {
        currentStatus = status
    }
}

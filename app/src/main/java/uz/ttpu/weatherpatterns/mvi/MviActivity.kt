package uz.ttpu.weatherpatterns.mvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.ui.WeatherScreen
import uz.ttpu.weatherpatterns.ui.theme.WeatherPatternsTheme

class MviActivity : ComponentActivity() {
    private val viewModel: WeatherMviViewModel by viewModels { WeatherModule.viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherPatternsTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                WeatherScreen(
                    status = state.toStatusText(),
                    onCitySelected = { city -> viewModel.processIntent(WeatherIntent.LoadWeather(city)) },
                    title = "MVI Weather"
                )
            }
        }
    }
}

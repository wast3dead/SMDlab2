package uz.ttpu.weatherpatterns.mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.ui.WeatherScreen
import uz.ttpu.weatherpatterns.ui.theme.WeatherPatternsTheme

class MvvmActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels { WeatherModule.viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherPatternsTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                WeatherScreen(
                    status = uiState.toStatusText(),
                    onCitySelected = { city -> viewModel.fetchWeather(city) },
                    title = "MVVM Weather"
                )
            }
        }
    }
}

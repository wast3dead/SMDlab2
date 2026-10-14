package uz.ttpu.weatherpatterns.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

class WeatherMviViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    fun processIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.LoadWeather -> loadWeather(intent.city)
        }
    }

    private fun loadWeather(city: String) {
        _state.update {
            it.copy(isLoading = true, city = city, weather = null, error = null)
        }
        viewModelScope.launch {
            val result = repository.getWeather(city)
            result.fold(
                onSuccess = { weather ->
                    _state.update {
                        it.copy(isLoading = false, weather = weather, error = null)
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message ?: "Failed to fetch weather", weather = null)
                    }
                }
            )
        }
    }
}

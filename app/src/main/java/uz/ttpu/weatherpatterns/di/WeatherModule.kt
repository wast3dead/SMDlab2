package uz.ttpu.weatherpatterns.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.ttpu.weatherpatterns.data.api.FakeWeatherApi
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

object WeatherModule {
    val repository: WeatherRepository by lazy {
        WeatherRepository(FakeWeatherApi(delayMs = 3000L))
    }

    fun <T : ViewModel> createFactory(creator: (WeatherRepository) -> T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                return creator(repository) as VM
            }
        }
    }

    val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val constructor = modelClass.getConstructor(WeatherRepository::class.java)
            return constructor.newInstance(repository)
        }
    }
}

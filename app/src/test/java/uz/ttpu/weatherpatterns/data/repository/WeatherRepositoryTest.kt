package uz.ttpu.weatherpatterns.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.ttpu.weatherpatterns.data.api.WeatherApi
import uz.ttpu.weatherpatterns.data.model.Weather

class WeatherRepositoryTest {

    private class TestWeatherApi(private val shouldFail: Boolean = false) : WeatherApi {
        override suspend fun fetchWeather(city: String): Weather {
            if (shouldFail) {
                throw RuntimeException("Network error")
            }
            return Weather(city, 25, "Clear")
        }
    }

    @Test
    fun getWeather_success_returnsSuccessResult() = runTest {
        val repository = WeatherRepository(TestWeatherApi(shouldFail = false))
        val result = repository.getWeather("Tashkent")

        assertTrue(result.isSuccess)
        assertEquals(Weather("Tashkent", 25, "Clear"), result.getOrNull())
    }

    @Test
    fun getWeather_failure_returnsFailureResult() = runTest {
        val repository = WeatherRepository(TestWeatherApi(shouldFail = true))
        val result = repository.getWeather("Unknown")

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}

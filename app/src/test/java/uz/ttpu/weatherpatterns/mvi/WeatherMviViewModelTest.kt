package uz.ttpu.weatherpatterns.mvi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import uz.ttpu.weatherpatterns.data.api.WeatherApi
import uz.ttpu.weatherpatterns.data.model.Weather
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherMviViewModelTest {

    private class FakeWeatherApi(private val shouldFail: Boolean) : WeatherApi {
        override suspend fun fetchWeather(city: String): Weather {
            if (shouldFail) throw RuntimeException("Error loading weather")
            return Weather("Tokyo", 22, "Cloudy")
        }
    }

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun processIntent_loadWeather_success_updatesState() = runTest(testDispatcher) {
        val repository = WeatherRepository(FakeWeatherApi(shouldFail = false))
        val viewModel = WeatherMviViewModel(repository)

        viewModel.processIntent(WeatherIntent.LoadWeather("Tokyo"))

        // Immediately after intent, state should be loading
        val loadingState = viewModel.state.value
        assertTrue(loadingState.isLoading)
        assertEquals("Tokyo", loadingState.city)

        // Advance test dispatcher to complete coroutine
        testScheduler.advanceUntilIdle()

        val successState = viewModel.state.value
        assertFalse(successState.isLoading)
        assertEquals(Weather("Tokyo", 22, "Cloudy"), successState.weather)
        assertNull(successState.error)
    }

    @Test
    fun processIntent_loadWeather_failure_updatesStateWithError() = runTest(testDispatcher) {
        val repository = WeatherRepository(FakeWeatherApi(shouldFail = true))
        val viewModel = WeatherMviViewModel(repository)

        viewModel.processIntent(WeatherIntent.LoadWeather("Unknown"))

        testScheduler.advanceUntilIdle()

        val errorState = viewModel.state.value
        assertFalse(errorState.isLoading)
        assertNull(errorState.weather)
        assertEquals("Error loading weather", errorState.error)
    }
}

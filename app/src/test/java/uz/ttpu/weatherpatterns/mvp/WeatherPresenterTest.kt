package uz.ttpu.weatherpatterns.mvp

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import uz.ttpu.weatherpatterns.data.api.WeatherApi
import uz.ttpu.weatherpatterns.data.model.Weather
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherPresenterTest {

    private class FakeWeatherApi(private val shouldFail: Boolean) : WeatherApi {
        override suspend fun fetchWeather(city: String): Weather {
            if (shouldFail) throw RuntimeException("City not found")
            return Weather("Tashkent", 28, "Sunny")
        }
    }

    private class FakeView : WeatherContract.View {
        var lastStatus: String = ""
        var loadedCity: String = ""
        var shownWeather: Weather? = null
        var errorMessage: String = ""

        override fun showLoading(city: String) {
            loadedCity = city
        }

        override fun showWeather(weather: Weather) {
            shownWeather = weather
        }

        override fun showError(message: String) {
            errorMessage = message
        }

        override fun renderStatus(status: String) {
            lastStatus = status
        }
    }

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Test
    fun loadWeather_success_updatesViewWithWeather() = testScope.runTest {
        val repository = WeatherRepository(FakeWeatherApi(shouldFail = false))
        val presenter = WeatherPresenter(repository, this)
        val fakeView = FakeView()

        presenter.attachView(fakeView)
        presenter.loadWeather("Tashkent")

        assertEquals("Tashkent", fakeView.loadedCity)
        assertEquals(Weather("Tashkent", 28, "Sunny"), fakeView.shownWeather)
        assertEquals("Tashkent: 28°C, Sunny", fakeView.lastStatus)
    }

    @Test
    fun loadWeather_failure_updatesViewWithError() = testScope.runTest {
        val repository = WeatherRepository(FakeWeatherApi(shouldFail = true))
        val presenter = WeatherPresenter(repository, this)
        val fakeView = FakeView()

        presenter.attachView(fakeView)
        presenter.loadWeather("Unknown")

        assertEquals("City not found", fakeView.errorMessage)
        assertEquals("Error: City not found", fakeView.lastStatus)
    }
}

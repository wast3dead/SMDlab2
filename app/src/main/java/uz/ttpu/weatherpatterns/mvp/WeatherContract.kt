package uz.ttpu.weatherpatterns.mvp

import uz.ttpu.weatherpatterns.data.model.Weather

interface WeatherContract {
    interface View {
        fun showLoading(city: String)
        fun showWeather(weather: Weather)
        fun showError(message: String)
        fun renderStatus(status: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadWeather(city: String)
    }
}

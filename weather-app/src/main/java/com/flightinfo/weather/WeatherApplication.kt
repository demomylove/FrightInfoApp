package com.flightinfo.weather

import android.app.Application
import com.flightinfo.weather.data.AppContainer

class WeatherApplication : Application() {
    var container = AppContainer()
}

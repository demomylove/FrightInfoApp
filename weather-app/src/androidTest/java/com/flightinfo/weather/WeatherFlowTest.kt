package com.flightinfo.weather

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flightinfo.weather.data.*
import com.flightinfo.weather.ui.WeatherActivity
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherFlowTest {
    @Test fun searchSelectRefreshFailureRetryAndRotation() {
        val app = ApplicationProvider.getApplicationContext<WeatherApplication>()
        val previous = app.container
        val clock = Clock.fixed(Instant.parse("2026-12-31T10:00:00Z"), ZoneOffset.UTC)
        val city = City(1816670, "北京", listOf("北京市"), 39.9075, 116.39723, "Asia/Shanghai")
        var requests = 0
        app.container = AppContainer(clock, object : WeatherRepository {
            override suspend fun search(query: String) = if (query == "xyz") emptyList() else listOf(city)
            override suspend fun load(city: City): WeatherSnapshot {
                requests++
                if (requests == 2) throw WeatherFailure(WeatherFailure.Kind.NETWORK)
                return WeatherSnapshot(city, clock.instant(), today(clock), today(clock).atTime(18, 0), 0.0, 0,
                    targetDates(today(clock)).map { DayWeather(it, 1, 10.0, -2.0) }, 39.9, 116.4)
            }
        })
        try {
            ActivityScenario.launch(WeatherActivity::class.java).use { scenario ->
                onView(withId(R.id.search)).perform(click())
                onView(withId(R.id.searchStatus)).check(matches(withText("请输入城市名称")))
                onView(withId(R.id.query)).perform(replaceText("xyz"), closeSoftKeyboard())
                onView(withId(R.id.search)).perform(click())
                onView(withId(R.id.searchStatus)).check(matches(withText(containsString("未找到城市"))))
                onView(withId(R.id.query)).perform(replaceText("beijing"), closeSoftKeyboard())
                onView(withId(R.id.search)).perform(click())
                onView(withText(city.label)).perform(click())
                onView(withId(R.id.current)).check(matches(withText("0℃ · 晴")))
                onView(withId(R.id.refresh)).perform(scrollTo(), click())
                onView(withId(R.id.weatherStatus)).check(matches(withText(containsString("更新失败，显示上次数据"))))
                onView(withId(R.id.refresh)).perform(click())
                onView(withId(R.id.weatherStatus)).check(matches(withText("已更新")))
                scenario.recreate()
                onView(withId(R.id.current)).check(matches(withText("0℃ · 晴")))
                org.junit.Assert.assertEquals(3, requests)
            }
        } finally { app.container = previous }
    }

    @Test fun realPhoneNetworkSupportsThreeCitiesAndSevenDates() {
        val app = ApplicationProvider.getApplicationContext<WeatherApplication>()
        val previous = app.container
        val clock = Clock.systemUTC()
        val folder = app.getExternalFilesDir(null)!!.resolve("weather-evidence").apply { mkdirs() }
        var sequence = 0
        val client = okhttp3.OkHttpClient.Builder().callTimeout(25, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val response = chain.proceed(chain.request())
                val index = ++sequence
                folder.resolve("response-$index.json").writeText(response.peekBody(1024 * 1024).string())
                folder.resolve("request-$index.txt").writeText("${java.time.Instant.now()}\n${chain.request().url}\nHTTP ${response.code}")
                response
            }.build()
        fun retrofit(url: String) = retrofit2.Retrofit.Builder().baseUrl(url).client(client)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create()).build()
        val real = NetworkWeatherRepository(retrofit("https://geocoding-api.open-meteo.com/").create(GeocodingApi::class.java),
            retrofit("https://api.open-meteo.com/").create(ForecastApi::class.java), WeatherMapper(clock))
        var candidates = emptyList<City>()
        var snapshot: WeatherSnapshot? = null
        app.container = AppContainer(clock, object : WeatherRepository {
            override suspend fun search(query: String) = real.search(query).also { candidates = it }
            override suspend fun load(city: City) = real.load(city).also { snapshot = it }
        })
        try {
            ActivityScenario.launch(WeatherActivity::class.java).use { scenario ->
                for (name in listOf("北京", "上海", "广州")) {
                    onView(withId(R.id.query)).perform(scrollTo(), replaceText(name), closeSoftKeyboard())
                    onView(withId(R.id.search)).perform(scrollTo(), click())
                    awaitText(scenario, R.id.searchStatus, "请选择地区")
                    val city = candidates.first()
                    onView(org.hamcrest.Matchers.allOf(withText(city.label), isAssignableFrom(android.widget.Button::class.java)))
                        .perform(scrollTo(), click())
                    awaitText(scenario, R.id.weatherStatus, "已更新")
                    val result = snapshot!!
                    org.junit.Assert.assertTrue(result.complete)
                    org.junit.Assert.assertEquals(city, result.city)
                    org.junit.Assert.assertEquals(targetDates(today(clock)), result.days.map { it.date })
                    onView(withId(R.id.city)).check(matches(withText(city.label)))
                    val temperature = java.text.DecimalFormat("0.#", java.text.DecimalFormatSymbols(java.util.Locale.CHINA)).format(result.temperature)
                    onView(withId(R.id.current)).check(matches(withText("${temperature}℃ · ${weatherText(result.code)}")))
                    scenario.onActivity { activity ->
                        val rows = activity.findViewById<android.widget.LinearLayout>(R.id.forecast)
                        org.junit.Assert.assertEquals(7, rows.childCount)
                        result.days.forEachIndexed { index, day ->
                            val text = (rows.getChildAt(index) as android.widget.TextView).text.toString()
                            val formatted = day.date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy年M月d日 EEEE", java.util.Locale.CHINA))
                            org.junit.Assert.assertTrue(text.contains(formatted))
                        }
                    }
                    folder.resolve("$name-snapshot.txt").writeText(result.toString())
                    onView(withId(R.id.current)).perform(scrollTo())
                    screenshot(scenario, folder.resolve("$name-current.png"))
                    onView(withId(R.id.sources)).perform(scrollTo())
                    screenshot(scenario, folder.resolve("$name-forecast.png"))
                    val before = sequence
                    onView(withId(R.id.refresh)).perform(scrollTo(), click())
                    awaitText(scenario, R.id.weatherStatus, "已更新")
                    org.junit.Assert.assertTrue(sequence > before)
                }
            }
        } finally { app.container = previous }
    }

    private fun awaitText(scenario: ActivityScenario<WeatherActivity>, id: Int, expected: String) {
        val deadline = System.currentTimeMillis() + 30000
        do {
            var text = ""
            scenario.onActivity { text = it.findViewById<android.widget.TextView>(id).text.toString() }
            if (text.contains(expected)) return
            if (System.currentTimeMillis() >= deadline) throw AssertionError("Expected $expected but found $text")
            Thread.sleep(100)
        } while (true)
    }

    private fun screenshot(scenario: ActivityScenario<WeatherActivity>, file: java.io.File) {
        // Capture only our activity. Do not register another system accessibility service,
        // which conflicts with existing automation on some physical devices.
        scenario.onActivity { activity ->
            val view = activity.window.decorView
            val bitmap = android.graphics.Bitmap.createBitmap(view.width, view.height, android.graphics.Bitmap.Config.ARGB_8888)
            view.draw(android.graphics.Canvas(bitmap))
            file.outputStream().use { bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
            bitmap.recycle()
        }
    }
}

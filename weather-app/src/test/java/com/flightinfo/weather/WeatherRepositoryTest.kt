package com.flightinfo.weather

import com.flightinfo.weather.data.*
import java.time.Clock
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepositoryTest {
    private val server = MockWebServer()
    private lateinit var repository: WeatherRepository
    private val city = City(1, "北京", emptyList(), 39.9, 116.4, null)
    @Before fun setup() {
        server.start()
        val retrofit = Retrofit.Builder().baseUrl(server.url("/"))
            .client(OkHttpClient.Builder().callTimeout(300, TimeUnit.MILLISECONDS).build())
            .addConverterFactory(GsonConverterFactory.create()).build()
        repository = NetworkWeatherRepository(retrofit.create(GeocodingApi::class.java), retrofit.create(ForecastApi::class.java), WeatherMapper(Clock.systemUTC()))
    }
    @After fun cleanup() { server.shutdown() }
    @Test fun chineseQueryAndForecastContractUseCorrectParameters() = runBlocking {
        server.enqueue(MockResponse().setBody("{}"))
        assertTrue(repository.search("北京").isEmpty())
        val search = server.takeRequest().requestUrl!!
        assertEquals("北京", search.queryParameter("name"))
        assertEquals("CN", search.queryParameter("countryCode"))
        assertEquals("zh", search.queryParameter("language"))
        server.enqueue(MockResponse().setBody("""{"timezone":"Asia/Shanghai","utc_offset_seconds":28800}"""))
        repository.load(city)
        val weather = server.takeRequest().requestUrl!!
        assertEquals("39.9", weather.queryParameter("latitude"))
        assertEquals("116.4", weather.queryParameter("longitude"))
        assertEquals("8", weather.queryParameter("forecast_days"))
        assertEquals("Asia/Shanghai", weather.queryParameter("timezone"))
        assertEquals("temperature_2m,weather_code", weather.queryParameter("current"))
        assertEquals("celsius", weather.queryParameter("temperature_unit"))
    }
    @Test fun serviceRateLimitAndMalformedJsonEndAsTypedErrors() = runBlocking {
        for ((response, kind) in listOf(
            MockResponse().setResponseCode(500) to WeatherFailure.Kind.SERVICE,
            MockResponse().setResponseCode(429) to WeatherFailure.Kind.RATE_LIMIT,
            MockResponse().setBody("""{"error":true}""") to WeatherFailure.Kind.SERVICE,
            MockResponse().setBody("""{"results":"bad"}""") to WeatherFailure.Kind.FORMAT,
        )) {
            server.enqueue(response)
            val error = runCatching { repository.search("北京") }.exceptionOrNull()
            assertTrue(error is WeatherFailure)
            assertEquals(kind, (error as WeatherFailure).kind)
        }
    }
    @Test fun timeoutCanBeRetriedWithoutCachedResponse() = runBlocking {
        server.enqueue(MockResponse().setBody("{}").setBodyDelay(1, TimeUnit.SECONDS))
        val failure = runCatching { repository.search("北京") }.exceptionOrNull()
        assertEquals(WeatherFailure.Kind.NETWORK, (failure as WeatherFailure).kind)
        server.enqueue(MockResponse().setBody("{}"))
        assertTrue(repository.search("北京").isEmpty())
        assertEquals(2, server.requestCount)
    }
}

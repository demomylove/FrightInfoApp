package com.flightinfo.weather.data

import com.google.gson.JsonParseException
import java.io.IOException
import java.io.EOFException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CancellationException
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.Clock
import java.util.concurrent.TimeUnit

interface GeocodingApi {
    @GET("v1/search")
    suspend fun search(@Query("name") name: String, @Query("count") count: Int = 10,
        @Query("language") language: String = "zh", @Query("format") format: String = "json",
        @Query("countryCode") country: String = "CN"): SearchDto
}
interface ForecastApi {
    @GET("v1/forecast")
    suspend fun forecast(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,weather_code",
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min",
        @Query("timezone") timezone: String = "Asia/Shanghai", @Query("forecast_days") days: Int = 8,
        @Query("temperature_unit") unit: String = "celsius"): ForecastDto
}
interface WeatherRepository {
    suspend fun search(query: String): List<City>
    suspend fun load(city: City): WeatherSnapshot
}
class NetworkWeatherRepository(private val geocoding: GeocodingApi, private val forecast: ForecastApi,
    private val mapper: WeatherMapper) : WeatherRepository {
    override suspend fun search(query: String): List<City> = translate { mapper.cities(geocoding.search(query)) }
    override suspend fun load(city: City): WeatherSnapshot = translate { mapper.snapshot(city, forecast.forecast(city.latitude, city.longitude)) }
    private suspend fun <T> translate(block: suspend () -> T): T = try {
        block()
    } catch (e: CancellationException) { throw e
    } catch (e: WeatherFailure) { throw e
    } catch (e: HttpException) {
        throw WeatherFailure(if (e.code() == 429) WeatherFailure.Kind.RATE_LIMIT else WeatherFailure.Kind.SERVICE)
    } catch (e: JsonParseException) { throw WeatherFailure(WeatherFailure.Kind.FORMAT)
    } catch (e: MalformedJsonException) { throw WeatherFailure(WeatherFailure.Kind.FORMAT)
    } catch (e: EOFException) { throw WeatherFailure(WeatherFailure.Kind.FORMAT)
    } catch (e: KotlinNullPointerException) { throw WeatherFailure(WeatherFailure.Kind.FORMAT)
    } catch (e: IOException) { throw WeatherFailure(WeatherFailure.Kind.NETWORK)
    } catch (e: IllegalStateException) { throw WeatherFailure(WeatherFailure.Kind.FORMAT)
    }
}
class AppContainer(val clock: Clock = Clock.systemUTC(), repository: WeatherRepository? = null) {
    val repository: WeatherRepository = repository ?: run {
        val client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS).callTimeout(20, TimeUnit.SECONDS).build()
        fun service(url: String) = Retrofit.Builder().baseUrl(url).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        NetworkWeatherRepository(service("https://geocoding-api.open-meteo.com/").create(GeocodingApi::class.java),
            service("https://api.open-meteo.com/").create(ForecastApi::class.java), WeatherMapper(clock))
    }
}

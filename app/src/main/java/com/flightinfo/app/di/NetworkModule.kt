package com.flightinfo.app.di

import android.content.Context
import android.content.pm.ApplicationInfo
import com.flightinfo.app.data.api.FlightApiService
import com.flightinfo.app.utils.network.CacheInterceptor
import com.flightinfo.app.utils.network.NetworkInterceptor
import com.flightinfo.app.utils.network.OfflineCacheInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        cacheInterceptor: CacheInterceptor,
        networkInterceptor: NetworkInterceptor,
        offlineCacheInterceptor: OfflineCacheInterceptor,
    ): OkHttpClient {
        // 创建缓存目录
        val cacheDir = File(context.cacheDir, "http_cache")
        val cacheSize = 50 * 1024 * 1024 // 50 MB

        return OkHttpClient.Builder()
            .cache(Cache(cacheDir, cacheSize.toLong()))
            // 添加网络拦截器
            .addNetworkInterceptor(networkInterceptor)
            // 添加应用拦截器
            .addInterceptor(offlineCacheInterceptor)
            .addInterceptor(cacheInterceptor)
            // 配置日志拦截器（仅在调试模式下启用详细日志）
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (isDebugBuild(context)) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                },
            )
            // 配置超时时间
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            // 配置重试策略
            .retryOnConnectionFailure(true)
            .build()
    }

    private fun isDebugBuild(context: Context): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FlightApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFlightApiService(retrofit: Retrofit): FlightApiService {
        return retrofit.create(FlightApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCacheInterceptor(): CacheInterceptor {
        return CacheInterceptor()
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(): NetworkInterceptor {
        return NetworkInterceptor()
    }

    @Provides
    @Singleton
    fun provideOfflineCacheInterceptor(): OfflineCacheInterceptor {
        return OfflineCacheInterceptor()
    }
}

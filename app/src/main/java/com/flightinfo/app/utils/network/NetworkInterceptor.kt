package com.flightinfo.app.utils.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 网络拦截器 - 处理网络请求和响应
 */
class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 添加通用请求头
        val requestBuilder = originalRequest.newBuilder()
            .header("User-Agent", "FlightInfoApp/1.0")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

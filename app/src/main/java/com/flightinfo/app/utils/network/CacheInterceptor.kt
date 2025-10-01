package com.flightinfo.app.utils.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器 - 处理HTTP缓存策略
 */
class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        // 为GET请求添加缓存控制头
        return if (originalRequest.method == "GET") {
            response.newBuilder()
                .header("Cache-Control", "public, max-age=300") // 缓存5分钟
                .build()
        } else {
            response
        }
    }
}

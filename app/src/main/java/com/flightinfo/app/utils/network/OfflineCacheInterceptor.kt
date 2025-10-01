package com.flightinfo.app.utils.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 离线缓存拦截器 - 处理离线情况下的缓存策略
 */
class OfflineCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // 为GET请求添加离线缓存策略
        if (request.method == "GET") {
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=86400") // 离线缓存24小时
                .build()
        }

        return chain.proceed(request)
    }
}

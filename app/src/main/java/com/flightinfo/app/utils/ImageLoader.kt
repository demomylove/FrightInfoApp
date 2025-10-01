package com.flightinfo.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

/**
 * 图片加载工具类
 * 支持懒加载、缓存和预加载功能
 */
object ImageLoader {

    private val memoryCache = ConcurrentHashMap<String, Bitmap>()
    private val diskCache = mutableMapOf<String, String>() // 简化的磁盘缓存
    private val loadingJobs = ConcurrentHashMap<String, Job>()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * 异步加载图片到ImageView
     */
    fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        placeholderResId: Int? = null,
        errorResId: Int? = null,
    ) {
        // 设置占位符
        placeholderResId?.let { imageView.setImageResource(it) }

        // 检查内存缓存
        val cachedBitmap = memoryCache[url]
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap)
            return
        }

        // 检查是否有正在进行的加载任务
        val existingJob = loadingJobs[url]
        if (existingJob != null && existingJob.isActive) {
            // 等待现有任务完成
            scope.launch(Dispatchers.Main) {
                existingJob.join()
                val bitmap = memoryCache[url]
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    errorResId?.let { imageView.setImageResource(it) }
                }
            }
            return
        }

        // 启动新的加载任务
        val job = scope.launch {
            try {
                loadImageInternal(url)
            } catch (e: Exception) {
                PerformanceMonitor.logError("ImageLoader", "Failed to load image: $url, ${e.message}")
            }
        }

        loadingJobs[url] = job

        // 在主线程更新UI
        scope.launch(Dispatchers.Main) {
            job.join()
            loadingJobs.remove(url)

            val bitmap = memoryCache[url]
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            } else {
                errorResId?.let { imageView.setImageResource(it) }
            }
        }
    }

    /**
     * 预加载图片到缓存
     */
    fun preloadImage(url: String) {
        if (memoryCache.containsKey(url)) return

        scope.launch {
            try {
                loadImageInternal(url)
            } catch (e: Exception) {
                PerformanceMonitor.logError("ImagePreloader", "Failed to preload image: $url, ${e.message}")
            }
        }
    }

    /**
     * 清除所有缓存
     */
    fun clearCache() {
        memoryCache.clear()
        diskCache.clear()
        loadingJobs.values.forEach { it.cancel() }
        loadingJobs.clear()
    }

    /**
     * 获取缓存统计信息
     */
    fun getCacheStats(): String {
        val memorySize = memoryCache.values.sumOf { bitmap ->
            bitmap.allocationByteCount.toLong()
        }
        return "Memory cache: ${memoryCache.size} items, ${formatBytes(memorySize)}"
    }

    private suspend fun loadImageInternal(url: String) {
        // 检查磁盘缓存
        val cachedPath = diskCache[url]
        if (cachedPath != null) {
            val bitmap = BitmapFactory.decodeFile(cachedPath)
            if (bitmap != null) {
                memoryCache[url] = bitmap
                return
            }
        }

        // 从网络加载
        val bitmap = withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection()
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                connection.getInputStream().use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: Exception) {
                null
            }
        }

        if (bitmap != null) {
            memoryCache[url] = bitmap
            PerformanceMonitor.logPerformance("ImageLoader", "Loaded image: $url, size: ${bitmap.width}x${bitmap.height}")
        }
    }

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 -> "${bytes / (1024 * 1024)}MB"
            bytes >= 1024 -> "${bytes / 1024}KB"
            else -> "$bytes B"
        }
    }
}

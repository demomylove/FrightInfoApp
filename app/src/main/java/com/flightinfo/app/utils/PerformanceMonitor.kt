package com.flightinfo.app.utils

import android.app.Application
import android.content.Context
import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 性能监控工具类
 * 用于监控应用启动性能、内存使用、网络请求等关键指标
 */
object PerformanceMonitor {

    private var startTime: Long = 0
    private var isMonitoring = false
    private lateinit var context: Context
    private val performanceLogs = mutableListOf<String>()
    private val monitorScope = CoroutineScope(Dispatchers.IO)

    /**
     * 开始监控应用性能
     */
    fun startMonitoring(application: Application) {
        if (isMonitoring) return

        context = application.applicationContext
        startTime = SystemClock.elapsedRealtime()
        isMonitoring = true

        // 记录启动时间
        logPerformance("AppStart", "Application onCreate started")

        // 启动内存监控
        startMemoryMonitoring()

        // 启动网络监控
        startNetworkMonitoring()
    }

    /**
     * 记录性能指标
     */
    fun logPerformance(tag: String, message: String) {
        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        val logMessage = "[${getCurrentTime()}] [$tag] $message (T+$elapsedTime ms)"

        performanceLogs.add(logMessage)

        // 保存到文件
        saveLogToFile(logMessage)
    }

    /**
     * 记录错误信息
     */
    fun logError(tag: String, error: String) {
        val logMessage = "[${getCurrentTime()}] [$tag] ERROR: $error"
        performanceLogs.add(logMessage)
        saveLogToFile(logMessage)
    }

    /**
     * 获取应用启动耗时
     */
    fun getStartupTime(): Long {
        return SystemClock.elapsedRealtime() - startTime
    }

    private fun startMemoryMonitoring() {
        monitorScope.launch {
            try {
                val runtime = Runtime.getRuntime()
                val maxMemory = runtime.maxMemory()
                val totalMemory = runtime.totalMemory()
                val freeMemory = runtime.freeMemory()

                logPerformance(
                    "Memory",
                    "Max: ${formatBytes(maxMemory)}, " +
                        "Total: ${formatBytes(totalMemory)}, " +
                        "Free: ${formatBytes(freeMemory)}, " +
                        "Used: ${formatBytes(totalMemory - freeMemory)}",
                )
            } catch (e: Exception) {
                logError("MemoryMonitor", e.message ?: "Unknown error")
            }
        }
    }

    private fun startNetworkMonitoring() {
        // 这里可以添加网络请求监控逻辑
        logPerformance("Network", "Network monitoring initialized")
    }

    private fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
    }

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 -> "${bytes / (1024 * 1024)}MB"
            bytes >= 1024 -> "${bytes / 1024}KB"
            else -> "$bytes B"
        }
    }

    private fun saveLogToFile(message: String) {
        monitorScope.launch {
            try {
                val logDir = File(context.filesDir, "performance_logs")
                if (!logDir.exists()) {
                    logDir.mkdirs()
                }

                val logFile = File(logDir, "performance_${getCurrentDate()}.txt")
                logFile.appendText("$message\n")
            } catch (e: Exception) {
                // 静默处理文件写入错误
            }
        }
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}

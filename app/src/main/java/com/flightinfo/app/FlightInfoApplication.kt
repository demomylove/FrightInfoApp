package com.flightinfo.app

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.flightinfo.app.utils.BackgroundSyncScheduler
import com.flightinfo.app.utils.PerformanceMonitor
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class FlightInfoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val mainHandler = Handler(Looper.getMainLooper())
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        // 启动性能监控
        PerformanceMonitor.startMonitoring(this)

        // 启用严格模式（仅在调试模式下）
        if (isDebugBuild()) {
            enableStrictMode()
        }

        // 延迟初始化后台任务，避免阻塞主线程
        mainHandler.postDelayed({
            initializeBackgroundTasks()
        }, 1000) // 延迟1秒初始化
    }

    private fun initializeBackgroundTasks() {
        applicationScope.launch {
            try {
                // Schedule periodic background sync for tracked flights
                BackgroundSyncScheduler.schedule(this@FlightInfoApplication)
            } catch (e: Exception) {
                // 记录初始化失败，但不崩溃应用
                PerformanceMonitor.logError("BackgroundTaskInit", e.message ?: "Unknown error")
            }
        }
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build(),
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build(),
        )
    }

    private fun isDebugBuild(): Boolean {
        return (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    companion object {
        fun getAppContext(context: Context): FlightInfoApplication {
            return context.applicationContext as FlightInfoApplication
        }
    }
}

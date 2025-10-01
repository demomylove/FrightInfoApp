package com.flightinfo.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.flightinfo.app.R
import com.flightinfo.app.data.model.NotificationType
import com.flightinfo.app.ui.MainActivity
import com.flightinfo.app.utils.EnhancedNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 通知闹钟接收器
 * 处理定时通知的广播接收
 */
@AndroidEntryPoint
class NotificationAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: EnhancedNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        // 获取通知信息
        val notificationId = intent.getStringExtra("notification_id") ?: return
        val title = intent.getStringExtra("title") ?: return
        val message = intent.getStringExtra("message") ?: return
        val typeString = intent.getStringExtra("type") ?: return

        try {
            val type = NotificationType.valueOf(typeString)

            // 显示通知
            showScheduledNotification(context, title, message, type)
        } catch (e: Exception) {
            // 处理类型转换错误
            showScheduledNotification(context, title, message, NotificationType.CUSTOM)
        }
    }

    private fun showScheduledNotification(
        context: Context,
        title: String,
        message: String,
        type: NotificationType,
    ) {
        val notification = NotificationCompat.Builder(context, EnhancedNotificationManager.SYSTEM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flight)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createMainActivityPendingIntent(context))
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(
            System.currentTimeMillis().toInt(),
            notification,
        )
    }

    private fun createMainActivityPendingIntent(context: Context): android.app.PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return android.app.PendingIntent.getActivity(
            context,
            0,
            intent,
            android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }
}

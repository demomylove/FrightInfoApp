package com.flightinfo.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\u0016\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/flightinfo/app/utils/NotificationHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "createNotificationChannel", "", "showFlightStatusNotification", "flightNumber", "", "newStatus", "Companion", "app_debug"})
public final class NotificationHelper {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_ID = "flight_status_channel";
    private static final int NOTIFICATION_ID_BASE = 10000;
    @org.jetbrains.annotations.NotNull
    public static final com.flightinfo.app.utils.NotificationHelper.Companion Companion = null;
    
    public NotificationHelper(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    private final void createNotificationChannel() {
    }
    
    public final void showFlightStatusNotification(@org.jetbrains.annotations.NotNull
    java.lang.String flightNumber, @org.jetbrains.annotations.NotNull
    java.lang.String newStatus) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/flightinfo/app/utils/NotificationHelper$Companion;", "", "()V", "CHANNEL_ID", "", "NOTIFICATION_ID_BASE", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
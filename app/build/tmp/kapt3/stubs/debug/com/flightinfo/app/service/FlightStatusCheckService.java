package com.flightinfo.app.service;

@dagger.hilt.android.AndroidEntryPoint
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0007\u0018\u0000 $2\u00020\u0001:\u0002$%B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0012H\u0002J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u0012H\u0016J\b\u0010\u001b\u001a\u00020\u0012H\u0016J\"\u0010\u001c\u001a\u00020\u001d2\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u001dH\u0016J\u0018\u0010 \u001a\u00020\u00122\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"H\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00060\u000eR\u00020\u0000X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/flightinfo/app/service/FlightStatusCheckService;", "Landroid/app/Service;", "()V", "flightRepository", "Lcom/flightinfo/app/data/repository/FlightRepository;", "getFlightRepository", "()Lcom/flightinfo/app/data/repository/FlightRepository;", "setFlightRepository", "(Lcom/flightinfo/app/data/repository/FlightRepository;)V", "handlerThread", "Landroid/os/HandlerThread;", "notificationHelper", "Lcom/flightinfo/app/utils/NotificationHelper;", "serviceHandler", "Lcom/flightinfo/app/service/FlightStatusCheckService$ServiceHandler;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "checkFlightStatuses", "", "createNotification", "Landroid/app/Notification;", "createNotificationChannel", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "sendFlightStatusNotification", "flightNumber", "", "newStatus", "Companion", "ServiceHandler", "app_debug"})
public final class FlightStatusCheckService extends android.app.Service {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String CHANNEL_ID = "flight_status_check_channel";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String CHANNEL_NAME = "Flight Status Checks";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String CHANNEL_DESCRIPTION = "Background flight status checking";
    private static final int NOTIFICATION_ID = 1001;
    private static final long CHECK_INTERVAL = 60000L;
    @javax.inject.Inject
    public com.flightinfo.app.data.repository.FlightRepository flightRepository;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    private android.os.HandlerThread handlerThread;
    private com.flightinfo.app.service.FlightStatusCheckService.ServiceHandler serviceHandler;
    private com.flightinfo.app.utils.NotificationHelper notificationHelper;
    @org.jetbrains.annotations.NotNull
    public static final com.flightinfo.app.service.FlightStatusCheckService.Companion Companion = null;
    
    public FlightStatusCheckService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.repository.FlightRepository getFlightRepository() {
        return null;
    }
    
    public final void setFlightRepository(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.repository.FlightRepository p0) {
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public android.os.IBinder onBind(@org.jetbrains.annotations.NotNull
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    private final void checkFlightStatuses() {
    }
    
    private final void sendFlightStatusNotification(java.lang.String flightNumber, java.lang.String newStatus) {
    }
    
    private final void createNotificationChannel() {
    }
    
    private final android.app.Notification createNotification() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/flightinfo/app/service/FlightStatusCheckService$Companion;", "", "()V", "CHANNEL_DESCRIPTION", "", "CHANNEL_ID", "CHANNEL_NAME", "CHECK_INTERVAL", "", "NOTIFICATION_ID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016\u00a8\u0006\t"}, d2 = {"Lcom/flightinfo/app/service/FlightStatusCheckService$ServiceHandler;", "Landroid/os/Handler;", "looper", "Landroid/os/Looper;", "(Lcom/flightinfo/app/service/FlightStatusCheckService;Landroid/os/Looper;)V", "handleMessage", "", "msg", "Landroid/os/Message;", "app_debug"})
    final class ServiceHandler extends android.os.Handler {
        
        public ServiceHandler(@org.jetbrains.annotations.NotNull
        android.os.Looper looper) {
            super();
        }
        
        @java.lang.Override
        public void handleMessage(@org.jetbrains.annotations.NotNull
        android.os.Message msg) {
        }
    }
}
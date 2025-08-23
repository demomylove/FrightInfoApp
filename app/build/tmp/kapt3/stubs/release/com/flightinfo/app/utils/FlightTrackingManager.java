package com.flightinfo.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \b2\u00020\u0001:\u0001\bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\t"}, d2 = {"Lcom/flightinfo/app/utils/FlightTrackingManager;", "", "()V", "startTrackingService", "", "context", "Landroid/content/Context;", "stopTrackingService", "Companion", "app_release"})
public final class FlightTrackingManager {
    @kotlin.jvm.Volatile
    @org.jetbrains.annotations.Nullable
    private static volatile com.flightinfo.app.utils.FlightTrackingManager INSTANCE;
    @org.jetbrains.annotations.NotNull
    public static final com.flightinfo.app.utils.FlightTrackingManager.Companion Companion = null;
    
    private FlightTrackingManager() {
        super();
    }
    
    public final void startTrackingService(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    public final void stopTrackingService(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/flightinfo/app/utils/FlightTrackingManager$Companion;", "", "()V", "INSTANCE", "Lcom/flightinfo/app/utils/FlightTrackingManager;", "getInstance", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.flightinfo.app.utils.FlightTrackingManager getInstance() {
            return null;
        }
    }
}
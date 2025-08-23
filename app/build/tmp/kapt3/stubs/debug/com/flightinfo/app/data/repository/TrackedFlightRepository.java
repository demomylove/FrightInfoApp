package com.flightinfo.app.data.repository;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u0012\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00100\u000fJ\u001b\u0010\u0011\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u0019\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ!\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\fH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0017"}, d2 = {"Lcom/flightinfo/app/data/repository/TrackedFlightRepository;", "", "trackedFlightDao", "Lcom/flightinfo/app/data/dao/TrackedFlightDao;", "(Lcom/flightinfo/app/data/dao/TrackedFlightDao;)V", "deleteTrackedFlight", "", "trackedFlight", "Lcom/flightinfo/app/data/model/TrackedFlight;", "(Lcom/flightinfo/app/data/model/TrackedFlight;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTrackedFlightById", "flightId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTrackedFlights", "Lkotlinx/coroutines/flow/Flow;", "", "getTrackedFlightById", "insertTrackedFlight", "updateTrackedFlight", "updateTrackedFlightStatus", "newStatus", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class TrackedFlightRepository {
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.dao.TrackedFlightDao trackedFlightDao = null;
    
    @javax.inject.Inject
    public TrackedFlightRepository(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.dao.TrackedFlightDao trackedFlightDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.flightinfo.app.data.model.TrackedFlight>> getAllTrackedFlights() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getTrackedFlightById(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.flightinfo.app.data.model.TrackedFlight> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object insertTrackedFlight(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.TrackedFlight trackedFlight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateTrackedFlight(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.TrackedFlight trackedFlight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object deleteTrackedFlight(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.TrackedFlight trackedFlight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object deleteTrackedFlightById(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateTrackedFlightStatus(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    java.lang.String newStatus, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}
package com.flightinfo.app.data.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH\'J\u001b\u0010\u000e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0011"}, d2 = {"Lcom/flightinfo/app/data/dao/TrackedFlightDao;", "", "deleteTrackedFlight", "", "trackedFlight", "Lcom/flightinfo/app/data/model/TrackedFlight;", "(Lcom/flightinfo/app/data/model/TrackedFlight;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTrackedFlightById", "flightId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTrackedFlights", "Lkotlinx/coroutines/flow/Flow;", "", "getTrackedFlightById", "insertTrackedFlight", "updateTrackedFlight", "app_debug"})
@androidx.room.Dao
public abstract interface TrackedFlightDao {
    
    @androidx.room.Query(value = "SELECT * FROM tracked_flights")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.flightinfo.app.data.model.TrackedFlight>> getAllTrackedFlights();
    
    @androidx.room.Query(value = "SELECT * FROM tracked_flights WHERE flightId = :flightId LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getTrackedFlightById(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.flightinfo.app.data.model.TrackedFlight> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertTrackedFlight(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.TrackedFlight trackedFlight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateTrackedFlight(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.TrackedFlight trackedFlight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteTrackedFlight(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.TrackedFlight trackedFlight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM tracked_flights WHERE flightId = :flightId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteTrackedFlightById(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
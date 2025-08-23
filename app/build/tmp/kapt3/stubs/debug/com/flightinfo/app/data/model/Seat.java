package com.flightinfo.app.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0018\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0005H\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\tH\u00c6\u0003J\t\u0010\"\u001a\u00020\u000bH\u00c6\u0003J\t\u0010#\u001a\u00020\rH\u00c6\u0003JO\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u00c6\u0001J\u0013\u0010%\u001a\u00020&2\b\u0010\'\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010(\u001a\u00020\u0005H\u00d6\u0001J\t\u0010)\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c\u00a8\u0006*"}, d2 = {"Lcom/flightinfo/app/data/model/Seat;", "", "id", "", "row", "", "column", "seatNumber", "status", "Lcom/flightinfo/app/data/model/SeatStatus;", "seatClass", "Lcom/flightinfo/app/data/model/SeatClass;", "price", "", "(Ljava/lang/String;IILjava/lang/String;Lcom/flightinfo/app/data/model/SeatStatus;Lcom/flightinfo/app/data/model/SeatClass;D)V", "getColumn", "()I", "getId", "()Ljava/lang/String;", "getPrice", "()D", "getRow", "getSeatClass", "()Lcom/flightinfo/app/data/model/SeatClass;", "getSeatNumber", "getStatus", "()Lcom/flightinfo/app/data/model/SeatStatus;", "setStatus", "(Lcom/flightinfo/app/data/model/SeatStatus;)V", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class Seat {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String id = null;
    private final int row = 0;
    private final int column = 0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String seatNumber = null;
    @org.jetbrains.annotations.NotNull
    private com.flightinfo.app.data.model.SeatStatus status;
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.model.SeatClass seatClass = null;
    private final double price = 0.0;
    
    public Seat(@org.jetbrains.annotations.NotNull
    java.lang.String id, int row, int column, @org.jetbrains.annotations.NotNull
    java.lang.String seatNumber, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.SeatStatus status, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.SeatClass seatClass, double price) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getId() {
        return null;
    }
    
    public final int getRow() {
        return 0;
    }
    
    public final int getColumn() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSeatNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.SeatStatus getStatus() {
        return null;
    }
    
    public final void setStatus(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.SeatStatus p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.SeatClass getSeatClass() {
        return null;
    }
    
    public final double getPrice() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.SeatStatus component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.SeatClass component6() {
        return null;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.Seat copy(@org.jetbrains.annotations.NotNull
    java.lang.String id, int row, int column, @org.jetbrains.annotations.NotNull
    java.lang.String seatNumber, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.SeatStatus status, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.SeatClass seatClass, double price) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}
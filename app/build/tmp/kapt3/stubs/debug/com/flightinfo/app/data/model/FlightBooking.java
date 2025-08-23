package com.flightinfo.app.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B_\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u000fH\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u000bH\u00c6\u0003J\t\u0010(\u001a\u00020\rH\u00c6\u0003Jm\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u00c6\u0001J\u0013\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010-\u001a\u00020.H\u00d6\u0001J\t\u0010/\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0014R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0014R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0014\u00a8\u00060"}, d2 = {"Lcom/flightinfo/app/data/model/FlightBooking;", "", "flightId", "", "passengerName", "passengerEmail", "passengerPhone", "seat", "passengerId", "specialRequests", "baggageInfo", "Lcom/flightinfo/app/data/model/BaggageInfo;", "mealPreference", "Lcom/flightinfo/app/data/model/MealPreference;", "insuranceOption", "Lcom/flightinfo/app/data/model/InsuranceOption;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/flightinfo/app/data/model/BaggageInfo;Lcom/flightinfo/app/data/model/MealPreference;Lcom/flightinfo/app/data/model/InsuranceOption;)V", "getBaggageInfo", "()Lcom/flightinfo/app/data/model/BaggageInfo;", "getFlightId", "()Ljava/lang/String;", "getInsuranceOption", "()Lcom/flightinfo/app/data/model/InsuranceOption;", "getMealPreference", "()Lcom/flightinfo/app/data/model/MealPreference;", "getPassengerEmail", "getPassengerId", "getPassengerName", "getPassengerPhone", "getSeat", "getSpecialRequests", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class FlightBooking {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String flightId = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String passengerName = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String passengerEmail = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String passengerPhone = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String seat = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String passengerId = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String specialRequests = null;
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.model.BaggageInfo baggageInfo = null;
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.model.MealPreference mealPreference = null;
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.model.InsuranceOption insuranceOption = null;
    
    public FlightBooking(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    java.lang.String passengerName, @org.jetbrains.annotations.NotNull
    java.lang.String passengerEmail, @org.jetbrains.annotations.NotNull
    java.lang.String passengerPhone, @org.jetbrains.annotations.NotNull
    java.lang.String seat, @org.jetbrains.annotations.NotNull
    java.lang.String passengerId, @org.jetbrains.annotations.NotNull
    java.lang.String specialRequests, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.BaggageInfo baggageInfo, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.MealPreference mealPreference, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.InsuranceOption insuranceOption) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFlightId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPassengerName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPassengerEmail() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPassengerPhone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSeat() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPassengerId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSpecialRequests() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.BaggageInfo getBaggageInfo() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.MealPreference getMealPreference() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.InsuranceOption getInsuranceOption() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.InsuranceOption component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.BaggageInfo component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.MealPreference component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.FlightBooking copy(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    java.lang.String passengerName, @org.jetbrains.annotations.NotNull
    java.lang.String passengerEmail, @org.jetbrains.annotations.NotNull
    java.lang.String passengerPhone, @org.jetbrains.annotations.NotNull
    java.lang.String seat, @org.jetbrains.annotations.NotNull
    java.lang.String passengerId, @org.jetbrains.annotations.NotNull
    java.lang.String specialRequests, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.BaggageInfo baggageInfo, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.MealPreference mealPreference, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.InsuranceOption insuranceOption) {
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
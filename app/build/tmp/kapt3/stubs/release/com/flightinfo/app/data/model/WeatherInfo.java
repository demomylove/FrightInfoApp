package com.flightinfo.app.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BS\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\u0005\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u00a2\u0006\u0002\u0010\u0010J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0005H\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\bH\u00c6\u0003J\t\u0010\"\u001a\u00020\u0005H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0005H\u00c6\u0003J\t\u0010%\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00c6\u0003Ji\u0010\'\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u00052\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00c6\u0001J\u0013\u0010(\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010+\u001a\u00020\bH\u00d6\u0001J\t\u0010,\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0012R\u0016\u0010\f\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0016\u0010\u000b\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u0016\u0010\n\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0012R\u0016\u0010\t\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019\u00a8\u0006-"}, d2 = {"Lcom/flightinfo/app/data/model/WeatherInfo;", "", "location", "", "temperature", "", "condition", "humidity", "", "windSpeed", "windDirection", "visibility", "pressure", "forecast", "", "Lcom/flightinfo/app/data/model/DailyForecast;", "(Ljava/lang/String;DLjava/lang/String;IDLjava/lang/String;DDLjava/util/List;)V", "getCondition", "()Ljava/lang/String;", "getForecast", "()Ljava/util/List;", "getHumidity", "()I", "getLocation", "getPressure", "()D", "getTemperature", "getVisibility", "getWindDirection", "getWindSpeed", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "app_release"})
public final class WeatherInfo {
    @com.google.gson.annotations.SerializedName(value = "location")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String location = null;
    @com.google.gson.annotations.SerializedName(value = "temperature")
    private final double temperature = 0.0;
    @com.google.gson.annotations.SerializedName(value = "condition")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String condition = null;
    @com.google.gson.annotations.SerializedName(value = "humidity")
    private final int humidity = 0;
    @com.google.gson.annotations.SerializedName(value = "wind_speed")
    private final double windSpeed = 0.0;
    @com.google.gson.annotations.SerializedName(value = "wind_direction")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String windDirection = null;
    @com.google.gson.annotations.SerializedName(value = "visibility")
    private final double visibility = 0.0;
    @com.google.gson.annotations.SerializedName(value = "pressure")
    private final double pressure = 0.0;
    @com.google.gson.annotations.SerializedName(value = "forecast")
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.flightinfo.app.data.model.DailyForecast> forecast = null;
    
    public WeatherInfo(@org.jetbrains.annotations.NotNull
    java.lang.String location, double temperature, @org.jetbrains.annotations.NotNull
    java.lang.String condition, int humidity, double windSpeed, @org.jetbrains.annotations.NotNull
    java.lang.String windDirection, double visibility, double pressure, @org.jetbrains.annotations.NotNull
    java.util.List<com.flightinfo.app.data.model.DailyForecast> forecast) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLocation() {
        return null;
    }
    
    public final double getTemperature() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCondition() {
        return null;
    }
    
    public final int getHumidity() {
        return 0;
    }
    
    public final double getWindSpeed() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getWindDirection() {
        return null;
    }
    
    public final double getVisibility() {
        return 0.0;
    }
    
    public final double getPressure() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.flightinfo.app.data.model.DailyForecast> getForecast() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component6() {
        return null;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double component8() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.flightinfo.app.data.model.DailyForecast> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.flightinfo.app.data.model.WeatherInfo copy(@org.jetbrains.annotations.NotNull
    java.lang.String location, double temperature, @org.jetbrains.annotations.NotNull
    java.lang.String condition, int humidity, double windSpeed, @org.jetbrains.annotations.NotNull
    java.lang.String windDirection, double visibility, double pressure, @org.jetbrains.annotations.NotNull
    java.util.List<com.flightinfo.app.data.model.DailyForecast> forecast) {
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
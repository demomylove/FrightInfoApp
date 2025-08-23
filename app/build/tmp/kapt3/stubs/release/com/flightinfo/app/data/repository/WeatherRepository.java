package com.flightinfo.app.data.repository;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\t\u001a\u00020\nJ2\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u00062\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\nJ$\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/flightinfo/app/data/repository/WeatherRepository;", "", "apiService", "Lcom/flightinfo/app/data/api/FlightApiService;", "(Lcom/flightinfo/app/data/api/FlightApiService;)V", "getCurrentWeather", "Lkotlinx/coroutines/flow/Flow;", "Lcom/flightinfo/app/utils/Resource;", "Lcom/flightinfo/app/data/model/WeatherResponse;", "location", "", "getFlightWeather", "Lcom/flightinfo/app/data/model/FlightWeatherInfo;", "flightNumber", "departureAirport", "arrivalAirport", "date", "getWeatherForecast", "days", "", "app_release"})
public final class WeatherRepository {
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.api.FlightApiService apiService = null;
    
    @javax.inject.Inject
    public WeatherRepository(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.api.FlightApiService apiService) {
        super();
    }
    
    /**
     * 获取指定地点的当前天气信息。
     * @param location 地点名称
     * @return 返回一个包含天气信息的 Flow。
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.WeatherResponse>> getCurrentWeather(@org.jetbrains.annotations.NotNull
    java.lang.String location) {
        return null;
    }
    
    /**
     * 获取指定地点的天气预报。
     * @param location 地点名称
     * @param days 预报天数
     * @return 返回一个包含天气预报信息的 Flow。
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.WeatherResponse>> getWeatherForecast(@org.jetbrains.annotations.NotNull
    java.lang.String location, int days) {
        return null;
    }
    
    /**
     * 获取航班的天气影响信息。
     * @param flightNumber 航班号
     * @param departureAirport 出发机场
     * @param arrivalAirport 到达机场
     * @param date 日期
     * @return 返回一个包含航班天气信息的 Flow。
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightWeatherInfo>> getFlightWeather(@org.jetbrains.annotations.NotNull
    java.lang.String flightNumber, @org.jetbrains.annotations.NotNull
    java.lang.String departureAirport, @org.jetbrains.annotations.NotNull
    java.lang.String arrivalAirport, @org.jetbrains.annotations.NotNull
    java.lang.String date) {
        return null;
    }
}
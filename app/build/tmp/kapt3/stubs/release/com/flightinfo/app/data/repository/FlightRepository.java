package com.flightinfo.app.data.repository;

@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\"\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00070\u00062\u0006\u0010\u000f\u001a\u00020\nJ\u0012\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00070\u0006J\u001a\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u00062\u0006\u0010\u0014\u001a\u00020\nJB\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00070\u00062\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/flightinfo/app/data/repository/FlightRepository;", "", "apiService", "Lcom/flightinfo/app/data/api/FlightApiService;", "(Lcom/flightinfo/app/data/api/FlightApiService;)V", "bookFlight", "Lkotlinx/coroutines/flow/Flow;", "Lcom/flightinfo/app/utils/Resource;", "", "flightId", "", "bookingInfo", "Lcom/flightinfo/app/data/model/FlightBooking;", "getFlightDetails", "Lcom/flightinfo/app/data/model/FlightInfo;", "flightNumber", "getRealtimeFlights", "Lcom/flightinfo/app/data/model/FlightSearchResponse;", "getTravelSuggestions", "Lcom/flightinfo/app/data/model/TravelSuggestionResponse;", "destination", "searchFlights", "departureAirport", "arrivalAirport", "date", "app_release"})
public final class FlightRepository {
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.api.FlightApiService apiService = null;
    
    @javax.inject.Inject
    public FlightRepository(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.api.FlightApiService apiService) {
        super();
    }
    
    /**
     * 根据航班号、出发机场、到达机场和日期搜索航班。
     * @param flightNumber 航班号
     * @param departureAirport 出发机场
     * @param arrivalAirport 到达机场
     * @param date 日期
     * @return 返回一个包含航班搜索结果的 Flow。
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightSearchResponse>> searchFlights(@org.jetbrains.annotations.Nullable
    java.lang.String flightNumber, @org.jetbrains.annotations.Nullable
    java.lang.String departureAirport, @org.jetbrains.annotations.Nullable
    java.lang.String arrivalAirport, @org.jetbrains.annotations.Nullable
    java.lang.String date) {
        return null;
    }
    
    /**
     * 获取实时航班信息。
     * @return 返回一个包含实时航班信息的 Flow。
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightSearchResponse>> getRealtimeFlights() {
        return null;
    }
    
    /**
     * 根据航班号获取航班详细信息。
     * @param flightNumber 航班号
     * @return 返回一个包含航班详细信息的 Flow。
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightInfo>> getFlightDetails(@org.jetbrains.annotations.NotNull
    java.lang.String flightNumber) {
        return null;
    }
    
    /**
     * 根据目的地获取旅行建议。
     * @param destination 目的地
     * @return 返回一个包含旅行建议的 Flow。
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.TravelSuggestionResponse>> getTravelSuggestions(@org.jetbrains.annotations.NotNull
    java.lang.String destination) {
        return null;
    }
    
    /**
     * 预订航班。
     * @param flightId 航班ID
     * @param bookingInfo 预订信息
     * @return 返回一个包含预订结果的 Flow。
     */
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.flightinfo.app.utils.Resource<kotlin.Unit>> bookFlight(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.FlightBooking bookingInfo) {
        return null;
    }
}
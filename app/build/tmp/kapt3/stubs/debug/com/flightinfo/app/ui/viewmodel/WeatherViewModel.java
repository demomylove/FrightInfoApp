package com.flightinfo.app.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0010\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ&\u0010\u0013\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001aJ\u0018\u0010\u0016\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u001f\u001a\u00020 R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001d\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/flightinfo/app/ui/viewmodel/WeatherViewModel;", "Landroidx/lifecycle/ViewModel;", "weatherRepository", "Lcom/flightinfo/app/data/repository/WeatherRepository;", "(Lcom/flightinfo/app/data/repository/WeatherRepository;)V", "_currentWeather", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/flightinfo/app/utils/Resource;", "Lcom/flightinfo/app/data/model/WeatherResponse;", "_flightWeather", "Lcom/flightinfo/app/data/model/FlightWeatherInfo;", "_isLoading", "", "_weatherForecast", "currentWeather", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentWeather", "()Lkotlinx/coroutines/flow/StateFlow;", "flightWeather", "getFlightWeather", "isLoading", "weatherForecast", "getWeatherForecast", "clearWeatherData", "", "location", "", "flightNumber", "departureAirport", "arrivalAirport", "date", "days", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class WeatherViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.repository.WeatherRepository weatherRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.WeatherResponse>> _currentWeather = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.WeatherResponse>> currentWeather = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.WeatherResponse>> _weatherForecast = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.WeatherResponse>> weatherForecast = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightWeatherInfo>> _flightWeather = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightWeatherInfo>> flightWeather = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading = null;
    
    @javax.inject.Inject
    public WeatherViewModel(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.repository.WeatherRepository weatherRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.WeatherResponse>> getCurrentWeather() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.WeatherResponse>> getWeatherForecast() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightWeatherInfo>> getFlightWeather() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading() {
        return null;
    }
    
    /**
     * 获取指定地点的当前天气信息。
     * @param location 地点名称
     */
    public final void getCurrentWeather(@org.jetbrains.annotations.NotNull
    java.lang.String location) {
    }
    
    /**
     * 获取指定地点的天气预报。
     * @param location 地点名称
     * @param days 预报天数
     */
    public final void getWeatherForecast(@org.jetbrains.annotations.NotNull
    java.lang.String location, int days) {
    }
    
    /**
     * 获取航班的天气影响信息。
     * @param flightNumber 航班号
     * @param departureAirport 出发机场
     * @param arrivalAirport 到达机场
     * @param date 日期
     */
    public final void getFlightWeather(@org.jetbrains.annotations.NotNull
    java.lang.String flightNumber, @org.jetbrains.annotations.NotNull
    java.lang.String departureAirport, @org.jetbrains.annotations.NotNull
    java.lang.String arrivalAirport, @org.jetbrains.annotations.NotNull
    java.lang.String date) {
    }
    
    /**
     * 清除天气数据。
     */
    public final void clearWeatherData() {
    }
}
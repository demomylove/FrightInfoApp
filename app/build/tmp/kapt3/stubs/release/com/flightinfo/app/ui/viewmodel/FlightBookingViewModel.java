package com.flightinfo.app.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001!B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J8\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\t2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001fJ\u0010\u0010 \u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u0015R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\r\u00a8\u0006\""}, d2 = {"Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/flightinfo/app/data/repository/FlightRepository;", "(Lcom/flightinfo/app/data/repository/FlightRepository;)V", "_bookingUiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState;", "_totalPrice", "", "bookingUiState", "Lkotlinx/coroutines/flow/StateFlow;", "getBookingUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "totalPrice", "getTotalPrice", "bookFlight", "", "flightId", "", "bookingInfo", "Lcom/flightinfo/app/data/model/FlightBooking;", "calculateTotalPrice", "basePrice", "seat", "Lcom/flightinfo/app/data/model/Seat;", "mealPreference", "Lcom/flightinfo/app/data/model/MealPreference;", "insuranceOption", "Lcom/flightinfo/app/data/model/InsuranceOption;", "baggageInfo", "Lcom/flightinfo/app/data/model/BaggageInfo;", "validateBookingInfo", "BookingUiState", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class FlightBookingViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.repository.FlightRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState> _bookingUiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState> bookingUiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Double> _totalPrice = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Double> totalPrice = null;
    
    @javax.inject.Inject
    public FlightBookingViewModel(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.repository.FlightRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState> getBookingUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Double> getTotalPrice() {
        return null;
    }
    
    public final void bookFlight(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.FlightBooking bookingInfo) {
    }
    
    /**
     * 验证预订信息
     * @param bookingInfo 预订信息
     * @return 验证结果，如果为null表示验证通过，否则返回错误消息
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String validateBookingInfo(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.FlightBooking bookingInfo) {
        return null;
    }
    
    /**
     * 计算总价
     * @param basePrice 基础票价
     * @param seat 座位信息
     * @param mealPreference 餐食选择
     * @param insuranceOption 保险选项
     * @param baggageInfo 行李信息
     */
    public final void calculateTotalPrice(double basePrice, @org.jetbrains.annotations.Nullable
    com.flightinfo.app.data.model.Seat seat, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.MealPreference mealPreference, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.InsuranceOption insuranceOption, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.BaggageInfo baggageInfo) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0003\u0004\u0005\u0006B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0004\u0007\b\t\n\u00a8\u0006\u000b"}, d2 = {"Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState;", "", "()V", "Error", "Idle", "Loading", "Success", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState$Error;", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState$Idle;", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState$Loading;", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState$Success;", "app_release"})
    public static abstract class BookingUiState {
        
        private BookingUiState() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState$Error;", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_release"})
        public static final class Error extends com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState {
            @org.jetbrains.annotations.NotNull
            private final java.lang.String message = null;
            
            public Error(@org.jetbrains.annotations.NotNull
            java.lang.String message) {
            }
            
            @org.jetbrains.annotations.NotNull
            public final java.lang.String getMessage() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState.Error copy(@org.jetbrains.annotations.NotNull
            java.lang.String message) {
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
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState$Idle;", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState;", "()V", "app_release"})
        public static final class Idle extends com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState {
            @org.jetbrains.annotations.NotNull
            public static final com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState.Idle INSTANCE = null;
            
            private Idle() {
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState$Loading;", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState;", "()V", "app_release"})
        public static final class Loading extends com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState {
            @org.jetbrains.annotations.NotNull
            public static final com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState.Loading INSTANCE = null;
            
            private Loading() {
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState$Success;", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel$BookingUiState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_release"})
        public static final class Success extends com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState {
            @org.jetbrains.annotations.NotNull
            private final java.lang.String message = null;
            
            public Success(@org.jetbrains.annotations.NotNull
            java.lang.String message) {
            }
            
            @org.jetbrains.annotations.NotNull
            public final java.lang.String getMessage() {
                return null;
            }
            
            public Success() {
            }
            
            @org.jetbrains.annotations.NotNull
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull
            public final com.flightinfo.app.ui.viewmodel.FlightBookingViewModel.BookingUiState.Success copy(@org.jetbrains.annotations.NotNull
            java.lang.String message) {
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
    }
}
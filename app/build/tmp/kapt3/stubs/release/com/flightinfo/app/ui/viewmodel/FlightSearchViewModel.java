package com.flightinfo.app.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0013\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\u000eJ\u0006\u0010!\u001a\u00020\u001eJ\u000e\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u000eJ\u0006\u0010$\u001a\u00020\u001eJ6\u0010%\u001a\u00020\u001e2\n\b\u0002\u0010&\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010)\u001a\u0004\u0018\u00010\u000eJ\u001e\u0010*\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u000e2\u0006\u0010&\u001a\u00020\u000e2\u0006\u0010+\u001a\u00020\u000eJ\u000e\u0010,\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u000eJ\u000e\u0010-\u001a\u00020\u001e2\u0006\u0010.\u001a\u00020\u000eJ\u0016\u0010/\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u000e2\u0006\u00100\u001a\u00020\u000eR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0014R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0014R\u001d\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u000b0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014\u00a8\u00061"}, d2 = {"Lcom/flightinfo/app/ui/viewmodel/FlightSearchViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/flightinfo/app/data/repository/FlightRepository;", "trackedFlightRepository", "Lcom/flightinfo/app/data/repository/TrackedFlightRepository;", "(Lcom/flightinfo/app/data/repository/FlightRepository;Lcom/flightinfo/app/data/repository/TrackedFlightRepository;)V", "_isLoading", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_realtimeFlights", "Lcom/flightinfo/app/utils/Resource;", "Lcom/flightinfo/app/data/model/FlightSearchResponse;", "_searchQuery", "", "_searchResults", "_travelSuggestions", "Lcom/flightinfo/app/data/model/TravelSuggestionResponse;", "isLoading", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "realtimeFlights", "getRealtimeFlights", "searchQuery", "getSearchQuery", "searchResults", "getSearchResults", "travelSuggestions", "getTravelSuggestions", "clearResults", "", "isFlightTracked", "flightId", "loadRealtimeFlights", "loadTravelSuggestions", "destination", "refreshRealtimeFlights", "searchFlights", "flightNumber", "departureAirport", "arrivalAirport", "date", "trackFlight", "currentStatus", "untrackFlight", "updateSearchQuery", "query", "updateTrackedFlightStatus", "newStatus", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class FlightSearchViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.repository.FlightRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.data.repository.TrackedFlightRepository trackedFlightRepository = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightSearchResponse>> _searchResults = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightSearchResponse>> searchResults = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightSearchResponse>> _realtimeFlights = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightSearchResponse>> realtimeFlights = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.TravelSuggestionResponse>> _travelSuggestions = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.TravelSuggestionResponse>> travelSuggestions = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;
    
    @javax.inject.Inject
    public FlightSearchViewModel(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.repository.FlightRepository repository, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.repository.TrackedFlightRepository trackedFlightRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightSearchResponse>> getSearchResults() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.FlightSearchResponse>> getRealtimeFlights() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.flightinfo.app.utils.Resource<com.flightinfo.app.data.model.TravelSuggestionResponse>> getTravelSuggestions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }
    
    public final void searchFlights(@org.jetbrains.annotations.Nullable
    java.lang.String flightNumber, @org.jetbrains.annotations.Nullable
    java.lang.String departureAirport, @org.jetbrains.annotations.Nullable
    java.lang.String arrivalAirport, @org.jetbrains.annotations.Nullable
    java.lang.String date) {
    }
    
    public final void loadRealtimeFlights() {
    }
    
    public final void updateSearchQuery(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
    }
    
    public final void clearResults() {
    }
    
    public final void refreshRealtimeFlights() {
    }
    
    public final void loadTravelSuggestions(@org.jetbrains.annotations.NotNull
    java.lang.String destination) {
    }
    
    public final void trackFlight(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    java.lang.String flightNumber, @org.jetbrains.annotations.NotNull
    java.lang.String currentStatus) {
    }
    
    public final void untrackFlight(@org.jetbrains.annotations.NotNull
    java.lang.String flightId) {
    }
    
    public final boolean isFlightTracked(@org.jetbrains.annotations.NotNull
    java.lang.String flightId) {
        return false;
    }
    
    public final void updateTrackedFlightStatus(@org.jetbrains.annotations.NotNull
    java.lang.String flightId, @org.jetbrains.annotations.NotNull
    java.lang.String newStatus) {
    }
}
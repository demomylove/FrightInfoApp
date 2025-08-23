package com.flightinfo.app.ui.fragment;

@dagger.hilt.android.AndroidEntryPoint
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0002J$\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010$\u001a\u00020\u001bH\u0016J\u001a\u0010%\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010\'\u001a\u00020\u001bH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012R\u001b\u0010\u0015\u001a\u00020\u00168BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006("}, d2 = {"Lcom/flightinfo/app/ui/fragment/FlightBookingFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/flightinfo/app/databinding/FragmentFlightBookingBinding;", "arrivalAirport", "", "binding", "getBinding", "()Lcom/flightinfo/app/databinding/FragmentFlightBookingBinding;", "departureAirport", "flightDate", "flightNumber", "selectedSeat", "Lcom/flightinfo/app/data/model/Seat;", "viewModel", "Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel;", "getViewModel", "()Lcom/flightinfo/app/ui/viewmodel/FlightBookingViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "weatherViewModel", "Lcom/flightinfo/app/ui/viewmodel/WeatherViewModel;", "getWeatherViewModel", "()Lcom/flightinfo/app/ui/viewmodel/WeatherViewModel;", "weatherViewModel$delegate", "loadFlightWeather", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "setupBaggagePickers", "app_debug"})
public final class FlightBookingFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable
    private com.flightinfo.app.databinding.FragmentFlightBookingBinding _binding;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy weatherViewModel$delegate = null;
    @org.jetbrains.annotations.Nullable
    private com.flightinfo.app.data.model.Seat selectedSeat;
    @org.jetbrains.annotations.Nullable
    private java.lang.String flightNumber;
    @org.jetbrains.annotations.Nullable
    private java.lang.String departureAirport;
    @org.jetbrains.annotations.Nullable
    private java.lang.String arrivalAirport;
    @org.jetbrains.annotations.Nullable
    private java.lang.String flightDate;
    
    public FlightBookingFragment() {
        super();
    }
    
    private final com.flightinfo.app.databinding.FragmentFlightBookingBinding getBinding() {
        return null;
    }
    
    private final com.flightinfo.app.ui.viewmodel.FlightBookingViewModel getViewModel() {
        return null;
    }
    
    private final com.flightinfo.app.ui.viewmodel.WeatherViewModel getWeatherViewModel() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadFlightWeather() {
    }
    
    private final void setupBaggagePickers() {
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
}
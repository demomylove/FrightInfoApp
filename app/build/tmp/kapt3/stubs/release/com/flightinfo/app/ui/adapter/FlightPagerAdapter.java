package com.flightinfo.app.ui.adapter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/flightinfo/app/ui/adapter/FlightPagerAdapter;", "Landroidx/viewpager2/adapter/FragmentStateAdapter;", "fragmentActivity", "Landroidx/fragment/app/FragmentActivity;", "viewModel", "Lcom/flightinfo/app/ui/viewmodel/FlightSearchViewModel;", "(Landroidx/fragment/app/FragmentActivity;Lcom/flightinfo/app/ui/viewmodel/FlightSearchViewModel;)V", "createFragment", "Landroidx/fragment/app/Fragment;", "position", "", "getItemCount", "app_release"})
public final class FlightPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
    @org.jetbrains.annotations.NotNull
    private final com.flightinfo.app.ui.viewmodel.FlightSearchViewModel viewModel = null;
    
    public FlightPagerAdapter(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentActivity fragmentActivity, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.ui.viewmodel.FlightSearchViewModel viewModel) {
        super(null);
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public androidx.fragment.app.Fragment createFragment(int position) {
        return null;
    }
}
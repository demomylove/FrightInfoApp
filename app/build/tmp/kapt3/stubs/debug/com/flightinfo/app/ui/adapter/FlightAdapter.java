package com.flightinfo.app.ui.adapter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u0012\u0012\u0004\u0012\u00020\u0002\u0012\b\u0012\u00060\u0003R\u00020\u00000\u0001:\u0002\u0014\u0015BM\u0012\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u001a\b\u0002\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00060\t\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\f\u001a\u00020\u00062\n\u0010\r\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u001c\u0010\u0010\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000fH\u0016R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00060\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/flightinfo/app/ui/adapter/FlightAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/flightinfo/app/data/model/FlightInfo;", "Lcom/flightinfo/app/ui/adapter/FlightAdapter$FlightViewHolder;", "onFlightClick", "Lkotlin/Function1;", "", "onBookClick", "onTrackClick", "Lkotlin/Function2;", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)V", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "FlightDiffCallback", "FlightViewHolder", "app_debug"})
public final class FlightAdapter extends androidx.recyclerview.widget.ListAdapter<com.flightinfo.app.data.model.FlightInfo, com.flightinfo.app.ui.adapter.FlightAdapter.FlightViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.flightinfo.app.data.model.FlightInfo, kotlin.Unit> onFlightClick = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.flightinfo.app.data.model.FlightInfo, kotlin.Unit> onBookClick = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<com.flightinfo.app.data.model.FlightInfo, java.lang.Boolean, kotlin.Unit> onTrackClick = null;
    
    public FlightAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.flightinfo.app.data.model.FlightInfo, kotlin.Unit> onFlightClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.flightinfo.app.data.model.FlightInfo, kotlin.Unit> onBookClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.flightinfo.app.data.model.FlightInfo, ? super java.lang.Boolean, kotlin.Unit> onTrackClick) {
        super(null);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.flightinfo.app.ui.adapter.FlightAdapter.FlightViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.flightinfo.app.ui.adapter.FlightAdapter.FlightViewHolder holder, int position) {
    }
    
    public FlightAdapter() {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/flightinfo/app/ui/adapter/FlightAdapter$FlightDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/flightinfo/app/data/model/FlightInfo;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    static final class FlightDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.flightinfo.app.data.model.FlightInfo> {
        
        public FlightDiffCallback() {
            super();
        }
        
        @java.lang.Override
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull
        com.flightinfo.app.data.model.FlightInfo oldItem, @org.jetbrains.annotations.NotNull
        com.flightinfo.app.data.model.FlightInfo newItem) {
            return false;
        }
        
        @java.lang.Override
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull
        com.flightinfo.app.data.model.FlightInfo oldItem, @org.jetbrains.annotations.NotNull
        com.flightinfo.app.data.model.FlightInfo newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\nH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/flightinfo/app/ui/adapter/FlightAdapter$FlightViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/flightinfo/app/databinding/ItemFlightBinding;", "(Lcom/flightinfo/app/ui/adapter/FlightAdapter;Lcom/flightinfo/app/databinding/ItemFlightBinding;)V", "bind", "", "flight", "Lcom/flightinfo/app/data/model/FlightInfo;", "formatTime", "", "timeString", "getStatusColor", "", "status", "app_debug"})
    public final class FlightViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.flightinfo.app.databinding.ItemFlightBinding binding = null;
        
        public FlightViewHolder(@org.jetbrains.annotations.NotNull
        com.flightinfo.app.databinding.ItemFlightBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.flightinfo.app.data.model.FlightInfo flight) {
        }
        
        private final java.lang.String formatTime(java.lang.String timeString) {
            return null;
        }
        
        private final int getStatusColor(java.lang.String status) {
            return 0;
        }
    }
}
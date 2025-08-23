package com.flightinfo.app.ui.adapter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B/\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u000b\u001a\u00020\tJ\b\u0010\f\u001a\u0004\u0018\u00010\u0002J\"\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0016\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/flightinfo/app/ui/adapter/SeatAdapter;", "Landroid/widget/ArrayAdapter;", "Lcom/flightinfo/app/data/model/Seat;", "context", "Landroid/content/Context;", "seats", "", "onSeatClickListener", "Lkotlin/Function1;", "", "(Landroid/content/Context;Ljava/util/List;Lkotlin/jvm/functions/Function1;)V", "clearSelection", "getSelectedSeat", "getView", "Landroid/view/View;", "position", "", "convertView", "parent", "Landroid/view/ViewGroup;", "updateSeat", "seatId", "", "status", "Lcom/flightinfo/app/data/model/SeatStatus;", "app_debug"})
public final class SeatAdapter extends android.widget.ArrayAdapter<com.flightinfo.app.data.model.Seat> {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.flightinfo.app.data.model.Seat> seats = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.flightinfo.app.data.model.Seat, kotlin.Unit> onSeatClickListener = null;
    
    public SeatAdapter(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<com.flightinfo.app.data.model.Seat> seats, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.flightinfo.app.data.model.Seat, kotlin.Unit> onSeatClickListener) {
        super(null, 0);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.view.View getView(int position, @org.jetbrains.annotations.Nullable
    android.view.View convertView, @org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent) {
        return null;
    }
    
    public final void updateSeat(@org.jetbrains.annotations.NotNull
    java.lang.String seatId, @org.jetbrains.annotations.NotNull
    com.flightinfo.app.data.model.SeatStatus status) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.flightinfo.app.data.model.Seat getSelectedSeat() {
        return null;
    }
    
    public final void clearSelection() {
    }
}
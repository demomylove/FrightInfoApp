package com.flightinfo.app.ui.dialog;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0018\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u0007H\u0002J\u0012\u0010\u0017\u001a\u00020\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0014R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/flightinfo/app/ui/dialog/SeatSelectionDialog;", "Landroid/app/Dialog;", "context", "Landroid/content/Context;", "onSeatSelectedListener", "Lkotlin/Function1;", "Lcom/flightinfo/app/data/model/Seat;", "", "(Landroid/content/Context;Lkotlin/jvm/functions/Function1;)V", "seatAdapter", "Lcom/flightinfo/app/ui/adapter/SeatAdapter;", "seats", "", "selectedSeat", "getSeatClassName", "", "seatClass", "Lcom/flightinfo/app/data/model/SeatClass;", "handleSeatClick", "seat", "selectedSeatTextView", "Landroid/widget/TextView;", "initializeSeats", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "app_release"})
public final class SeatSelectionDialog extends android.app.Dialog {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.flightinfo.app.data.model.Seat, kotlin.Unit> onSeatSelectedListener = null;
    private com.flightinfo.app.ui.adapter.SeatAdapter seatAdapter;
    private java.util.List<com.flightinfo.app.data.model.Seat> seats;
    @org.jetbrains.annotations.Nullable
    private com.flightinfo.app.data.model.Seat selectedSeat;
    
    public SeatSelectionDialog(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.flightinfo.app.data.model.Seat, kotlin.Unit> onSeatSelectedListener) {
        super(null);
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initializeSeats() {
    }
    
    private final void handleSeatClick(com.flightinfo.app.data.model.Seat seat, android.widget.TextView selectedSeatTextView) {
    }
    
    private final java.lang.String getSeatClassName(com.flightinfo.app.data.model.SeatClass seatClass) {
        return null;
    }
}
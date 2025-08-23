package com.flightinfo.app.databinding;
import com.flightinfo.app.R;
import com.flightinfo.app.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentWeatherBindingImpl extends FragmentWeatherBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.titleTextView, 1);
        sViewsWithIds.put(R.id.locationEditText, 2);
        sViewsWithIds.put(R.id.searchButton, 3);
        sViewsWithIds.put(R.id.progressBar, 4);
        sViewsWithIds.put(R.id.weatherScrollView, 5);
        sViewsWithIds.put(R.id.locationNameTextView, 6);
        sViewsWithIds.put(R.id.temperatureTextView, 7);
        sViewsWithIds.put(R.id.conditionTextView, 8);
        sViewsWithIds.put(R.id.humidityTextView, 9);
        sViewsWithIds.put(R.id.windTextView, 10);
        sViewsWithIds.put(R.id.forecastRecyclerView, 11);
        sViewsWithIds.put(R.id.flightNumberTextView, 12);
        sViewsWithIds.put(R.id.departureWeatherTextView, 13);
        sViewsWithIds.put(R.id.arrivalWeatherTextView, 14);
        sViewsWithIds.put(R.id.delayProbabilityTextView, 15);
        sViewsWithIds.put(R.id.recommendationTextView, 16);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentWeatherBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private FragmentWeatherBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[12]
            , (androidx.recyclerview.widget.RecyclerView) bindings[11]
            , (android.widget.TextView) bindings[9]
            , (android.widget.EditText) bindings[2]
            , (android.widget.TextView) bindings[6]
            , (android.widget.ProgressBar) bindings[4]
            , (android.widget.TextView) bindings[16]
            , (android.widget.Button) bindings[3]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[1]
            , (android.widget.ScrollView) bindings[5]
            , (android.widget.TextView) bindings[10]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.viewModel == variableId) {
            setViewModel((com.flightinfo.app.ui.viewmodel.WeatherViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.flightinfo.app.ui.viewmodel.WeatherViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}
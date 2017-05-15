package com.jivarela.codebarscanner.ui.views;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.jivarela.codebarscanner.R;
import com.jivarela.codebarscanner.adapters.ProductsAdapter;
import com.jivarela.codebarscanner.classes.CaptureActivityAnyOrientation;
import com.jivarela.codebarscanner.managers.ProductsManager;
import com.jivarela.codebarscanner.ui.activities.MainActivity;
import com.squareup.otto.Bus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityView extends ActivityView {
    private Bus mBus;
    private ProductsManager mProductsManager;

    @Bind(R.id.items_list) RecyclerView mRecyclerView;

    public MainActivityView(MainActivity activity, Bus bus, ProductsManager manager) {
        super(activity);

        this.mBus = bus;
        this.mProductsManager = manager;

        ButterKnife.bind(this, activity);

        initialize(activity);
    }

    @OnClick(R.id.export_codes_button)
    public void exportButtonOnClick() {
        mBus.post(new exportButtonOnClickEvent());
    }

    @OnClick(R.id.scan_button)
    public void readCode() {
        MainActivity activity = (MainActivity) getActivity();

        if (activity == null) { return; }

        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public void notifyNewItem(int size) {
        mRecyclerView.getAdapter().notifyItemInserted(size);
        mRecyclerView.smoothScrollToPosition(size);
    }

    private void initialize(final MainActivity activity) {
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new ProductsAdapter(activity, mProductsManager.getProductsList());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    InputMethodManager imm = (InputMethodManager) (activity.getSystemService(Context.INPUT_METHOD_SERVICE));
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
            }
        });
    }

    public static class exportButtonOnClickEvent {}
}

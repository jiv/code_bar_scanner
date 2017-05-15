package com.jivarela.codebarscanner.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import com.jivarela.codebarscanner.R;
import com.jivarela.codebarscanner.managers.ProductsManager;
import com.jivarela.codebarscanner.providers.BusProvider;
import com.jivarela.codebarscanner.ui.models.MainActivityModel;
import com.jivarela.codebarscanner.ui.presenters.MainActivityPresenter;
import com.jivarela.codebarscanner.ui.views.MainActivityView;
import com.squareup.otto.Bus;


public class MainActivity extends BaseActivity {
    private MainActivityPresenter mPresenter;
    private ProductsManager mProductsManager = ProductsManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bus mBus = BusProvider.getInstance();

        this.mPresenter = new MainActivityPresenter(
                new MainActivityModel(mBus, mProductsManager),
                new MainActivityView(this, mBus, mProductsManager));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregister(mPresenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRegistered()) {
            register(mPresenter);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mProductsManager.getProductsList().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(R.string.if_you_exit)
                    .setPositiveButton(getString(R.string.close_dialog_positive_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(getString(R.string.close_dialog_negative_option), null)
                    .show();
        }else{
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        register(mPresenter);
        mPresenter.onActivityResult(requestCode, resultCode, intent);
    }
}

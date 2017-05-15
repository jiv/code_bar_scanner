package com.jivarela.codebarscanner.ui.presenters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jivarela.codebarscanner.R;
import com.jivarela.codebarscanner.ui.activities.MainActivity;
import com.jivarela.codebarscanner.ui.models.MainActivityModel;
import com.jivarela.codebarscanner.ui.views.MainActivityView;
import com.squareup.otto.Subscribe;

public class MainActivityPresenter {
    private MainActivityModel mModel;
    private MainActivityView mView;

    public MainActivityPresenter(MainActivityModel model, MainActivityView view) {
        this.mModel = model;
        this.mView = view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult.getContents() != null) {
            String barcode = scanResult.getContents();

            mModel.addNewCode(barcode);
        } else {
            Log.i("ScanResult", "Empty result");
        }
    }

    @Subscribe
    public void exportButtonOnClick(MainActivityView.exportButtonOnClickEvent e) {
        mModel.exportProductCodes();
    }

    @Subscribe
    public void exportProducts(MainActivityModel.ExportProductsEvent e) {
        final MainActivity activity = (MainActivity) mView.getActivity();

        if (activity == null) { return; }

        new AlertDialog.Builder(activity)
                .setView(activity.getLayoutInflater().inflate(R.layout.export_dialog, null))
                .setPositiveButton(R.string.export, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog d = (Dialog) dialog;
                        EditText separator = (EditText) d.findViewById(R.id.separator);
                        EditText company = (EditText) d.findViewById(R.id.company);
                        if (!company.getText().toString().isEmpty()) {
                            // TODO: 15/05/17 Check for storage permissions
                            mModel.create_file(separator.getText().toString(),
                                    company.getText().toString());
                        } else {
                            Toast.makeText(
                                    activity,
                                    activity.getString(R.string.company_cant_be_blank),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Subscribe
    public void newBarCode(MainActivityModel.NewBarCodeOnSuccessEvent e) {
        mView.notifyNewItem(e.PRODUCTS_LIST_SIZE);
    }

    @Subscribe
    public void productListIsEmpty(MainActivityModel.ProductsListIsEmptyEvent e) {
        MainActivity activity = (MainActivity) mView.getActivity();

        if (activity == null) { return; }

        Toast.makeText(
                activity,
                activity.getString(R.string.no_codes_scanned),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Subscribe
    public void itemsWithoutQuantity(MainActivityModel.ItemsWithoutQuantityEvent e) {
        MainActivity activity = (MainActivity) mView.getActivity();

        if (activity == null) { return; }

        Toast.makeText(
                activity,
                activity.getString(R.string.item_without_quantity),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Subscribe
    public void createFileOnSuccess(MainActivityModel.CreateFileOnSuccessEvent e) {
        MainActivity activity = (MainActivity) mView.getActivity();

        if (activity == null) { return; }

        Toast.makeText(
                activity,
                activity.getString(R.string.file_exported_to) + e.FILE_PATH,
                Toast.LENGTH_LONG)
                .show();
    }
}

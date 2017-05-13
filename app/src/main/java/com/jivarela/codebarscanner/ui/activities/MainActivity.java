package com.jivarela.codebarscanner.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jivarela.codebarscanner.classes.CaptureActivityAnyOrientation;
import com.jivarela.codebarscanner.adapters.ItemAdapter;
import com.jivarela.codebarscanner.classes.Product;
import com.jivarela.codebarscanner.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    ArrayList<Product> items = new ArrayList<>();

    @Bind(R.id.items_list) RecyclerView mRecyclerView;
    @Bind(R.id.scan_button) Button scan_button;
    @Bind(R.id.export_codes_button) Button export_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ItemAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    InputMethodManager imm = (InputMethodManager) (getSystemService(Context.INPUT_METHOD_SERVICE));
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
            }
        });

        scan_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                readCode(v);
            }
        });

        export_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (items_ok()) {
                    export(v);
                } else {
                    showToast(getString(R.string.item_without_quantity));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!items.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(R.string.if_you_exit)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        }else{
            finish();
        }
    }

    public void readCode(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult.getContents() != null) {
            Log.i("SCAN", scanResult.toString());
            String barcode = scanResult.getContents();

            items.add(new Product(barcode));
            mAdapter.notifyItemInserted(items.size() - 1);
            mRecyclerView.smoothScrollToPosition(items.size() - 1);
        } else {
            Log.i("ScanResult", "Empty result");
        }
    }

    public String current_date(){
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        return dateFormat.format(date);
    }

    public void export(View view){
        if (items.isEmpty()){
            showToast(getString(R.string.no_codes_scanned));
        }else {
            new AlertDialog.Builder(this)
                    .setView(getLayoutInflater().inflate(R.layout.export_dialog, null))
                    .setPositiveButton(R.string.export, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Dialog d = (Dialog) dialog;
                            EditText separator = (EditText) d.findViewById(R.id.separator);
                            EditText company = (EditText) d.findViewById(R.id.company);
                            if (!company.getText().toString().isEmpty()) {
                                create_file(separator.getText().toString(), company.getText().toString());
                            } else {
                                showToast(getString(R.string.company_cant_be_blank));
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }

    private boolean items_ok(){
        boolean ok = true;
        int i = 0;
        while (ok && i < items.size()){
            Product p = items.get(i);
            ok = (!p.getCode().isEmpty() && !p.getQuantity().equals(0));
            i++;
        }
        return ok;
    }

    private void create_file(String separator, String company){
        String root = Environment.getExternalStorageDirectory().toString();
        File new_file = new File(root, company + "_" + current_date() + ".txt");

        try {
            FileOutputStream f = new FileOutputStream(new_file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(f));

            for (int i = 0; i < items.size(); i++) {
                Product p = items.get(i);
                bw.write(p.getCode() + separator + p.getQuantity() + "\r\n");
            }

            bw.flush();
            bw.close();
            f.close();
        } catch ( IOException e ){
            e.printStackTrace();
        }

        Toast toast = Toast.makeText(this, getString(R.string.file_exported_to) + new_file.getAbsolutePath(), Toast.LENGTH_LONG);
        toast.show();
    }

    public void showToast(String msj){
        Toast.makeText(this, msj, Toast.LENGTH_SHORT).show();
    }
}
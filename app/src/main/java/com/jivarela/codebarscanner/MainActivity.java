package com.jivarela.codebarscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends Activity {
    ArrayList<Product> items = new ArrayList<>();
    ItemAdapter adapter;

    @Bind(R.id.list) ListView list;
    @Bind(R.id.scan_button) Button scan_button;
    @Bind(R.id.export_codes_button) Button export_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        adapter=new ItemAdapter(this, items);
        list.setAdapter(adapter);

        scan_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                readCode(v);
                ;
            }
        });

        export_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                export(v);;
            }
        });
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
        if (scanResult != null) {
            String barcode = scanResult.getContents();
            items.add(new Product(barcode));
            adapter.notifyDataSetChanged();
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
//        String root = Environment.getExternalStorageDirectory().toString();
//        File new_file = new File(root + "/codigos_" + current_date() + ".txt");



    }
}

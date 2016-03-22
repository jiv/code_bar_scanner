package com.jivarela.codebarscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends Activity {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ItemAdapter(this, items);
        mRecyclerView.setAdapter(mAdapter);

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
            mAdapter.notifyItemInserted(items.size()-1);
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
        String root = Environment.getExternalStorageDirectory().toString();
        File new_file = new File(root, "codigos_" + current_date() + ".txt");

        try {
            FileOutputStream f = new FileOutputStream(new_file);
            PrintWriter pw = new PrintWriter(f);

            for (int i = 0; i < items.size(); i++) {
                Product p = items.get(i);
                pw.println(p.getCode() + ',' + p.getQuantity());
            }

            pw.flush();
            pw.close();
            f.close();
        } catch ( IOException e ){
            e.printStackTrace();
        }

        Toast toast = Toast.makeText(this, "File exported to " + new_file.getAbsolutePath(), Toast.LENGTH_LONG);
        toast.show();
    }
}

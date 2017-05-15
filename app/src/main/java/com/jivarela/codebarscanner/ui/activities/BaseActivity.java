package com.jivarela.codebarscanner.ui.activities;


import android.support.v7.app.AppCompatActivity;

import com.jivarela.codebarscanner.providers.BusProvider;

public class BaseActivity extends AppCompatActivity {
    private boolean isRegistered = false;

    public void register(Object... objects) {
        if (objects != null) {
            for (Object object : objects) {
                if (object != null) {
                    BusProvider.getInstance().register(object);
                    isRegistered = true;
                }
            }
        }
    }

    public void unregister(Object... objects) {
        if (objects != null) {
            for (Object object : objects) {
                if (object != null) {
                    BusProvider.getInstance().unregister(object);
                    isRegistered = false;
                }
            }
        }
    }

    public boolean isRegistered() {
        return isRegistered;
    }
}

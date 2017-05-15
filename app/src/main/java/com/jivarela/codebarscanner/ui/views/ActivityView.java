package com.jivarela.codebarscanner.ui.views;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import java.lang.ref.WeakReference;

public abstract class ActivityView {
    private transient WeakReference<AppCompatActivity> activityRef;

    public ActivityView(AppCompatActivity activity) {
        activityRef = new WeakReference<>(activity);
    }

    @Nullable
    public AppCompatActivity getActivity() {
        return activityRef.get();
    }
}

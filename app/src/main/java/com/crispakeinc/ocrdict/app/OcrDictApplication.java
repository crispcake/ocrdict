package com.crispakeinc.ocrdict.app;

import android.app.Application;
import android.os.StrictMode;

import com.crispakeinc.ocrdict.common.OcrDictUtils;

public class OcrDictApplication extends Application {
    private static boolean activityVisible = false;
    private static Long reqLocTimeOutThreadId = -1l;

    @Override
    public void onCreate() {
        super.onCreate();
//        ActiveAndroid.initialize(this);
        setupStrictMode();
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private void setupStrictMode() {
        if (OcrDictUtils.GetMetaValue(getApplicationContext(), "developer_mode").equals("yes")) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    public static void setActivityVisible(boolean activityVisible) {
        OcrDictApplication.activityVisible = activityVisible;
    }

    public static Long getReqLocTimeOutThreadId() {
        return reqLocTimeOutThreadId;
    }

    public static void setReqLocTimeOutThreadId(Long reqLocTimeOutThreadId) {
        OcrDictApplication.reqLocTimeOutThreadId = reqLocTimeOutThreadId;
    }
}

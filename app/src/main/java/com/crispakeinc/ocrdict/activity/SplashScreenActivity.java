package com.crispakeinc.ocrdict.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.crispakeinc.ocrdict.common.OcrDictConstants;
import com.crispcakeinc.ocrdict.R;

public class SplashScreenActivity extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    private Activity activity = this;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        sharedPref = getSharedPreferences(OcrDictConstants.OCRDICT_SHARED_PREFERRENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                if(sharedPref.getString(OcrDictConstants.LOGGED_IN_TOKEN, null) != null)
                    startActivity(new Intent(activity, HomepageActivity.class));
                else
                    startActivity(new Intent(activity, SignInActivity.class));
                finish();
//            }
//        }, SPLASH_TIME_OUT);
    }
}
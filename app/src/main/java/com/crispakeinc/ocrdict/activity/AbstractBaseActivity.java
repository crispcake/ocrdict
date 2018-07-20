package com.crispakeinc.ocrdict.activity;

import android.content.*;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.crispakeinc.ocrdict.app.OcrDictApplication;
import com.crispakeinc.ocrdict.common.OcrDictConstants;
import com.crispcakeinc.ocrdict.R;

import java.util.Set;

/**
 * A base activity that handles common functionality in the app.
 */
public abstract class AbstractBaseActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    protected SharedPreferences sharedPref;
    protected SharedPreferences.Editor editor;
    protected TextView abTitle;
    protected ActionBar actionBar;
    protected Boolean useCacheInLoadingData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
        getWindow().setBackgroundDrawable(null);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        sharedPref = getSharedPreferences(OcrDictConstants.OCR_DICT_PREFERRENCES, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        try {
            abTitle = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", "android"));
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void unbindDrawables(View view) {
        if (view == null)
            return;
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        OcrDictApplication.activityResumed();
//        LocalBroadcastManager.getInstance(this).registerReceiver(whereskBroadcastReceiver, new IntentFilter(WhereskAndroidConstants.WHERESK_BROADCAST_RECEIVER_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        OcrDictApplication.activityPaused();
        interruptReqLocThread();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(whereskBroadcastReceiver);
    }

    private void interruptReqLocThread() {
        Thread reqLocTimeOutThread = getReqLocTimeOutThread();
        if (reqLocTimeOutThread != null && reqLocTimeOutThread.isAlive()) {
            reqLocTimeOutThread.interrupt();
        }
        OcrDictApplication.setReqLocTimeOutThreadId(-1l);
    }

    public void doNothing(View v) {
    }

    private int getMarginLeftOfGuideBehaviourMemberListFrame(int sizeOfMemberList) {
        int marginLeft = (int) (
                getResources().getDimension(R.dimen.target_small_photo_size_for_marker_in_map_dp) * sizeOfMemberList
                        + getResources().getDimension(R.dimen.map_marker_photo_icon_border_size) * 2 * sizeOfMemberList
                        + getResources().getDimension(R.dimen.target_small_photo_margin_left_in_map_dp) * (sizeOfMemberList - 1)
        );
        int groupMapMemberListWidthPixel = (int) getResources().getDimension(R.dimen.group_map_member_list_width);
        if (marginLeft > groupMapMemberListWidthPixel)
            marginLeft = groupMapMemberListWidthPixel;
        return marginLeft;
    }

    protected void runOperationWhenSwipeRefresh() {
    }

    @Override
    public void onRefresh() {
        setUseCacheInLoadingData(false);
        runOperationWhenSwipeRefresh();
    }

    protected Integer[] getSwipeableChildrenIds() {
        return new Integer[]{android.R.id.list};
    }

    public void setUseCacheInLoadingData(Boolean useCacheInLoadingData) {
        this.useCacheInLoadingData = useCacheInLoadingData;
    }

    public Boolean getUseCacheInLoadingData() {
        return useCacheInLoadingData;
    }

    public Thread getReqLocTimeOutThread() {
        if (OcrDictApplication.getReqLocTimeOutThreadId() == null)
            return null;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        if (threadSet == null)
            return null;
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for (Thread thread : threadArray) {
            if (thread.getId() == OcrDictApplication.getReqLocTimeOutThreadId())
                return thread;
        }
        return null;
    }
}

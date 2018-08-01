package com.crispakeinc.ocrdict.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.crispakeinc.ocrdict.adapter.CustomDrawerAdapter;
import com.crispakeinc.ocrdict.adapter.DrawerItem;
import com.crispakeinc.ocrdict.common.OcrDictConstants;
import com.crispcakeinc.ocrdict.R;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNavigationDrawerActivity extends AbstractBaseActivity implements OnItemClickListener {
    ListView drawerList;
    protected DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    protected List<DrawerItem> drawerItemList;
    protected boolean needLeftNavigationDrawer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(needLeftNavigationDrawer) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(needLeftNavigationDrawer) {
            initAndDisplayLeftDrawer();
            drawerToggle.syncState();
        }
    }

    protected void initAndDisplayLeftDrawer() {
        initializeDrawerItemList();
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList.setDivider(null);
        drawerList.setAdapter(new CustomDrawerAdapter(this, R.layout.custom_drawer_item, drawerItemList));
        drawerList.setOnItemClickListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
        };

        specifyItemSelected();

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        displayNavigationDrawerOnFirstLaunch();
    }

    protected void initializeDrawerItemList() {
        drawerItemList = new ArrayList<DrawerItem>();
        drawerItemList.add(new DrawerItem(true, null, null, null));
        drawerItemList.add(new DrawerItem(false, HomepageActivity.class, getString(R.string.home), R.drawable.home_white));
//        drawerItemList.add(new DrawerItem(false, FollowedQuestionPageActivity.class, getString(R.string.follow), R.drawable.focus_white));
//        drawerItemList.add(new DrawerItem(false, SystemConfigurationActivity.class, getString(R.string.setting), R.drawable.action_bar_settings_white));
    }

    protected void specifyItemSelected() {
        for (int i = 0; i < drawerItemList.size(); i++) {
            if (((Object) this).getClass().equals(drawerItemList.get(i).getActivityClass()))
                drawerList.setItemChecked(i, true);
        }
    }

    private void displayNavigationDrawerOnFirstLaunch() {
        boolean isFirstStart = sharedPref.getBoolean(OcrDictConstants.SHARED_PREFERRENCE_FIRST_LAUNCH_INDICATOR, true);
        if (isFirstStart) {
            final ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
            drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.openDrawer(Gravity.LEFT);
                    final View transparentView = View.inflate(getApplicationContext(), R.layout.transparent_layout, null);
                    transparentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    rootView.addView(transparentView);
                    drawerLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerLayout.closeDrawer(drawerList);
                            editor.putBoolean(OcrDictConstants.SHARED_PREFERRENCE_FIRST_LAUNCH_INDICATOR, false);
                            editor.apply();
                            rootView.removeView(transparentView);
                        }
                    }, 1500);
                }
            }, 500);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(needLeftNavigationDrawer) {
            if (item.getItemId() == android.R.id.home) {
                if (drawerLayout.isDrawerOpen(drawerList)) {
                    drawerLayout.closeDrawer(drawerList);
                } else {
                    drawerLayout.openDrawer(drawerList);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        if(needLeftNavigationDrawer) {
            Intent intent = new Intent(getApplicationContext(), drawerItemList.get(position).getActivityClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }

    protected void animateDrawerItemBackground(Class activityClass) {
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        LinearLayout layout = null;
        for (int i = 0; i < drawerItemList.size(); i++) {
            if (activityClass.equals(drawerItemList.get(i).getActivityClass()))
                layout = (LinearLayout) listView.getChildAt(i).findViewById(R.id.itemContainer);
        }

        final AnimationDrawable drawable = new AnimationDrawable();

        drawable.addFrame(new ColorDrawable(getResources().getColor(R.color.holo_red_light)), 400);
        drawable.addFrame(new ColorDrawable(getResources().getColor(R.color.holo_green_light)), 400);
        drawable.setOneShot(false);

        layout.setBackgroundDrawable(drawable);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawable.start();
            }
        }, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.drawer_layout));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(needLeftNavigationDrawer) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                moveTaskToBack(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }
}

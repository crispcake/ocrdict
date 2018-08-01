package com.crispakeinc.ocrdict.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crispakeinc.ocrdict.activity.AbstractNavigationDrawerActivity;
import com.crispakeinc.ocrdict.common.OcrDictConstants;
import com.crispakeinc.ocrdict.common.OcrDictUtils;
import com.crispcakeinc.ocrdict.R;

import java.io.File;
import java.util.List;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    AbstractNavigationDrawerActivity activity;
	List<DrawerItem> drawerItemList;
	int layoutResID;
    String displayName;
    Bitmap userProfileImageBitmap;
    private SharedPreferences sharedPref;

    public CustomDrawerAdapter(AbstractNavigationDrawerActivity activity, int layoutResourceID, List<DrawerItem> listItems) {
		super(activity, layoutResourceID, listItems);
		this.activity = activity;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;
        this.sharedPref = activity.getSharedPreferences(OcrDictConstants.OCRDICT_SHARED_PREFERRENCES, Context.MODE_PRIVATE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final DrawerItemHolder drawerItemHolder;

		if (view == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
//            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layoutResID, parent, false);
            drawerItemHolder = initDrawerItemHolder(view);
			view.setTag(drawerItemHolder);
		} else {
			drawerItemHolder = (DrawerItemHolder) view.getTag();
		}

		DrawerItem dItem = this.drawerItemList.get(position);

		if (dItem.getIsHeader()) {
            view.setClickable(false);
			drawerItemHolder.headerLayout.setVisibility(LinearLayout.VISIBLE);
            drawerItemHolder.headerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.getDrawerLayout().closeDrawers();
//                    Intent intent = new Intent(activity, UserProfileActivity.class);
//                    SharedPreferences sharedPref = activity.getSharedPreferences(WhereskAndroidConstants.OCRDICT_SHARED_PREFERRENCES, Context.MODE_PRIVATE);
//                    intent.putExtra(WhereskAndroidConstants.KEY_USER_ID, sharedPref.getLong(WhereskAndroidConstants.LOGGED_IN_USER_ID_ON_SERVER, 0));
//                    activity.startActivity(intent);
                }
            });
            drawerItemHolder.itemLayout.setVisibility(LinearLayout.GONE);

            if(displayName == null) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        displayName = sharedPref.getString(OcrDictConstants.SELF_DISPLAY_NAME, null);
                        String photoFilePath = sharedPref.getString(OcrDictConstants.SELF_PHONE_FILE_PATH, null);
                        if (photoFilePath != null) {
                            File photoFile = new File(OcrDictUtils.GetExternalFilePath(activity, photoFilePath));
                            userProfileImageBitmap = OcrDictUtils.GetBitmapFromImageFile(photoFile);
                        } else {
                            userProfileImageBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.redpoint);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        drawerItemHolder.userNameTextView.setText(displayName);
                        //drawerItemHolder.selfDescriptionTextView.setText("Integration Engineer");
                        drawerItemHolder.userIcon.setImageBitmap(userProfileImageBitmap);
                    }
                }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                drawerItemHolder.userNameTextView.setText(displayName);
                //drawerItemHolder.selfDescriptionTextView.setText("Integration Engineer");
                drawerItemHolder.userIcon.setImageBitmap(userProfileImageBitmap);
            }
		} else {
            view.setClickable(false);
			drawerItemHolder.headerLayout.setVisibility(LinearLayout.GONE);
			drawerItemHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
			drawerItemHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
			drawerItemHolder.ItemNameTextView.setText(dItem.getItemName());
        }
		return view;
	}

    protected DrawerItemHolder initDrawerItemHolder(View view) {
        DrawerItemHolder drawerItemHolder = new DrawerItemHolder();
        drawerItemHolder.headerLayout = view.findViewById(R.id.headerLayout);
        drawerItemHolder.userNameTextView = (TextView) view.findViewById(R.id.user_name_text_view);
        drawerItemHolder.selfDescriptionTextView = (TextView) view.findViewById(R.id.self_description_text_view);
        drawerItemHolder.selfDescriptionContainer = view.findViewById(R.id.self_description_container);
        drawerItemHolder.userIcon = (ImageView) view.findViewById(R.id.user_icon);

        drawerItemHolder.itemLayout = view.findViewById(R.id.itemLayout);
        drawerItemHolder.ItemNameTextView = (TextView) view.findViewById(R.id.drawer_itemName);
        drawerItemHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

        return drawerItemHolder;
    }

    public static class DrawerItemHolder {
		public TextView ItemNameTextView, userNameTextView, selfDescriptionTextView;
        public ImageView icon, userIcon;
        public View headerLayout, itemLayout, selfDescriptionContainer;
	}
}

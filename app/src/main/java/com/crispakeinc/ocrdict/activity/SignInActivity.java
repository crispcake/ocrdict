package com.crispakeinc.ocrdict.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crispakeinc.ocrdict.common.OcrDictConstants;
import com.crispakeinc.ocrdict.common.OcrDictUtils;
import com.crispcakeinc.ocrdict.R;
import com.facebook.*;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import info.guardianproject.netcipher.NetCipher;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ProgressDialog mProgressDialog;
    private Activity activity = this;
    private SharedPreferences sharedPreferences;

    String userPhotoUrl = null;
    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(OcrDictConstants.OCRDICT_SHARED_PREFERRENCES, Context.MODE_PRIVATE);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.sign_in_page);

        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        customizeFacebookButtonStyle(facebookLoginButton);

        facebookLoginButton.setReadPermissions("public_profile", "user_friends", "email", "user_birthday");
        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(final JSONObject object, GraphResponse response) {
                                        try {
                                            final String facebookId = object.getString("id");
                                            final String displayName = object.getString("name");
                                            userPhotoUrl = "https://graph.facebook.com/" + facebookId + "/picture?type=small";
                                            final String email = object.getString("email");


                                            new AsyncTask<Void, Void, Void>() {
                                                boolean hasError = false;

                                                @Override
                                                protected Void doInBackground(Void... params) {
                                                    try {
                                                        String photoUrlLarge = userPhotoUrl.replace("small", "large");

                                                        HttpsURLConnection connection = NetCipher.getHttpsURLConnection(photoUrlLarge);
                                                        String fileNameWidthExtension = "user_myself" + OcrDictConstants.JPG_FILE_SUFFIX;
                                                        OcrDictUtils.ConvertInputStreamToFile(connection.getInputStream(), OcrDictUtils.GetExternalFilePath(activity, fileNameWidthExtension), null);
                                                        final SharedPreferences.Editor edit = sharedPreferences.edit();
                                                        edit.putString(OcrDictConstants.LOGGED_IN_TOKEN, facebookId);
                                                        edit.putString(OcrDictConstants.SELF_PHONE_FILE_PATH, fileNameWidthExtension);
                                                        edit.putString(OcrDictConstants.SELF_DISPLAY_NAME, displayName);
                                                        edit.putString(OcrDictConstants.SELF_EMAIL, email);
                                                        edit.commit();
                                                    } catch (Exception e) {
                                                        throw new RuntimeException("Can not get user photo!");
                                                    }
                                                    return null;
                                                }

                                                @Override
                                                protected void onPostExecute(Void aVoid) {
                                                    hideProgressDialog();
                                                    if (hasError) {
                                                        Toast.makeText(activity, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        startActivity(new Intent(activity, HomepageActivity.class));
                                                        finish();
                                                    }
                                                }
                                            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                                        } catch (JSONException ex) {
                                            Log.e(OcrDictConstants.LOG_OCR_DICT_TAG, "Error in facebook login process!", ex);
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                }
        );

        TextView termsOfUseTextView = (TextView) findViewById(R.id.text_view_terms_of_use);
        termsOfUseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_for_terms_of_use);
                TextView termsOfUseTextView = (TextView) dialog.findViewById(R.id.user_terms_of_use);
                Button confirmButton = (Button) dialog.findViewById(R.id.confirm_button_in_terms_of_use);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });
    }

    private void customizeFacebookButtonStyle(LoginButton facebookLoginButton) {
        float fbIconScale = 1.45F;
        Drawable facebookDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            facebookDrawable = getDrawable(R.drawable.com_facebook_button_icon);
        else
            facebookDrawable = getResources().getDrawable(R.drawable.com_facebook_button_icon);
        facebookDrawable.setBounds(0, 0, (int) (facebookDrawable.getIntrinsicWidth() * fbIconScale), (int) (facebookDrawable.getIntrinsicHeight() * fbIconScale));
        facebookLoginButton.setCompoundDrawables(facebookDrawable, null, null, null);
        facebookLoginButton.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        facebookLoginButton.setPadding(
                getResources().getDimensionPixelSize(R.dimen.fb_margin_override_lr),
                getResources().getDimensionPixelSize(R.dimen.fb_margin_override_top),
                0,
                getResources().getDimensionPixelSize(R.dimen.fb_margin_override_bottom));
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.auth_google_in_progress));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

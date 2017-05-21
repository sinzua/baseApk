package com.ty.followboom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.ContactsContract.Profile;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.forwardwin.base.widgets.JsonSerializer;
import com.nativex.network.volley.DefaultRetryPolicy;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.ParseHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.models.InstagramApi;
import com.ty.followboom.models.LikeServerInstagram;
import com.ty.followboom.models.TrackActionManager;
import com.ty.followboom.models.UserInfoManager;
import com.ty.followboom.okhttp.HttpSingleton;
import com.ty.followboom.okhttp.JsonHelper;
import com.ty.http.RequestCallback;
import com.ty.http.responses.LoginResponse;
import com.ty.instagramapi.InstagramService;
import com.ty.instaview.R;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String TAG = "LoginActivity";
    private RequestCallback<Response> loginCallBack = new RequestCallback<Response>() {
        public void onResponse(Response response) {
            Headers headers = response.headers();
            StringBuilder cookies = new StringBuilder();
            for (String value : headers.values("Set-Cookie")) {
                cookies.append(value.split(";")[0]).append(";");
            }
            InstagramService.getInstance().getUserInfo().setCookie(cookies.toString());
            try {
                final LoginResponse t = (LoginResponse) JsonHelper.gson.fromJson(response.body().string(), LoginResponse.class);
                if (t.isSuccessful()) {
                    InstagramService.getInstance().getUserInfo().setAvatarUrl(t.getLogged_in_user().getProfile_pic_url());
                    InstagramService.getInstance().getUserInfo().setUserid(Long.valueOf(Long.parseLong(t.getLogged_in_user().getPk())));
                    LikeServerInstagram.getSingleton().LoginToServer(LoginActivity.this, LoginActivity.this.mLoginCallBack);
                    return;
                }
                HttpSingleton.getMainThreadHandler().post(new Runnable() {
                    public void run() {
                        LoginActivity.this.showProgress(false);
                        Toast.makeText(LoginActivity.this, t.getMessage(), 0).show();
                    }
                });
            } catch (Exception e) {
                HttpSingleton.getMainThreadHandler().post(new Runnable() {
                    public void run() {
                        LoginActivity.this.showProgress(false);
                    }
                });
                e.printStackTrace();
            }
        }

        public void onFailure(Exception e) {
            HttpSingleton.getMainThreadHandler().post(new Runnable() {
                public void run() {
                    LoginActivity.this.showProgress(false);
                    Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.login_failed), 0).show();
                }
            });
        }
    };
    private AutoCompleteTextView mEmailView;
    private com.ty.followboom.okhttp.RequestCallback<com.ty.followboom.okhttp.responses.LoginResponse> mLoginCallBack = new com.ty.followboom.okhttp.RequestCallback<com.ty.followboom.okhttp.responses.LoginResponse>() {
        public void onResponse(com.ty.followboom.okhttp.responses.LoginResponse loginResponse) {
            Log.e(LoginActivity.TAG, "LoginToServer : " + loginResponse.getStatus().getStatusMsg());
            LoginActivity.this.showProgress(false);
            AppConfigHelper.saveAppConfig(LoginActivity.this, JsonSerializer.getInstance().serialize(loginResponse.getData()));
            UserInfoManager.getSingleton().saveUserInfo(LoginActivity.this);
            TrackActionManager.getSingleton().init(LoginActivity.this);
            ParseHelper.signup(LoginActivity.this);
            VLTools.gotoMainActivity(LoginActivity.this);
        }

        public void onFailure(Exception e) {
            Toast.makeText(LoginActivity.this, e.toString(), 1).show();
        }
    };
    private View mLoginFormView;
    private EditText mPasswordView;
    private View mProgressView;

    private interface ProfileQuery {
        public static final int ADDRESS = 0;
        public static final int IS_PRIMARY = 1;
        public static final String[] PROJECTION = new String[]{"data1", "is_primary"};
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        this.mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        if (UserInfoManager.getSingleton().getUserInfo(this) != null) {
            this.mEmailView.setText(UserInfoManager.getSingleton().getUserInfo(this).getUsername());
        }
        populateAutoComplete();
        this.mPasswordView = (EditText) findViewById(R.id.password);
        this.mPasswordView.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id != R.id.login && id != 0) {
                    return false;
                }
                LoginActivity.this.attemptLogin();
                return true;
            }
        });
        ((Button) findViewById(R.id.email_sign_in_button)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.attemptLogin();
            }
        });
        this.mLoginFormView = findViewById(R.id.login_form);
        this.mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (mayRequestContacts()) {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    private boolean mayRequestContacts() {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            return true;
        }
        if (shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
            Snackbar.make(this.mEmailView, (int) R.string.permission_rationale, -2).setAction(17039370, new OnClickListener() {
                @TargetApi(23)
                public void onClick(View v) {
                    LoginActivity.this.requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
                }
            });
        } else {
            requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && grantResults.length == 1 && grantResults[0] == 0) {
            populateAutoComplete();
        }
    }

    private void attemptLogin() {
        this.mEmailView.setError(null);
        this.mPasswordView.setError(null);
        String username = this.mEmailView.getText().toString();
        String password = this.mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!(TextUtils.isEmpty(password) || isPasswordValid(password))) {
            this.mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = this.mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            this.mEmailView.setError(getString(R.string.error_field_required));
            focusView = this.mEmailView;
            cancel = true;
        } else if (!isEmailValid(username)) {
            this.mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = this.mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return;
        }
        showProgress(true);
        InstagramApi.getSingleton().login(username, password, this.loginCallBack);
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email);
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password);
    }

    @TargetApi(13)
    private void showProgress(final boolean show) {
        float f = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        int i = 8;
        int i2 = 0;
        int i3;
        if (VERSION.SDK_INT >= 13) {
            int shortAnimTime = getResources().getInteger(17694720);
            View view = this.mLoginFormView;
            if (show) {
                i3 = 8;
            } else {
                i3 = 0;
            }
            view.setVisibility(i3);
            this.mLoginFormView.animate().setDuration((long) shortAnimTime).alpha(show ? 0.0f : DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    LoginActivity.this.mLoginFormView.setVisibility(show ? 8 : 0);
                }
            });
            View view2 = this.mProgressView;
            if (!show) {
                i2 = 8;
            }
            view2.setVisibility(i2);
            ViewPropertyAnimator duration = this.mProgressView.animate().setDuration((long) shortAnimTime);
            if (!show) {
                f = 0.0f;
            }
            duration.alpha(f).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    LoginActivity.this.mProgressView.setVisibility(show ? 0 : 8);
                }
            });
            return;
        }
        View view3 = this.mProgressView;
        if (show) {
            i3 = 0;
        } else {
            i3 = 8;
        }
        view3.setVisibility(i3);
        view2 = this.mLoginFormView;
        if (!show) {
            i = 0;
        }
        view2.setVisibility(i);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, Uri.withAppendedPath(Profile.CONTENT_URI, "data"), ProfileQuery.PROJECTION, "mimetype = ?", new String[]{"vnd.android.cursor.item/email_v2"}, "is_primary DESC");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<String> emails = new ArrayList();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                emails.add(cursor.getString(0));
                cursor.moveToNext();
            }
            addEmailsToAutoComplete(emails);
        } catch (Exception e) {
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        this.mEmailView.setAdapter(new ArrayAdapter(this, 17367050, emailAddressCollection));
    }
}

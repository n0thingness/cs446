package io.chatr.chatr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.chatr.chatr.data.model.LoginRequest;
import io.chatr.chatr.data.model.User;
import io.chatr.chatr.data.remote.ServiceGenerator;
import io.chatr.chatr.data.remote.chatrAPI;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Integer.parseInt;

public class UpdateUserProfileActivity extends AppCompatActivity implements AsyncResponse<String>  {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mUpdateFormView;
    private AutoCompleteTextView mName;
    private AutoCompleteTextView mSurname;
    private AutoCompleteTextView mAge;
    private AutoCompleteTextView mGender;
    private AutoCompleteTextView mLocation;
    private AutoCompleteTextView mOccupation;
    private AutoCompleteTextView mInterests;
    private Button mUpdateSubmitButton;
    private ProfileUpdateTask mTask = null;
    private SharedPreferences sharedPref;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
//    private static final int REQUEST_READ_CONTACTS = 0;
//
//
//    private static final String[] DUMMY_CREDENTIALS = new String[]{
//            "foo@example.com:hello", "bar@example.com:world"
//    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UpdateUserProfileActivity.UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
////        populateAutoComplete();
//
//        mPasswordView = (EditText) findViewById(R.id.update_password);
//
//        mPasswordConfirmView = (EditText) findViewById(R.id.update_password_confirm);
//        mPasswordConfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
////                  attemptUpdate();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        mUpdateFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
//
        mUpdateSubmitButton = (Button) findViewById(R.id.update_submit_button);
        mUpdateSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptUpdate();
//                goBackToMainActivity();

            }
        });

        mUpdateFormView = findViewById(R.id.update_profile_scroll_view);
        mProgressView = findViewById(R.id.update_profile_progress);

        mName = findViewById(R.id.update_first_name);
        mSurname = findViewById(R.id.update_last_name);
        mAge = findViewById(R.id.update_age);
        mGender = findViewById(R.id.update_gender);
        mLocation = findViewById(R.id.update_location);
        mOccupation = findViewById(R.id.update_occupation);
        mInterests = findViewById(R.id.update_interests);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

//        Toolbar toolbar = findViewById(R.id.user_profile_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }


    private void attemptUpdate() {
        showProgress(true);
        User postData = new User();
        postData.setName(mName.getText().toString().trim());
        postData.setSurname(mSurname.getText().toString().trim());
        postData.setAge(parseInt(mAge.getText().toString().trim()));
        postData.setGender(mGender.getText().toString().trim());
        postData.setLocation(mLocation.getText().toString().trim());
        postData.setOccupation(mOccupation.getText().toString().trim());
        postData.setInterests(mInterests.getText().toString().trim());

        String auth = sharedPref.getString("auth", null);
        if (auth != null) {
            mTask = new ProfileUpdateTask(postData, auth, UpdateUserProfileActivity.this);
            mTask.execute((Void) null);
        } else {
            showProgress(false);
            Toast.makeText(this, "No auth", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mUpdateFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void processFinish(boolean success, int code, String message) {
        if (success) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class ProfileUpdateTask extends AsyncTask<Void, Void, Boolean> {

        private final User mPostData;
        private User mUser;

        private AsyncResponse mDelegate;

        private String mAuth;
        private int mCode;


        ProfileUpdateTask(User postData, String auth, AsyncResponse delegate) {
            mPostData = postData;
            mAuth = auth;
            mDelegate = delegate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, mAuth);

            Call<User> call = api.updateProfile(mPostData);
            try {
                Response<User> response = call.execute();
                mUser = response.body();
                mCode = response.code();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return (mUser != null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTask = null;
            showProgress(false);
            if (mDelegate != null) {
                mDelegate.processFinish(success, mCode, "");
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }
}

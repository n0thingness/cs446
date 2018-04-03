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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
    private Button mUpdateImageButton;
    private ProfileUpdateTask mTask = null;
    private SharedPreferences sharedPref;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    public static Bitmap profile_image_bitmap; 

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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mUpdateImageButton = (Button) findViewById(R.id.update_image_button);
        mUpdateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                  openImageUploadActivity(view);
//                goBackToMainActivity();
                  Log.d("updateUserProfile: ", "BUTTON WAS CLICKED!!!!!!!!!>>><<<");
                  onPickImage(view);

            }
        });

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

    public void onPickImage(View view) {
        Log.d("updateUserProfile: ", "GOING TO PICK IMAGE!!!!!!!!!>>><<<");
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                profile_image_bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void openImageUploadActivity(View view){
        Intent intent = new Intent(this, ProfileImageUpload.class);
        startActivity(intent);
    }

    private void attemptUpdate() {
        View focusView = null;
        boolean cancel = false;



        String pName = mName.getText().toString().trim();
        String pSurname = mSurname.getText().toString().trim();
        String pAge = mAge.getText().toString().trim();
        String pGender = mGender.getText().toString().trim();
        String pLocation = mLocation.getText().toString().trim();
        String pOccuption = mOccupation.getText().toString().trim();
        String pInterests = mInterests.getText().toString().trim();

        if (TextUtils.isEmpty(pInterests)) {
            mInterests.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        if (TextUtils.isEmpty(pOccuption)) {
            mOccupation.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        if (TextUtils.isEmpty(pLocation)) {
            mLocation.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        if (TextUtils.isEmpty(pGender)) {
            mGender.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        if (TextUtils.isEmpty(pAge)) {
            mAge.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        if (TextUtils.isEmpty(pSurname)) {
            mSurname.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        if (TextUtils.isEmpty(pName)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            User postData = new User();
            postData.setName(pName);
            postData.setSurname(pSurname);
            postData.setAge(parseInt(pAge));
            postData.setGender(pGender);
            postData.setLocation(pLocation);
            postData.setOccupation(pOccuption);
            postData.setInterests(pInterests);
            String auth = sharedPref.getString("auth", null);
            if (auth != null) {
                showProgress(true);
                mTask = new ProfileUpdateTask(postData, auth, UpdateUserProfileActivity.this);
                mTask.execute((Void) null);
            } else {
                showProgress(false);
                Toast.makeText(this, "No auth", Toast.LENGTH_SHORT).show();
            }
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

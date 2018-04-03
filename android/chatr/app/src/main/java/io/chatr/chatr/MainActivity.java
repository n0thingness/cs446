package io.chatr.chatr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.chatr.chatr.data.model.LoginRequest;
import io.chatr.chatr.data.model.StringData;
import io.chatr.chatr.data.model.User;
import io.chatr.chatr.data.remote.ServiceGenerator;
import io.chatr.chatr.data.remote.chatrAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import io.chatr.chatr.data.model.Location;
import io.chatr.chatr.data.remote.chatrAPI;

import static io.chatr.chatr.UpdateUserProfileActivity.profile_image_bitmap;

public class MainActivity extends AppCompatActivity implements AsyncResponse<String> {

    private static final int REQUEST_PLACE_PICKER = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    private TextView mWelcomeTextView;

    private SharedPreferences sharedPref;

    private CheckInTask mTask = null;

    private CircleImageView top_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            checkPlayServices();
        }

        FloatingActionButton fabCheckIn = (FloatingActionButton) findViewById(R.id.main_check_in_fab);
        fabCheckIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mTask != null) {
                    return;
                }
                locationCheckIn();
            }
        });

        top_image = (CircleImageView) findViewById(R.id.main_profile_image);
        if (profile_image_bitmap != null){
//            top_image.setImageBitmap(profile_image_bitmap);
              Glide.with(this).load(profile_image_bitmap).into(top_image);
        }
        else {
            Glide.with(this).load(R.drawable.placeholder_cat).into(top_image);
        }
        top_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });

        mWelcomeTextView = (TextView) findViewById(R.id.welcome_text);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("auth", null);
//        editor.commit();

//        Log.d("auth", sharedPref.getString("auth", "none"));
        if (sharedPref.getString("auth", null) == null) {
            openLogin(null);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.showErrorDialogFragment(this, result, REQUEST_GOOGLE_PLAY_SERVICES);
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_PLACE_PICKER:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
//                    String toastMsg = String.format("Place: %s", place.getName());
//                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                    int id = -1;
                    String gid = place.getId();
                    String name = String.valueOf(place.getName());
                    String address = String.valueOf(place.getAddress());
                    String phoneNumber = String.valueOf(place.getPhoneNumber());
                    List<Integer> placeTypes = place.getPlaceTypes();
                    int priceLevel = place.getPriceLevel();
                    float rating = place.getRating();

                    Log.d("Location", gid);
                    Log.d("Location", name);
                    Log.d("Location", address);
                    Log.d("Location", phoneNumber);
                    Log.d("Location", String.valueOf(priceLevel));
                    Log.d("Location", String.valueOf(rating));

                    Location target = new Location(id, gid, name, address, phoneNumber, placeTypes, priceLevel, rating);

//                    showProgress(true);
                    mTask = new CheckInTask(target, MainActivity.this);
                    mTask.execute((Void) null);
                }
                break;
            case REQUEST_GOOGLE_PLAY_SERVICES:
                // not sure what to do here
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String auth = sharedPref.getString("auth", null);

        if (auth != null) {
            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);

            // Fetch a list of the Github repositories.
            Call<StringData> call = api.getResource();

            // Execute the call asynchronously. Get a positive or negative callback.
            call.enqueue(new Callback<StringData>() {
                @Override
                public void onResponse(Call<StringData> call, Response<StringData> response) {
                    // The network call was a success and we got a response
                    // TODO: use the repository list and display it
                    if (response.isSuccessful()) {
                        if (response.body().getData() != null) {
                            mWelcomeTextView.setText(response.body().getData());
                            if (top_image != null) {
                                Log.d("Main Activity: ", "image was updated!!! <<<>>>!!>>");
                                top_image.setImageBitmap(profile_image_bitmap);
                            }
                        }
                    } else {
                        mWelcomeTextView.setText("Failed");
                    }

                }

                @Override
                public void onFailure(Call<StringData> call, Throwable t) {
                    // the network call was a failure
                    // TODO: handle error
                }
            });

        }

//        mWelcomeTextView.setText("Hello");


    }

    public void locationCheckIn() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void openLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openUpdateProfileActivity(View view){
        Intent intent = new Intent(this, UpdateUserProfileActivity.class);
        startActivity(intent);
    }
  
    @Override
    public void processFinish(boolean success, int code, String message){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        if (success && !TextUtils.isEmpty(message)) {
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString("auth", mUser.getToken());
//            editor.commit();
            Intent intent = new Intent(this, LocationProfileActivity.class);
            intent.putExtra("gid", message);
            startActivity(intent);
        } else {
//            if (code == 409) {
            Toast.makeText(this, "An unknown error occurred", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class CheckInTask extends AsyncTask<Void, Void, Boolean> {

        private Location mLocation;
        private AsyncResponse mDelegate;
        private int mCode = -1;
        private String mMessage = "";

        CheckInTask(Location location, AsyncResponse delegate) {
            mLocation = location;
            mDelegate = delegate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Location rLocation = null;

            String auth = sharedPref.getString("auth", null);

            if (auth != null) {
//                Log.d("location ID is: ", mLocation.getGid());
                // get a photo of user selected location
                chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);
                Call<Location> call = api.getLocation(mLocation.getGid());
                Response<Location> response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                if (response != null) {
                    rLocation = response.body();
                    mCode = response.code();
                }

                if (rLocation != null) {
                    mMessage = rLocation.getGid();
                }

                if (mCode == 404) {
                    call = api.newLocation(mLocation);
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                    if (response != null) {
                        rLocation = response.body();
                        mCode = response.code();
                    }
                    if (rLocation != null) {
                        mMessage = rLocation.getGid();
                    }
                }
            }
            return (rLocation != null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTask = null;
//            showProgress(false);

            if (mDelegate != null) {
                mDelegate.processFinish(success, mCode, mMessage);
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
//            showProgress(false);
        }
    }

}

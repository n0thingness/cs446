package io.chatr.chatr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import de.hdodenhof.circleimageview.CircleImageView;
import io.chatr.chatr.data.model.StringData;
import io.chatr.chatr.data.remote.ServiceGenerator;
import io.chatr.chatr.data.remote.chatrAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PLACE_PICKER = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    private TextView mWelcomeTextView;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            checkPlayServices();
        }
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        FloatingActionButton fabCheckIn = (FloatingActionButton) findViewById(R.id.main_check_in_fab);
        fabCheckIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationCheckIn();
            }
        });

        CircleImageView top_image = (CircleImageView) findViewById(R.id.main_profile_image);
        Glide.with(this).load(R.drawable.placeholder_cat).into(top_image);

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
//                    Place place = PlacePicker.getPlace(this, data);
//                    String toastMsg = String.format("Place: %s", place.getName());
//                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, LocationProfileActivity.class);
                    startActivity(intent);
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
}

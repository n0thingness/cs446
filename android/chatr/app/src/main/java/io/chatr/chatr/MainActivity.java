package io.chatr.chatr;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class MainActivity extends AppCompatActivity {

    String API_URL = "http://uw-chatr-api.herokuapp.com";
    String PATH = "/api/v1/location";


    private static final int REQUEST_PLACE_PICKER = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    public MainActivity() throws IOException {
    }

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
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                    String place_name = String.valueOf(place.getName());
                    String place_id_str = place.getId();
                    String place_address = String.valueOf(place.getAddress());
                    String place_phone_no = String.valueOf(place.getPhoneNumber());
                    List<Integer> place_types = place.getPlaceTypes();
                    int place_price_level = place.getPriceLevel();
                    float place_rating = place.getRating();



                    JSONObject location_data = new JSONObject();
                    try {
                        location_data.put("id", place_id_str);
                        location_data.put("location_name", place_name);
                        location_data.put("location_address", place_address);
                        location_data.put("location_phone_no", place_phone_no);
                        location_data.put("location_types", place_types);
                        location_data.put("location_price_level", place_price_level);
                        location_data.put("location_rating", place_rating);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // Send a POST request to the server
                    HttpRequest request = null;
                    try {
                        request = new HttpRequest(API_URL + PATH).addHeader("Content-Type", "application/json");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int httpCode = 0;
                    try {
                        httpCode = request.post(new JSONObject().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (HttpURLConnection.HTTP_OK == httpCode) {
//                        int response = request.getJSONObjectResponse();
                        // do something?
                    } else {
                        // log error
                    }
                    request.close();

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


}

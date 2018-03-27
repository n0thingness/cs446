package io.chatr.chatr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.Task;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.chatr.chatr.data.model.Location;
import io.chatr.chatr.data.remote.ServiceGenerator;
import io.chatr.chatr.data.remote.chatrAPI;
import retrofit2.Call;
import retrofit2.Response;

public class LocationProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Location>, OnCompleteListener {

    private Bitmap bitmap = null;
    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private SharedPreferences sharedPref;

    private LocationProfileInfoTabFragment infoTabFragment;
    private LocationProfileUsersTabFragment usersTabFragment;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private ImageView top_image;

    private String intentGid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_profile);

        mToolbar = (Toolbar) findViewById(R.id.location_profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.location_profile_collapsing_toolbar_layout);

        top_image = (ImageView) findViewById(R.id.location_profile_top_image);

        int screenWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;

        top_image.getLayoutParams().width = screenWidthPixels;
        top_image.getLayoutParams().height = screenWidthPixels;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.location_profile_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.location_profile_tab_layout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle b = getIntent().getExtras();

        if (b != null && b.containsKey("gid")) {
            intentGid = b.getString("gid");
        }


//        infoTabFragment = (LocationProfileInfoTabFragment) getSupportFragmentManager().findFragmentById(R.id.location_profile_info_fragment);

//        getSupportLoaderManager().initLoader(0, null, (LoaderManager.LoaderCallbacks<Location>)this).forceLoad();

//        getSupportLoaderManager().initLoader(0, queryBundle, this);

        //Location image URL
//        String url = "http://www.simcoedining.com/img/venue_photos/williams-cafe-barrie.jpg";
//        Glide.with(this).load(url).into(top_image);
    }

    @Override
    public void onComplete() {
        Log.d("location activity", "onComplete: here");
        int loaderId = 0;
        Bundle queryBundle = new Bundle();
        queryBundle.putString("gid", intentGid);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Location> loader = loaderManager.getLoader(loaderId);
        if (loader == null) {
            Log.d("loader", "is null");
            loaderManager.initLoader(loaderId, queryBundle, this).forceLoad();
        } else {
            Log.d("loader", "is not null");
            loaderManager.restartLoader(loaderId, queryBundle, this);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Location> onCreateLoader(int id, Bundle args) {
        String gid = "";
        if (args.containsKey("gid")) {
            gid = args.getString("gid");
        }
        Log.d("loader", "onCreateLoader");
        return new FetchData(LocationProfileActivity.this, gid);
    }

    @Override
    public void onLoadFinished(Loader<Location> loader, Location data) {
        Log.d("onLoadFinished", "hi");
        if (data != null) {
            this.setTitle(data.getName());
            mCollapsingToolbarLayout.setTitle(data.getName());
            if (infoTabFragment != null) {
                infoTabFragment.setAddress(data.getAddress());
                infoTabFragment.setPrice(String.valueOf(data.getPriceLevel()));
                infoTabFragment.setRating(String.valueOf(data.getRating()));
            } else {
                Log.d("Location Error", "infoTabFragment is null");
            }

            getPhotos(data.getGid());
        }
    }

    @Override
    public void onLoaderReset(Loader<Location> loader) {}

    // Request photos and metadata for the specified place.
    private void getPhotos(String placeId) {
//        final String placeId = "ChIJa147K9HX3IAR-lwiGIQv9i4";
//        final Bitmap bitmap;
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                // Get the attribution text.
                CharSequence attribution = photoMetadata.getAttributions();
                // Get a full-size bitmap for the photo.
                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                photoResponse.addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        bitmap = photo.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Glide.with(LocationProfileActivity.this).load(stream.toByteArray()).into(top_image);
                    }
                });
            }
        });
        assert(bitmap!=null);
        Log.d("getPhotos: ", "completed function, bitmap should be assigned");

    }


    private static class FetchData extends AsyncTaskLoader<Location> {

        private String mGid;
        private int mCode;
        private Context mContext;

        public FetchData(Context context, String gid) {
            super(context);
            mContext = context;
            mGid = gid;
        }

        @Override
        public Location loadInBackground() {
            Location rLocation = null;
            Log.d("loader", "loadInBackground");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String auth = sharedPref.getString("auth", null);

            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);

            Call<Location> call = api.getLocation(mGid);
            Response<Location> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (response != null) {
                rLocation = response.body();
                mCode = response.code();
            }

            if (mCode == 404) {
                return null;
            }

            return rLocation;
        }

        @Override
        public void deliverResult(Location data) {
            super.deliverResult(data);
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return new LocationProfileUsersTabFragment();
                case 1:
                    return new LocationProfileInfoTabFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    usersTabFragment = (LocationProfileUsersTabFragment) createdFragment;
                    break;
                case 1:
                    Log.d("instantiateItem", "instantiateItem: info");
                    infoTabFragment = (LocationProfileInfoTabFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }
    }
}

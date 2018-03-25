package io.chatr.chatr;

import android.content.Context;
import android.content.res.Resources;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.chatr.chatr.data.model.Location;

public class LocationProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Location> {

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

    int query_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.location_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView top_image = (ImageView) findViewById(R.id.location_profile_top_image);

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

        Bundle b = getIntent().getExtras();
        query_id = b.getInt("id");

        getSupportLoaderManager().initLoader(0, null, (LoaderManager.LoaderCallbacks<Location>)this).forceLoad();


        //Location image URL
        String url = "http://www.simcoedining.com/img/venue_photos/williams-cafe-barrie.jpg";
        Glide.with(this).load(url).into(top_image);
        setTitle("Williams Fresh Cafe");
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
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Location> loader, Location data) {

    }

    @Override
    public void onLoaderReset(Loader<Location> loader) {

    }




    private static class FetchData extends AsyncTaskLoader<Location> {

        public FetchData(Context context) {
            super(context);
        }

        @Override
        public Location loadInBackground() {
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            String jsonStr = null;
//            String line;
//            try {
//                URL url = new URL("https://itunes.apple.com/search?term=classic");
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) return null;
//
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//                while ((line = reader.readLine()) != null) buffer.append(line);
//
//                if (buffer.length() == 0) return null;
//                jsonStr = buffer.toString();
//
//            } catch (IOException e) {
//                Log.e("MainActivity", "Error ", e);
//                return null;
//            } finally {
//                if (urlConnection != null) urlConnection.disconnect();
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("MainActivity", "Error closing stream", e);
//                    }
//                }
//            }

            return new Location();
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
                    LocationProfileUsersTabFragment tab1 = new LocationProfileUsersTabFragment();
                    return tab1;
                case 1:
                    LocationProfileInfoTabFragment tab2 = new LocationProfileInfoTabFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}

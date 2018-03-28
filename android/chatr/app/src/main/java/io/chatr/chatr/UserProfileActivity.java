package io.chatr.chatr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.chatr.chatr.data.model.Location;
import io.chatr.chatr.data.model.User;
import io.chatr.chatr.data.remote.ServiceGenerator;
import io.chatr.chatr.data.remote.chatrAPI;
import retrofit2.Call;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<User> {

    private String[] talkAbout = {"Coding","Playing Music", "Reading Books", "Cooking","Baking","Camping"};

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView tvInfoGender;
    private TextView tvInfoAge;
    private TextView tvInfoOccupation;
    private TextView tvInfoLocation;

    private FlexboxLayout topicContainer;
    private LinearLayout.LayoutParams llp;

    private int intentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.user_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView top_image = (ImageView) findViewById(R.id.user_profile_top_image);

        int screenWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;

        top_image.getLayoutParams().width = screenWidthPixels;
        top_image.getLayoutParams().height = screenWidthPixels;

//        Log.d("height", String.valueOf(top_image.getLayoutParams().height));
//        Log.d("width", String.valueOf(top_image.getLayoutParams().width));

        Glide.with(this).load(R.drawable.cat).into(top_image);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.user_profile_message_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        topicContainer = (FlexboxLayout) findViewById(R.id.user_profile_topics_flex_list);
        llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.text_bubble_margin);
        llp.setMargins(margin, margin, margin, margin);

//        for( int i = 0; i < talkAbout.length; i++ )
//        {
//            TextView textView = new TextView(this);
//            textView.setText(talkAbout[i]);
//            textView.setBackgroundResource(R.drawable.rounded_corner);
//            textView.setPadding(padding, padding,padding,padding);
//            topicContainer.addView(textView, llp);
//        }

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.user_profile_collapsing_toolbar_layout);

        tvInfoGender = (TextView) findViewById(R.id.user_profile_info_gender);
        tvInfoAge = (TextView) findViewById(R.id.user_profile_info_age);
        tvInfoOccupation = (TextView) findViewById(R.id.user_profile_info_occupation);
        tvInfoLocation = (TextView) findViewById(R.id.user_profile_info_location);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("id")) {
            intentId = b.getInt("id");
        }

        int loaderId = 1;
        Bundle queryBundle = new Bundle();
        queryBundle.putInt("id", intentId);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<User> loader = loaderManager.getLoader(loaderId);
        if (loader == null) {
            Log.d("loader", "is null");
            loaderManager.initLoader(loaderId, queryBundle, this).forceLoad();
        } else {
            Log.d("loader", "is not null");
            loaderManager.restartLoader(loaderId, queryBundle, this);
        }
    }

    @Override
    public Loader<User> onCreateLoader(int id, Bundle args) {
        int userId = 0;
        if (args.containsKey("id")) {
            userId = args.getInt("id");
        }
        Log.d("loader", "onCreateLoader");
        return new FetchData(UserProfileActivity.this, userId);
    }

    @Override
    public void onLoadFinished(Loader<User> loader, User data) {
        Log.d("onLoadFinished", "hi");
        if (data != null) {
            this.setTitle(data.getName() + " " + data.getSurname());
            mCollapsingToolbarLayout.setTitle(data.getName() + " " + data.getSurname());
            tvInfoGender.setText(data.getGender());
            tvInfoAge.setText(String.valueOf(data.getAge()));
            tvInfoOccupation.setText(data.getOccupation());
            tvInfoLocation.setText(data.getLocation());
            String topics = data.getInterests();
            String[] topicsList = new String[0];
            if (topics != null) {
                topicsList = topics.split(",[ ]*");
            }
            int padding = getResources().getDimensionPixelSize(R.dimen.text_bubble_padding);
            topicContainer.removeAllViewsInLayout();
            for( int i = 0; i < topicsList.length; i++ )
            {
                TextView textView = new TextView(this);
                textView.setText(topicsList[i]);
                textView.setBackgroundResource(R.drawable.rounded_corner);
                textView.setPadding(padding, padding,padding,padding);
                topicContainer.addView(textView, llp);
            }
//            if (infoTabFragment != null) {
//                infoTabFragment.setAddress(data.getAddress());
//                infoTabFragment.setPrice(String.valueOf(data.getPriceLevel()));
//                infoTabFragment.setRating(String.valueOf(data.getRating()));
//            } else {
//                Log.d("Location Error", "infoTabFragment is null");
//            }
        }
    }

    @Override
    public void onLoaderReset(Loader<User> loader) {

    }

    private static class FetchData extends AsyncTaskLoader<User> {

        private int mId;
        private int mCode;
        private Context mContext;

        public FetchData(Context context, int id) {
            super(context);
            mContext = context;
            mId = id;
        }

        @Override
        public User loadInBackground() {
            User rUser = null;
            Log.d("loader", "loadInBackground");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String auth = sharedPref.getString("auth", null);

            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);

            Call<User> call = api.getUser(mId);
            Response<User> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (response != null) {
                rUser = response.body();
                mCode = response.code();
            }

            if (mCode == 404) {
                return null;
            }

            return rUser;
        }

        @Override
        public void deliverResult(User data) {
            super.deliverResult(data);
        }
    }
}

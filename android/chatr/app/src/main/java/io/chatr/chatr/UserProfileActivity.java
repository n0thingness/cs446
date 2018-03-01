package io.chatr.chatr;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

public class UserProfileActivity extends AppCompatActivity {

    private String[] talkAbout = {"Coding","Playing Music", "Reading Books", "Cooking","Baking","Camping"};

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

        setTitle("John Smith");

        FlexboxLayout topicContainer = (FlexboxLayout) findViewById(R.id.user_profile_topics_flex_list);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.text_bubble_margin);
        int padding = getResources().getDimensionPixelSize(R.dimen.text_bubble_padding);
        llp.setMargins(margin, margin, margin, margin);

        for( int i = 0; i < talkAbout.length; i++ )
        {
            TextView textView = new TextView(this);
            textView.setText(talkAbout[i]);
            textView.setBackgroundResource(R.drawable.rounded_corner);
            textView.setPadding(padding, padding,padding,padding);
            topicContainer.addView(textView, llp);
        }

        TextView tvInfoGender = (TextView) findViewById(R.id.user_profile_info_gender);
        TextView tvInfoAge = (TextView) findViewById(R.id.user_profile_info_age);
        TextView tvInfoOccupation = (TextView) findViewById(R.id.user_profile_info_occupation);
        TextView tvInfoLocation = (TextView) findViewById(R.id.user_profile_info_location);

        tvInfoGender.setText("Male");
        tvInfoAge.setText("22");
        tvInfoOccupation.setText("Student");
        tvInfoLocation.setText("Waterloo");
    }
}

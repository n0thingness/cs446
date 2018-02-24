package io.chatr.chatr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

public class UserProfileActivity extends AppCompatActivity {

    private String[] talkAbout = {"lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
            "adipiscing", "elit", "curabitur", "vel", "hendrerit", "libero",
            "eleifend", "blandit", "nunc", "ornare", "odio", "ut",
            "orci", "gravida", "imperdiet", "nullam", "purus", "lacinia",
            "a", "pretium", "quis", "congue", "praesent", "sagittis",
            "laoreet", "auctor", "mauris", "non", "velit", "eros",
            "dictum", "proin", "accumsan", "sapien", "nec", "massa",
            "volutpat", "venenatis", "sed", "eu", "molestie", "lacus",
            "quisque", "porttitor", "ligula", "dui", "mollis", "tempus",
            "at", "magna", "vestibulum", "turpis", "ac", "diam",
            "tincidunt", "id", "condimentum", "enim", "sodales", "in",
            "hac", "habitasse", "platea", "dictumst", "aenean", "neque",
            "fusce", "augue", "leo", "eget", "semper", "mattis",
            "tortor", "scelerisque", "nulla", "interdum", "tellus", "malesuada",
            "rhoncus", "porta", "sem", "aliquet", "et", "nam",
            "suspendisse", "potenti", "vivamus", "luctus", "fringilla", "erat",
            "donec", "justo", "vehicula", "ultricies", "varius", "ante",
            "primis", "faucibus", "ultrices", "posuere", "cubilia", "curae",
            "etiam", "cursus", "aliquam", "quam", "dapibus", "nisl",
            "feugiat", "egestas", "class", "aptent", "taciti", "sociosqu",
            "ad", "litora", "torquent", "per", "conubia", "nostra",
            "inceptos", "himenaeos", "phasellus", "nibh", "pulvinar", "vitae",
            "urna", "iaculis", "lobortis", "nisi", "viverra", "arcu",
            "morbi", "pellentesque", "metus", "commodo", "ut", "facilisis",
            "felis", "tristique", "ullamcorper", "placerat", "aenean", "convallis",
            "sollicitudin", "integer", "rutrum", "duis", "est", "etiam",
            "bibendum", "donec", "pharetra", "vulputate", "maecenas", "mi",
            "fermentum", "consequat", "suscipit", "aliquam", "habitant", "senectus",
            "netus", "fames", "quisque", "euismod", "curabitur", "lectus",
            "elementum", "tempor", "risus", "cras"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView top_image = (ImageView) findViewById(R.id.place_image);
        top_image.getLayoutParams().height = top_image.getLayoutParams().width;

//        Glide.with(this).load("http://goo.gl/gEgYUd").into(top_image);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setTitle("John Smith");

        FlexboxLayout topicContainer = (FlexboxLayout) findViewById(R.id.topic_list);
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

        TextView tvInfoGender = (TextView) findViewById(R.id.profile_info_gender);
        TextView tvInfoAge = (TextView) findViewById(R.id.profile_info_age);
        TextView tvInfoOccupation = (TextView) findViewById(R.id.profile_info_occupation);
        TextView tvInfoLocation = (TextView) findViewById(R.id.profile_info_location);

        tvInfoGender.setText("Male");
        tvInfoAge.setText("22");
        tvInfoOccupation.setText("Student");
        tvInfoLocation.setText("Waterloo");





    }
}

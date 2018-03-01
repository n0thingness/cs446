package io.chatr.chatr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Daniel on 2018-02-27.
 */

public class LocationProfileUsersTabFragment extends Fragment {
    String listItem[]={"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense",   "HTC Sensation XE"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_location_profile_users_tab, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

//        ArrayList values = new ArrayList();
//        values.addAll(Arrays.asList(listItem));
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);

        ListView lv = (ListView) rootView.findViewById(R.id.location_profile_user_list);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(rootView.getContext(), R.layout.simple_list_item_1);

        lv.setAdapter(itemsAdapter);

        itemsAdapter.add("John Smith");
//        itemsAdapter.add("Sally");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Intent intent = new Intent(rootView.getContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


}

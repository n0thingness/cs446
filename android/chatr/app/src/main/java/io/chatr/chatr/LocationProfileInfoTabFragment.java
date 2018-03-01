package io.chatr.chatr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Daniel on 2018-02-27.
 */

public class LocationProfileInfoTabFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_profile_info_tab, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        TextView address_tv = (TextView) rootView.findViewById(R.id.location_profile_info_address);
        address_tv.setText("170 University Ave W");

        TextView hours_tv = (TextView) rootView.findViewById(R.id.location_profile_info_hours);
        hours_tv.setText("7am - 12am");
        return rootView;
    }
}

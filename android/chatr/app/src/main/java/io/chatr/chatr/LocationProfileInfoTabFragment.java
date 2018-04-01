package io.chatr.chatr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Daniel on 2018-02-27.
 */

public class LocationProfileInfoTabFragment extends Fragment {
    private TextView tvAddress;
    private TextView tvPrice;
    private TextView tvRating;

    private OnCompleteListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_profile_info_tab, container, false);

        Log.d("Fragment", "onCreateView: hi");

        tvAddress = (TextView) rootView.findViewById(R.id.location_profile_info_address);

        tvPrice = (TextView) rootView.findViewById(R.id.location_profile_info_price);

        tvRating = (TextView) rootView.findViewById(R.id.location_profile_info_rating);

        tvAddress.setText("default");
        tvPrice.setText("default");
        tvRating.setText("default");

        mListener.onComplete();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (OnCompleteListener)context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    public void setAddress(String address) {
        tvAddress.setText(address);
    }

    public void setPrice(String price) {
        tvPrice.setText(price);
    }

    public void setRating(String rating) {
        tvRating.setText(rating);
    }
}

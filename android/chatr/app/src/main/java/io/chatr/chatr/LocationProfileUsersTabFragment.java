package io.chatr.chatr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.chatr.chatr.data.model.Location;
import io.chatr.chatr.data.model.User;
import io.chatr.chatr.data.remote.ServiceGenerator;
import io.chatr.chatr.data.remote.chatrAPI;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Daniel on 2018-02-27.
 */

public class LocationProfileUsersTabFragment extends Fragment implements AsyncResponse {
    String listItem[]={"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense",   "HTC Sensation XE"};

    private View rootView;
    private ListView lv;
    private ArrayAdapter<String> itemsAdapter;
    private FetchMatch mTask = null;
    private Button mButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location_profile_users_tab, container, false); //final
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

//        ArrayList values = new ArrayList();
//        values.addAll(Arrays.asList(listItem));
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);

        lv = (ListView) rootView.findViewById(R.id.location_profile_user_list);

        itemsAdapter =
                new ArrayAdapter<String>(rootView.getContext(), R.layout.simple_list_item_1);

        lv.setAdapter(itemsAdapter);

//        itemsAdapter.add("John Smith");
////        itemsAdapter.add("Sally");
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position,
//                                    long id) {
//                Intent intent = new Intent(rootView.getContext(), UserProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        mButton = rootView.findViewById(R.id.get_match_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTask = new FetchMatch(getActivity(), LocationProfileUsersTabFragment.this);
                mTask.execute((Void) null);
            }
        });

        return rootView;
    }

    public void setUsers(final int user_id, String name) {
        itemsAdapter.add(name);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Intent intent = new Intent(rootView.getContext(), UserProfileActivity.class);
                intent.putExtra("id", user_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void processFinish(boolean success, int code, String message) {
        if (success) {
            setUsers(code, message);
        } else {
            Log.d("LocationUserFragment", "getMatch failed");
        }

    }

    public class FetchMatch extends AsyncTask<Void, Void, Boolean> {
        private String mGid;
        private int mCode;
        private Context mContext;
        private AsyncResponse mDelegate;

        private String mMessage = "";


        public FetchMatch(Context context, AsyncResponse delegate) {
            mContext = context;
            mDelegate = delegate;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            User rUser = null;
            Log.d("getMatch", "loadInBackground");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String auth = sharedPref.getString("auth", null);

            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);

            Call<User> call = api.getMatch();
            Response<User> response = null;


            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (response != null) {
                rUser= response.body();
                mCode = response.code();
            }

            if (mCode == 404) {
                return null;
            }

            if (rUser != null) {
                mCode = rUser.getId();
                mMessage = rUser.getName() +" " +rUser.getSurname();
            }



//            setUsers(rUser.getId(), rUser.getName(), rUser.getSurname());

            return (rUser != null);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mTask = null;
            if (mDelegate != null) {
                mDelegate.processFinish(success, mCode, mMessage);
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
        }
    }

}

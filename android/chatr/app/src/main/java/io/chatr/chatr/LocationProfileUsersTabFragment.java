package io.chatr.chatr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import io.chatr.chatr.data.model.User;
import io.chatr.chatr.data.remote.ServiceGenerator;
import io.chatr.chatr.data.remote.chatrAPI;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Daniel on 2018-02-27.
 */

public class LocationProfileUsersTabFragment extends Fragment implements AsyncResponse {

    private View rootView;
    private ListView lv;
    private ArrayAdapter<String> itemsAdapter;
    private FetchMatch mGetMatchTask = null;
    private ClearMatch mClearMatchTask = null;
    private Button getMatchButton;
    private Button clearMatchButton;
    private LinearLayout matchView;
    private TextView matchName;
    private TextView matchTopics;
    private TextView matchMessageSelf;
    private TextView matchMessageOther;

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

        matchView = rootView.findViewById(R.id.match_view);
        matchName = rootView.findViewById(R.id.match_name);
        matchTopics = rootView.findViewById(R.id.match_topics);
        matchMessageSelf = rootView.findViewById(R.id.match_msg_self);
        matchMessageOther = rootView.findViewById(R.id.match_msg_other);

        getMatchButton = rootView.findViewById(R.id.get_match_button);
        clearMatchButton = rootView.findViewById(R.id.clear_match_button);

        getMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetMatchTask = new FetchMatch(getActivity(), LocationProfileUsersTabFragment.this);
                mGetMatchTask.execute((Void) null);
            }
        });

        clearMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClearMatchTask = new ClearMatch(getActivity(), LocationProfileUsersTabFragment.this);
                mClearMatchTask.execute((Void) null);
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

    public void setMatchInfo(final int user_id, String name) {
        if (user_id != -1) {
            matchView.setVisibility(View.VISIBLE);
            matchName.setText(name);
            matchName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(rootView.getContext(), UserProfileActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                }
            });
            matchTopics.setText("Let's talk about");
            matchMessageSelf.setText("My message");
            matchMessageOther.setText("Other message");
        } else {
            matchView.setVisibility(View.GONE);
            matchName.setText("");
            matchTopics.setText("");
            matchMessageSelf.setText("");
            matchMessageOther.setText("");
        }
    }

    @Override
    public void processFinish(boolean success, int code, String message) {
        if (success) {
//            setUsers(code, message);
            setMatchInfo(code, message);
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
            mGetMatchTask = null;
            if (mDelegate != null) {
                mDelegate.processFinish(success, mCode, mMessage);
            }
        }

        @Override
        protected void onCancelled() {
            mGetMatchTask = null;
        }
    }

    public class ClearMatch extends AsyncTask<Void, Void, Boolean> {
        private int mCode;
        private Context mContext;
        private AsyncResponse mDelegate;

        private String mMessage = "";


        public ClearMatch(Context context, AsyncResponse delegate) {
            mContext = context;
            mDelegate = delegate;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            User rUser = null;
            Log.d("clearMatch", "loadInBackground");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String auth = sharedPref.getString("auth", null);

            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);

            Call<User> call = api.clearMatch();
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

            return (rUser != null);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mClearMatchTask = null;
            if (mDelegate != null) {
                mDelegate.processFinish(success, mCode, mMessage);
            }
        }

        @Override
        protected void onCancelled() {
            mClearMatchTask = null;
        }
    }

}

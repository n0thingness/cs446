package io.chatr.chatr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import io.chatr.chatr.data.model.Match;
import io.chatr.chatr.data.model.StringData;
import io.chatr.chatr.data.model.User;
import io.chatr.chatr.data.remote.ServiceGenerator;
import io.chatr.chatr.data.remote.chatrAPI;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Daniel on 2018-02-27.
 */

public class LocationProfileUsersTabFragment extends Fragment implements AsyncResponse<Match> {

    private View rootView;
    private ListView lv;
    private ArrayAdapter<String> itemsAdapter;
    private FetchMatch mGetMatchTask = null;
    private ClearMatch mClearMatchTask = null;
    private SetMatchMessage mSetMatchMessage = null;
    private Button getMatchButton;
    private Button clearMatchButton;
    private Button setMessageButton;
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
        setMessageButton = rootView.findViewById(R.id.set_message_button);

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





        setMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationProfileUsersTabFragment.this.getContext());
                builder.setTitle("Set Match Message");

                // Set up the input
                final EditText input = new EditText(LocationProfileUsersTabFragment.this.getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringData postData = new StringData();
                        postData.setData(input.getText().toString().trim());
                        mSetMatchMessage = new SetMatchMessage(postData, getActivity(), LocationProfileUsersTabFragment.this);
                        mSetMatchMessage.execute((Void) null);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
//                mSetMessageTask = new ClearMatch(getActivity(), LocationProfileUsersTabFragment.this);
//                mSetMessageTask.execute((Void) null);
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

    public void setMatchInfo(final int user_id, Match match) {
        if (user_id != -1) {
            matchView.setVisibility(View.VISIBLE);
            matchName.setText(match.getName() +" " + match.getSurname());
            matchName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(rootView.getContext(), UserProfileActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                }
            });
            matchTopics.setText("Let's talk about");
            matchMessageSelf.setText("Self message: " +match.getSelfMessage());
            matchMessageOther.setText("Other message: " +match.getOtherMessage());
        } else {
            matchView.setVisibility(View.GONE);
            matchName.setText("");
            matchTopics.setText("");
            matchMessageSelf.setText("");
            matchMessageOther.setText("");
        }
    }

    @Override
    public void processFinish(boolean success, int code, Match data) {
        if (success) {
//            setUsers(code, message);
            setMatchInfo(code, data);
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

        private Match rMatch;


        public FetchMatch(Context context, AsyncResponse delegate) {
            mContext = context;
            mDelegate = delegate;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.d("getMatch", "loadInBackground");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String auth = sharedPref.getString("auth", null);

            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);

            Call<Match> call = api.getMatch();
            Response<Match> response = null;


            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (response != null) {
                rMatch= response.body();
                mCode = response.code();
            }

            if (mCode == 404) {
                return null;
            }

            if (rMatch != null) {
                mCode = rMatch.getId();
//                mMessage = rUser.getName() +" " +rUser.getSurname();
            }



//            setUsers(rUser.getId(), rUser.getName(), rUser.getSurname());

            return (rMatch != null);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mGetMatchTask = null;
            if (mDelegate != null) {
                mDelegate.processFinish(success, mCode, rMatch);
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

        private Match rMatch;


        public ClearMatch(Context context, AsyncResponse delegate) {
            mContext = context;
            mDelegate = delegate;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.d("clearMatch", "loadInBackground");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String auth = sharedPref.getString("auth", null);

            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);

            Call<Match> call = api.clearMatch();
            Response<Match> response = null;

            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (response != null) {
                rMatch = response.body();
                mCode = response.code();
            }

            if (mCode == 404) {
                return null;
            }

            if (rMatch != null) {
                mCode = rMatch.getId();
                mMessage = rMatch.getName() +" " +rMatch.getSurname();
            }

            return (rMatch != null);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mClearMatchTask = null;
            if (mDelegate != null) {
                mDelegate.processFinish(success, mCode, rMatch);
            }
        }

        @Override
        protected void onCancelled() {
            mClearMatchTask = null;
        }
    }

    public class SetMatchMessage extends AsyncTask<Void, Void, Boolean> {
        private int mCode;
        private StringData mPostData;
        private Context mContext;
        private AsyncResponse mDelegate;

        private String mMessage = "";

        private Match rMatch;


        public SetMatchMessage(StringData postData, Context context, AsyncResponse delegate) {
            mPostData = postData;
            mContext = context;
            mDelegate = delegate;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.d("clearMatch", "loadInBackground");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String auth = sharedPref.getString("auth", null);

            chatrAPI api = ServiceGenerator.createService(chatrAPI.class, auth);

            Call<Match> call = api.setMatchMessage(mPostData);
            Response<Match> response = null;

            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (response != null) {
                rMatch = response.body();
                mCode = response.code();
            }

            if (mCode == 404) {
                return null;
            }

            if (rMatch != null) {
                mCode = rMatch.getId();
                mMessage = rMatch.getName() +" " +rMatch.getSurname();
            }

            return (rMatch != null);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSetMatchMessage = null;
            if (mDelegate != null) {
                mDelegate.processFinish(success, mCode, rMatch);
            }
        }

        @Override
        protected void onCancelled() {
            mSetMatchMessage = null;
        }
    }
}

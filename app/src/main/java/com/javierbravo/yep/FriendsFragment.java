package com.javierbravo.yep;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manu on 29/01/2016.
 */
public class FriendsFragment extends ListFragment {
    protected ProgressBar spinner;

    protected static final String TAG = "error";

    protected List<ParseUser> mUsers;
    protected ArrayList<String> usernames;
    protected ArrayList<String> objectIds;
    protected ArrayAdapter<String> adapter;

    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendsRelation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container,
                false);

        spinner =(ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser= ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        usernames = new ArrayList<String>();
        adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, usernames);


        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List users, ParseException e) {
                if (e == null) {
                    spinner.setVisibility(View.INVISIBLE);
                    mUsers = users;
                    for (ParseUser user : mUsers) {
                        adapter.add(user.getUsername());
                    }
                } else {
                    Log.e(TAG, "ParseException caught: ", e);
                    getString(R.string.query_error);
                }
            }
        });
        this.setListAdapter(adapter);

        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "yay:");
                } else {
                    Log.e(TAG, "ParseException caught: ", e);
                    getString(R.string.query_error);
                }
            }
        });
    }
}

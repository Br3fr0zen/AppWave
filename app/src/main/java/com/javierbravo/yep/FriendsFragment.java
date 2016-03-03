package com.javierbravo.yep;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by manu on 29/01/2016.
 */

public class FriendsFragment extends Fragment {

    protected static final String TAG = "Error";

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected GridView mGrid;
    protected TextView emptyText;
    protected String[] usernames;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    /*
    -> Grid view??
    protected int[] images;
    public String[] emails;
    public String email;*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container,
                false);
        mGrid = (GridView) rootView.findViewById(R.id.friendsGrid);
        emptyText = (TextView) rootView.findViewById(android.R.id.empty);
        mGrid.setEmptyView(emptyText);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.SwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.swipeRefresh1, R.color.swipeRefresh2, R.color.swipeRefresh3, R.color.swipeRefresh4);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List users, ParseException e) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (e == null) {
                    mUsers = users;
                    usernames = new String[mUsers.size()];
                    int i = 0;
                    for (ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }

                    if(mGrid.getAdapter() == null){
                        UserAdapter adapter = new UserAdapter(getActivity(),mUsers);
                        mGrid.setAdapter(adapter);
                    } else {
                        ((UserAdapter)mGrid.getAdapter()).refill(mUsers);
                    }

                   // ArrayAdapter <String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, usernames);
                    UserAdapter adapter = new UserAdapter(getActivity(),mUsers);
                    mGrid.setAdapter(adapter);
                    //getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);


                } else {
                    showUserError(e);
                }
            }
        });

        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Yay, it\'s working!!!: ");
                } else {
                    showUserError(e);
                }
            }
        });
    }

    private void showUserError(ParseException e) {
        Log.e(TAG, "ParseException caught: ", e);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error_load_users)
                .setMessage(e.getMessage())
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onResume();
        }
    };
}
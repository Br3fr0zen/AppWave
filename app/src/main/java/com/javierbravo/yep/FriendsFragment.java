package com.javierbravo.yep;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
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

public class FriendsFragment extends ListFragment {

    protected static final String TAG = "Error";

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected GridView mGrid;
    protected TextView emptyText;
    protected String[] usernames;

    /*
    -> Grid view??
    protected int[] images;
    public String[] emails;
    public String email;*/

    protected ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container,
                false);
        //mGrid = (GridView) rootView.findViewById(R.id.fGrid);
        emptyText = (TextView) rootView.findViewById(android.R.id.empty);
        emptyText.setVisibility(View.GONE);
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
                if (e == null) {
                    mUsers = users;
                    usernames = new String[mUsers.size()];
                    int i = 0;
                    for (ParseUser user : mUsers) {
                        if (mCurrentUser.equals(user.getUsername())) {

                        } else {
                            usernames[i] = user.getUsername();
                            i++;
                        }
                    }

                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, usernames);
                    setListAdapter(adapter);
                    getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);

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

    //Revisar cuando lleguemos al grid view.
    /*CustomGrid adapter = new CustomGrid(getActivity(), usernames , emails, images);
                    grid.setAdapter(adapter);
                    getActivity().setProgressBarIndeterminateVisibility(false);
                    if(mFriends.size()>0){
                        // Log.i("FriendsFragment.","Empty array.");
                        empty.setVisibility(View.INVISIBLE);
                    }else{
                        empty.setVisibility(View.VISIBLE);
                    }

      AÑADIR AL XML DENTRO DEL RELATIVE LAYOUT CUANDO SE IMPLEMENTE EL GRID

      <LinearLayout
        android:layout_width="fill_parent"
        android:layout_height="fill_parent">
        <GridView
            android:layout_width="match_parent"
            android:layout_height="515dp"
            android:id="@+id/fGrid"
            android:numColumns="3"
            android:scrollbars="vertical"
            android:layout_alignParentTop="true"
            android:layout_alignParentLeft="true"
            android:layout_alignParentStart="true"
            android:horizontalSpacing="10dp"
            android:verticalSpacing="10dp"
            android:layout_margin="10dp"
            android:gravity="center" />

    </LinearLayout>

    */
}
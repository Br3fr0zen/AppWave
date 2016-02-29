package com.javierbravo.yep;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manu on 02/02/2016.
 */
public class EditFriendsActivity extends ListActivity {

    protected static final String TAG = "Operation in course...";

    protected List<ParseUser> mUsers;
    protected ArrayList<String> usernames;
    protected ArrayList<String> objectIds;
    protected ArrayAdapter<String> adapter;

    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendsRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mFriendsRelation.add(mUsers.get(position));
        if(! this.getListView().isItemChecked(position)) {
            Log.d(TAG, "Deleted:");
            mFriendsRelation.remove(mUsers.get(position));

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser=ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        usernames = new ArrayList<>();
        objectIds = new ArrayList<>();
        adapter =  new ArrayAdapter<>(this,android.R.layout.simple_list_item_checked,usernames);
        setListAdapter(adapter);

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List users, ParseException e) {
                if (e == null) {
                    mUsers = users;
                    for (ParseUser user : mUsers) {
                        if(!user.getObjectId().equals(mCurrentUser.getObjectId())){
                            objectIds.add(user.getObjectId());
                            adapter.add(user.getUsername());
                        }
                    }
                    ArrayAdapter adapter1 = new ArrayAdapter<>(EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames);
                    setListAdapter(adapter1);
                    addFriendCheckmarks();
                } else {
                    Log.e(TAG, "ParseException caught: ", e);
                    getString(R.string.query_error);
                }
            }
        });

        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Success");
                } else {
                    Log.e(TAG, "ParseException caught: ", e);
                    showUserError(e);
                }
            }
        });
    }

    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    for (ParseUser user : friends) {
                        Log.d(TAG, "id " + user.getObjectId());
                        if (objectIds.contains(user.getObjectId()))
                                getListView().setItemChecked(objectIds.indexOf(user.getObjectId()), true);
                    }

                } else {
                    Log.e(TAG, "ParseException caught: ", e);
                    showUserError(e);
                }
            }
        });

    }

    private void showUserError(ParseException e) {
        Log.e(TAG, "ParseException caught: ", e);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_load_users)
                .setMessage(e.getMessage())
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog alert = builder.create();
        alert.show();
    }


}

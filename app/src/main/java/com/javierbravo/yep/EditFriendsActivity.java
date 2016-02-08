package com.javierbravo.yep;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
 * Created by manu on 02/02/2016.
 */
public class EditFriendsActivity extends ListActivity {
    protected ProgressBar progressBar;
    protected static final String TAG = "error";

    List<ParseUser> mUsers;
    ArrayList<String> usernames;
    ArrayList<String> objectIds;
    ArrayAdapter<String> adapter;


    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendsRelation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = (ProgressBar) findViewById(R.id.progressBarr);
        setContentView(R.layout.activity_edit_friends);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mFriendsRelation.add(mUsers.get(position));
        if(! this.getListView().isItemChecked(position)) {
            Log.d(TAG, "borrado:");
            mFriendsRelation.remove(mUsers.get(position));

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        usernames = new ArrayList<>();
        objectIds = new ArrayList<>();
        adapter =  new ArrayAdapter<>(getListView().getContext(),android.R.layout.simple_list_item_checked,usernames);

        setListAdapter(adapter);

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List users, ParseException e) {
                if (e == null) {
                    //progressBar.setVisibility(View.INVISIBLE);
                    mUsers = users;
                    for (ParseUser user : mUsers) {
                        objectIds.add(user.getObjectId());
                        adapter.add(user.getUsername());
                    }
                    addFriendCheckmarks();
                } else {
                    Log.e(TAG, "ParseException caught: ", e);
                    getString(R.string.query_error);
                }
            }
        });

        mCurrentUser=ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        this.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


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

    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback <ParseUser>(){
            @Override
            public void done(List<ParseUser> friends ,ParseException e){
                if(e == null){
                    for(ParseUser user : friends){
                        Log.d(TAG, "id " +user.getObjectId());
                        if(objectIds.contains(user.getObjectId())){
                            getListView().setItemChecked(objectIds.indexOf(user.getObjectId()), true);
                        }
                    }
                    //progressBar.setVisibility(View.INVISIBLE);
                }else{
                    Log.e(TAG, "ParseException caught: ", e);
                    getString(R.string.query_error);
                }
            }
        });

    }


}

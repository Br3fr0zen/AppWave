package com.javierbravo.yep;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.parse.ParseObject;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Bravo on 05/02/2016.
 */



public class InBoxFragment extends ListFragment {

    protected List<ParseObject> mMessage;
    protected ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container,
                false);

        spinner =(ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        return rootView;
    }


    public void done(List<ParseObject> messages, ParseException e) {
        getActivity().setProgressBarIndeterminateVisibility(false);

        if (e == null) {
            mMessage = messages;

            String[] usernames = new String[mMessage.size()];
            int i = 0;
            for (ParseObject message : mMessage) {
                usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                i++;
            }
            MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessage);
            setListAdapter(adapter);
        }


    }

}
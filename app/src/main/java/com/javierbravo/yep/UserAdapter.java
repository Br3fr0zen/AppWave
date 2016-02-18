package com.javierbravo.yep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class UserAdapter extends ArrayAdapter<ParseUser> {

    protected Context mContext;
    protected List<ParseUser> mUsers;

    public UserAdapter(Context context, List<ParseUser> users) {
        super(context, R.layout.message_item, users);
        mContext = context;
        mUsers = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            //holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon);
            holder.name_label = (TextView) convertView.findViewById(R.id.nameLabel);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        ParseUser user = mUsers.get(position);

       /* if (user.get(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
            holder.iconImageView.setImageResource(R.drawable.ic_photo);
        }
        else {
            holder.iconImageView.setImageResource(R.drawable.ic_movie);
        }*/

        holder.name_label.setText(user.getUsername());

        return convertView;
    }

    private static class ViewHolder {
        //ImageView iconImageView;
        TextView name_label;
    }

    public void refill(List<ParseUser> users){
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }
}

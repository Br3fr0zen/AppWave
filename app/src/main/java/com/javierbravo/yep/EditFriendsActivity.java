package com.javierbravo.yep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by manu on 02/02/2016.
 */
public class EditFriendsActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_friends);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new EditFriendsFragment())
                        .commit();
            }
        }

}

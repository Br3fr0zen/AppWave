package com.javierbravo.yep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by manu on 02/02/2016.
 */
public class EditFriendsActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_friends);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new EditFriendsFragment())
                        .commit();
            }
        }

}

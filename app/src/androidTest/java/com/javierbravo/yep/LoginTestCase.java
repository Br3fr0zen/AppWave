package com.javierbravo.yep;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

/**
 * Created by Bravo on 06/02/2016.
 */
public class LoginTestCase extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity actividad;
    private EditText usuarios;
    private EditText password;
    private Button loginBtn;

    private static final String USERNAME = "Manu";
    private static final String PASSWORD = "1 2 3";

    public LoginTestCase() {
        super(LoginActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        actividad = getActivity();
        usuarios = (EditText) actividad.findViewById(R.id.UserMainField);
        password = (EditText) actividad.findViewById(R.id.PasswordField);
        loginBtn = (Button) actividad.findViewById(R.id.LoginButton);
    }


    public void testAddValues() {
        if(ParseUser.getCurrentUser() != null)
            ParseUser.logOutInBackground();

        TouchUtils.tapView(this, usuarios);
        getInstrumentation().sendStringSync(USERNAME);

        TouchUtils.tapView(this, password);
        sendKeys(PASSWORD);

        TouchUtils.clickView(this, loginBtn);

        String Usuario = ParseUser.getCurrentUser().getUsername();

        assertTrue("Add result should be...", Usuario.equals(USERNAME));

    }

}
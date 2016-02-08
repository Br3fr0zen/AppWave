package com.javierbravo.yep;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;
//
public class LoginTestCase extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity activity;
    private EditText users;
    private EditText password;
    private Button loginButton;

    private static final String USERNAME = "Manu";
    private static final String PASSWORD = "1 2 3";

    public LoginTestCase() {
        super(LoginActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        users = (EditText) activity.findViewById(R.id.UserMainField);
        password = (EditText) activity.findViewById(R.id.PasswordField);
        loginButton = (Button) activity.findViewById(R.id.LoginButton);
    }


    public void testAddValues() {
        if(ParseUser.getCurrentUser() != null)
            ParseUser.logOutInBackground();

        TouchUtils.tapView(this, users);
        getInstrumentation().sendStringSync(USERNAME);

        TouchUtils.tapView(this, password);
        sendKeys(PASSWORD);

        TouchUtils.clickView(this, loginButton);

        String Usuario = ParseUser.getCurrentUser().getUsername();

        assertTrue("Add result should be...", Usuario.equals(USERNAME));

    }

}
package com.lognsys.kalrav;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginFragment;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.lognsys.kalrav.db.SQLiteHelper;
import com.lognsys.kalrav.db.UserInfoDAO;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private UserInfoDAOImpl userDaoImpl;
    private KalravApplication globalObj;


    public LoginActivity() {
        //Create database
        Log.d(TAG, "Instantiating Login Authentication");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //creating database sqlite and storing user pojo
        SQLiteHelper database = new SQLiteHelper(this);
        userDaoImpl = new UserInfoDAOImpl(this);
        //Instantiating global obj
        globalObj = ((KalravApplication) getApplicationContext());

        //LOGIN MODULE Step 1: Check if last user loggedIN
        UserInfo user = userDaoImpl.lastUserLoggedIn();

        if (null != user) {
            Log.d(TAG, "CASE1: User Exists in database.. Setting global object...");

            //setting global variable
            globalObj.setGlobalObject(user);

            Log.d(TAG, "OnCreate method - User Exists in DB. " + user.toString());


        } else {

            Log.d(TAG, "Case2: OnCreate method - Login through Facebook Auth and saving to database.");
            //Initialize Facebook sdk
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();

            //setting layout activity_login
            setContentView(com.lognsys.kalrav.R.layout.activity_login);

            loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_location"));
            //loginButton.setReadPermissions(Arrays.asList("read_stream, public_profile, email, user_birthday, user_friends, user_about_me, user_location, user_likes"));
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    requestData();

                    //start home activity
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);

                    loginButton.setVisibility(View.INVISIBLE);

                    //close current activity
                    finish();

                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Login Cancelled...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException e) {

                    Toast.makeText(getApplicationContext(), "Login Error: Check network connection...", Toast.LENGTH_SHORT).show();

                    //log facebook Error
                    Log.e(TAG, e.getMessage());
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {

        Log.d(TAG, "OnResume method called...");
        super.onResume();

        UserInfo user = globalObj.getGlobalObject();


        if (null != user) {

            Log.v(TAG, "OnResume method - Starting HOME_ACTIVITY FROM ");
            //start home activity
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);

            this.finish();

        } else {
            Log.v(TAG, "OnResume method - global Object user - NULL");
        }

    }


    /**
     * requestData to get data
     */
    public void requestData() {

        Log.d(TAG, "Requesting facebook data...");

        final UserInfo userInfo = new UserInfo();

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.v(TAG, " RESPONSE FB - " + response.toString());

                JSONObject json = response.getJSONObject();

                try {
                    if (json != null) {

                        userInfo.setFb_id(json.getString("id"));
                        userInfo.setEmail(json.getString("email"));
                        userInfo.setName(json.getString("name"));
                        userInfo.setBirthday(json.getString("birthday"));
                        userInfo.setLocation(json.getJSONObject("location").getString("name"));
                        userInfo.setLoggedIn(Constants.LOG_IN);

                        globalObj.setGlobalObject(userInfo);

                        //save to the database
                        userDaoImpl.addUser(userInfo);

                    } else {
                        Log.e(TAG, "Could not retrieve data from facebook graphapi...");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();

    }

}

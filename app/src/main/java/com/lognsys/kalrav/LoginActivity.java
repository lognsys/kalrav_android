package com.lognsys.kalrav;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.lognsys.kalrav.db.SQLiteHelper;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.FontManager;
import com.lognsys.kalrav.util.KalravApplication;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import com.lognsys.kalrav.R;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private CallbackManager facebookCallbackManager;
    private LoginButton loginButton;
    private UserInfoDAOImpl userDaoImpl;
    private HTextView hTextView;


    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    Handler mHtHandler;
    Handler mUiHandler;

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
       // globalObj = ((KalravApplication) getApplicationContext());

        //LOGIN MODULE Step 1: Check if last user loggedIN
        UserInfo user = userDaoImpl.lastUserLoggedIn();

        if (null != user) {
            Log.d(TAG, "CASE1: User Exists in database.. Setting global object...");

            //setting global variable
            KalravApplication.getInstance().setGlobalUserObject(user);

            Log.d(TAG, "OnCreate method - User Exists in DB. " + user.toString());


        } else {

            Log.d(TAG, "Case2: OnCreate method - Login through Facebook Auth and saving to database.");
            //Initialize Facebook sdk
            FacebookSdk.sdkInitialize(getApplicationContext());
            facebookCallbackManager = CallbackManager.Factory.create();

            //setting layout activity_login
            setContentView(R.layout.activity_login);


            hTextView = (HTextView) findViewById(R.id.text);
            hTextView.setTypeface(FontManager.getInstance(getApplicationContext().getAssets()).getFont("fonts/Mirza-Regular.ttf"));
            // be sure to set custom typeface before setting the animate type, otherwise the font may not be updated.
            hTextView.setAnimateType(HTextViewType.TYPER);
            hTextView.animateText(getBaseContext().getString(R.string.company_name)); // animate


            loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_location"));
            //loginButton.setReadPermissions(Arrays.asList("read_stream, public_profile, email, user_birthday, user_friends, user_about_me, user_location, user_likes"));
            loginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
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
        boolean isFacebookLogin = facebookCallbackManager.onActivityResult(requestCode, resultCode, data);


    }


    @Override
    protected void onPause() {
        super.onPause();

        stoptimertask(getWindow().getDecorView().getRootView());

    }

    @Override
    protected void onResume() {

        Log.d(TAG, "OnResume method called...");
        super.onResume();


        //onResume we start our timer so it can start when the app comes from the background
        startTimer();

        //Check if user is loggedIn
        UserInfo user =  KalravApplication.getInstance().getGlobalUserObject();
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


    public void startTimer() {

        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms

        timer.schedule(timerTask, 5000, 9000); //

    }

    public void stoptimertask(View v) {
        Log.d(TAG, "OnResume method called...");
        //stop the timer, if it's not already null
        if (timer != null) {


            Log.d(TAG, "Stopping timer HTextView animated text...");
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }


    public void initializeTimerTask() {
        timerTask = new TimerTask() {

            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {

                    public void run() {
                        hTextView = (HTextView) findViewById(R.id.text);
                        hTextView.setTypeface(FontManager.getInstance(getApplicationContext().getAssets()).getFont("fonts/Mirza-Regular.ttf"));
                        // be sure to set custom typeface before setting the animate type, otherwise the font may not be updated.
                        hTextView.setAnimateType(HTextViewType.TYPER);
                        hTextView.animateText(getBaseContext().getString(R.string.company_name)); // animate

                    }
                });
            }
        };
    }


    /**
     * Request data from facebook api
     */
    public void requestData() {

        Log.d(TAG, "Requesting facebook data...");

        final UserInfo userInfo = new UserInfo();

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.v(TAG, " Facebook API response - " + response.toString());

                JSONObject json = response.getJSONObject();

                try {
                    if (json != null) {

                        userInfo.setFb_id(json.getString("id"));
                        userInfo.setEmail(json.getString("email"));
                        userInfo.setName(json.getString("name"));
                        userInfo.setBirthday(json.getString("birthday"));
                        userInfo.setLocation(json.getJSONObject("location").getString("name"));
                        userInfo.setLoggedIn(Constants.LOG_IN);

                        KalravApplication.getInstance().setGlobalUserObject(userInfo);

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

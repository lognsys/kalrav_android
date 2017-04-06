package com.lognsys.kalrav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.lognsys.kalrav.db.SQLiteHelper;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.dialog.NetworkStatusDialog;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    //faceboook callback manager
    private CallbackManager mCallbackManager;
    private static final int RC_SIGN_IN = 100;
    private static final int RC_NETWORK_DIALOG = 101;

    //login_activity UI variable
    private ImageView fbSignIn, googSignIn;
    private LoginButton loginButton;

    //firebase variable declaration
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Google API variable
    private GoogleApiClient mGoogleApiClient;


    //Shared preference variables;
    private String email = "";
    private boolean login_status = false;
    private String oauthId = "";
    private String device_token_id = "";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize SharedPreferences
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //SharedPreferences Editor
        SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();


        //facebook initialize
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        //Add device token
        device_token_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("devicetoken", device_token_id);
        sharedPrefEditor.putString("Constants.Shared.DEVICE_TOKEN_ID.name()", device_token_id);
        sharedPrefEditor.commit();
//Add device type
        String manufacturer = Build.MANUFACTURER; //this one will work for you.
        String product = Build.PRODUCT;
        String model = Build.MODEL;
        String s = "Manufacturer:" + manufacturer + ",Product:" + product + " ," + "model: " + model;
        sharedPrefEditor.putString("device_type", "android tab");
        sharedPrefEditor.commit();
        //Use Case 1:If cust_id in shared pref then goto MainActivity
        String cust_id = sharedpreferences.getString("Constants.Shared.CUSTOMER_ID.name()", null);
        if (cust_id != null) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        /****************************** FACEBOOK AUTH *****************************************/
        //FB:1 Initialize callbackmanager factory object
        mCallbackManager = CallbackManager.Factory.create();

        //FB:2 Facebook authentication code
        loginButton = (LoginButton) findViewById(R.id.fb_image);
        loginButton.setBackgroundResource(R.drawable.fb);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setReadPermissions("email", "user_birthday", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                //call graph user
                AsyncTask.Status status = saveFacebookData(loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken()); // method call after sucessfull authorisation
                Log.e("token", String.valueOf(loginResult.getAccessToken()));
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                return;
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

                //DONE 5 : Return Dialog box with network error
                DialogFragment dialog = new NetworkStatusDialog();
                Bundle args = new Bundle();
                  /*  args.putString("title", getString(R.string.text_network_title));
                    args.putString("message", getString(R.string.text_network_msg));*/
                dialog.setArguments(args);
                dialog.setTargetFragment(dialog, RC_NETWORK_DIALOG);
                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");

                return;
            }
        });
        /******************************************************************************/

        /*********************************GOOGLE AUTH*************************************/

        //GOOG:1 Google authentication code
        googSignIn = (ImageView) findViewById(R.id.google_image);
        googSignIn.setOnClickListener(this);

        //GOOG:2 sign-in builder pattern with tokenid, email attributes
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //GOOG:3 Pass google "signInOption" variable with attributes to googleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this /* FragmentActivity */, this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        /******************************************************************************/


        /*********************************FIREBASE************************************/

        //Initialize FirebaseAuth
        //You need to include google-services.json (downloaded from firebase console) file under the "app" folder of this project.
        mAuth = FirebaseAuth.getInstance();

        // firebase authentication listener will chek if user is authenticated.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Log.d(TAG, "onAuthStateChanged:Provider:" + user.getProviderId());

                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        /******************************************************************************/


        /******************** USER REGISTATION SIGN IN  & VALIDATION **********************/


        //TODO: Do Authentication of name and email address and call API to validate User
        //This is the Sign-In button for already registered users
        //Test conditions if username & password is not blank

        /**
         * Button Sign_in function
         * params : username, password, device_token
         */


    }


    /**
     * CLick On Google IMAGE
     * View a root element used to call sub elements
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_image:
                signIn();
                break;
            default:
                return;
        }
    }

    /**
     * Signin method with GoogleSignInApi
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //After redirecting from current activity (LoginActivity) to Google OAuth
    //or facebook OAuth after authentication it will call this method immediately
    //Based on the request code it will call the activity either google or facebook

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode==100) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Log.e("result",result.getSignInAccount().getServerAuthCode().toString());

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {

                // Google Sign In failed, update UI appropriately
                DialogFragment dialog = new NetworkStatusDialog();
                Bundle args = new Bundle();
                  /*  args.putString("title", getString(R.string.text_google_error_title));
                    args.putString("message", getString(R.string.text_google_error_msg));*/
                dialog.setArguments(args);
                dialog.setTargetFragment(dialog, 100);
                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment TAG");
                return;

            }
        }

    }


    /**
     * This method is called from onActvityResult#GoogleSignInResult
     *
     * @param acct
     */
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        //Check if Goog with same email-id already present
        String checkFacebookEmail = sharedpreferences.getString("Constants.FacebookFields.FB_EMAIL_ID.name()", "");
       if ((!checkFacebookEmail.equals("")) && checkFacebookEmail.equals(acct.getEmail()))
            sharedPrefEditor.putBoolean("Constants.Shared.IS_SIMILAR_EMAILID.name()", true);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:Google:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "Google Login Success...");
                            String firebaseUID = mAuth.getCurrentUser().getUid();
                             /*   sharedPrefEditor.putString(Constants.GoogleFields.GOOG_UID.name(), firebaseUID);
                                sharedPrefEditor.putString(Constants.GoogleFields.GOOG_DISPLAY_NAME.name(), acct.getDisplayName());
                                sharedPrefEditor.putString(Constants.GoogleFields.GOOG_EMAIL_ID.name(), acct.getEmail());
                                sharedPrefEditor.putString(Constants.GoogleFields.GOOG_GIVEN_NAME.name(), acct.getGivenName());
                                sharedPrefEditor.putString(Constants.GoogleFields.GOOG_PHOTO_URL.name(), acct.getPhotoUrl().toString());
                                sharedPrefEditor.putString(Constants.GoogleFields.GOOG_TOKEN_ID.name(), acct.getIdToken());
                                sharedPrefEditor.putString(Constants.GoogleFields.GOOG_SERVE_AUTHCODE.name(), acct.getServerAuthCode());
                                sharedPrefEditor.commit();*/

                            //start activity MainActivity.class
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }

                    }
                });
    }

    /**
     * call this method by facebook callbackmanager on successful login authorisation.
     *
     * @param token
     */

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token.getUserId());

        //Adding facebook userid in shared_preferences
        final SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithCredential:onComplete Facebook:" + task.isSuccessful());

                        //FB:UID
                        String firebaseUID = mAuth.getCurrentUser().getUid();
                        sharedPrefEditor.putString("Constants.FacebookFields.FB_UID.name()", firebaseUID);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            try {
                                Log.e(TAG, "signInWithCredential", task.getException());
                                throw task.getException();

                            } catch (FirebaseAuthUserCollisionException fe) {

                                //Google account already created in Firebase with same EmailId as Facebook EmailId
                                Log.e(TAG, "#handleFacebookAccessToken#FacebookAuthUserCollision - " + fe.getMessage());

                                sharedPrefEditor.putBoolean("Constants.Shared.IS_SIMILAR_EMAILID.name()", true);

                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();

                            } catch (Exception e) {
                                Log.e(TAG, "handleFacebookAccessToken#Facebook Login Exception - " + e.getMessage());
                            }

                        } else {

                            Log.d(TAG, "Facebook Login Successful...");
                            Log.d(TAG, "Starting MainActivity...");
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
    }

    /**
     * @param token
     */
    public AsyncTask.Status saveFacebookData(AccessToken token) {

        final SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();
        //final FBUser fbuser = new FBUser();

        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.v(TAG, " Facebook API response - " + response.toString());

                JSONObject json = response.getJSONObject();

                try {
                    if (json != null) {
                        String s = json.getString("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

                            /*fbuser.setId(json.getString("id"));
                            sharedPrefEditor.putString(Constants.FacebookFields.FB_ID.name(), fbuser.getId());

                            fbuser.setEmail(json.getString("email"));
                            sharedPrefEditor.putString(Constants.FacebookFields.FB_EMAIL_ID.name(), fbuser.getEmail());

                            fbuser.setName(json.getString("name"));
                            sharedPrefEditor.putString(Constants.FacebookFields.FB_NAME.name(), fbuser.getName());

                            fbuser.setFirst_name(json.getString("first_name"));
                            sharedPrefEditor.putString(Constants.FacebookFields.FB_FIRST_NAME.name(), fbuser.getFirst_name());

                            fbuser.setLast_name(json.getString("last_name"));
                            sharedPrefEditor.putString(Constants.FacebookFields.FB_LAST_NAME.name(), fbuser.getLast_name());

                            fbuser.setLink(json.getString("link"));
                            sharedPrefEditor.putString(Constants.FacebookFields.FB_LINK.name(), fbuser.getLink());

                            fbuser.setPicture(json.getJSONObject("picture").getJSONObject("data").getString("url"));
                            sharedPrefEditor.putString(Constants.FacebookFields.FB_PICTURE.name(), fbuser.getPicture());

                            fbuser.setTimezone(json.getString("timezone"));
                            sharedPrefEditor.putString(Constants.FacebookFields.FB_TIME_ZONE.name(), fbuser.getTimezone());

                            sharedPrefEditor.commit();

                           // Log.d(TAG, "Facebook User : " + fbuser);

                        } else {
                            Log.e(TAG, "Could not retrieve data from facebook graphapi...");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, cover, name, first_name, last_name, link, " +
                    "gender, picture, timezone, updated_time, email");
            request.setParameters(parameters);

            AsyncTask.Status status = request.executeAsync().getStatus();
            return status;
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            // An unresolvable error has occurred and Google APIs (including Sign-In) will not
            // be available.
            Log.e(TAG, "onConnectionFailed:" + connectionResult);
            Toast.makeText(this, "Google Unresolved Error occurred....", Toast.LENGTH_SHORT).show();
        }


        @Override
        protected void onResume() {
            super.onResume();
        }

        @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);

            }
        }

   /* private final String TAG = getClass().getSimpleName();
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


    *//**
         * Request data from facebook api
         *//*
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
}                        //save to the database
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

    }*/
        AsyncTask.Status status = request.executeAsync().getStatus();
        return status;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Unresolved Error occurred....", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }


}


package com.lognsys.kalrav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.lognsys.kalrav.FCM.FCMInstanceIdService;
import com.lognsys.kalrav.db.SQLiteHelper;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.dialog.NetworkStatusDialog;
import com.lognsys.kalrav.model.Auditorium;
import com.lognsys.kalrav.model.SeatExample;
import com.lognsys.kalrav.model.SeatsDetailInfo;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.schemes.SchemePrabhodhanFragment;
import com.lognsys.kalrav.util.CallAPI;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.FontManager;
import com.lognsys.kalrav.util.KalravApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.lognsys.kalrav.util.PropertyReader;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    //faceboook callback manager
    private CallbackManager mCallbackManager;
    private static final int RC_SIGN_IN = 100;

    Timer timer;
    TimerTask timerTask;
    TextView textSkipLogin;

    //login_activity UI variable
    private ImageView googSignIn;
    private LoginButton loginButton;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    private HTextView hTextView;

    //firebase variable declaration
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Google API variable
    private GoogleApiClient mGoogleApiClient;


    //Shared preference variables;
    private String fb_id,google_id,email = "";
    UserInfoDAOImpl userDaoImpl;
    SharedPreferences sharedpreferences;
    CallAPI callAPI;
boolean isServerDown;

    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";
    String seatAuth=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        seatAuth = intent.getStringExtra("seatAuth");

        //creating database sqlite and storing user pojo
        SQLiteHelper database = new SQLiteHelper(this);
        userDaoImpl = new UserInfoDAOImpl(this);
        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);
        isServerDown=getIntent().getBooleanExtra("isServerDown",false);
        //Instantiating global obj
        // globalObj = ((KalravApplication) getApplicationContext());
        UserInfo user=null;
        callAPI = new CallAPI(LoginActivity.this);

        if( userDaoImpl.lastUserLoggedIn()!=null && isServerDown==false ){
            user = userDaoImpl.lastUserLoggedIn();
            if ( user !=null && KalravApplication.getInstance().getPrefs().getIsLogin()) {
                Log.d(TAG, "Rest CASE1: User Exists in database.. ");
                if (KalravApplication.getInstance().getPrefs().getCustomer_id() != null) {
                    //setting global variable
                    KalravApplication.getInstance().setGlobalUserObject(user);
                    Log.v(TAG, " isConnecting -----onCreate  ");

                    Log.v(TAG, " isConnecting -----onCreate  KalravApplication.getInstance().isConnectedToInternet()"+KalravApplication.getInstance().isConnectedToInternet());
                    if(KalravApplication.getInstance().isConnectedToInternet()){
                        Log.v(TAG, " isConnecting -----onCreate  KalravApplication.getInstance().isServerReachable(LoginActivity.this) "+KalravApplication.getInstance().isServerReachable(getApplicationContext()));

                        if(KalravApplication.getInstance().isServerReachable(getApplicationContext())){
                            if (user.getEmail() != null && user.getEmail().length() > 0) {
                                String alReadyExsistUser=properties.getProperty(Constants.API_URL_USER.post_userdetails_already_exist_url.name());
                                callAPI.alReadyExsistUser(user, fb_id, google_id,alReadyExsistUser,seatAuth,LoginActivity.this);
                            }
                        }
                        else{
                            KalravApplication.getInstance().showDialog(LoginActivity.this,getString(R.string.unknown_error));

                        }
                    }
                    else{
                        KalravApplication.getInstance().buildDialog(LoginActivity.this);
                    }

                }
            }
        }
        else {
            Log.d(TAG, "Rest CASE2: OnCreate method - Login through Facebook Auth and saving to database.");
            if(KalravApplication.getInstance().getPrefs().getDevice_token()==null)
                invokeFCMService();
            Log.d(TAG, "Rest CASE2: OnCreate method - token "+KalravApplication.getInstance().getPrefs().getDevice_token());

            //Initialize Facebook sdk
            FacebookSdk.sdkInitialize(getApplicationContext());
            mCallbackManager = CallbackManager.Factory.create();

            //setting layout activity_login
            setContentView(R.layout.activity_login);
            //Initialize FirebaseAuth
            //You need to include google-services.json (downloaded from firebase console) file under the "app" folder of this project.
            mAuth = FirebaseAuth.getInstance();
            Log.d(TAG, "onCreate mAuth:========================== " + mAuth);

            textSkipLogin = (TextView) findViewById(R.id.textSkipLogin);
            textSkipLogin.setOnClickListener(this);
            hTextView = (HTextView) findViewById(R.id.text);

            hTextView.setTypeface(FontManager.getInstance(getApplicationContext().getAssets()).getFont("fonts/Mirza-Regular.ttf"));

            // be sure to set custom typeface before setting the animate type, otherwise the font may not be updated.
            hTextView.setAnimateType(HTextViewType.TYPER);
            hTextView.animateText(getBaseContext().getString(R.string.company_name)); // animate


            loginButton = (LoginButton) findViewById(R.id.fb_image);

            loginButton.setBackgroundResource(R.drawable.fb);
            loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    Log.v(TAG, " Facebook API requestData - ");
                    if (KalravApplication.getInstance().getPrefs().getDevice_token() == null)
                    {
                        invokeFCMService();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 500ms
                            if(isServerDown==false){
                                requestData(loginResult);
                            }

                        }
                    }, 3000);
                    Log.d(TAG, "mCallbackManager loginResult ==" + loginResult);
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Login attempt canceled.", Toast.LENGTH_LONG).show();

                    Log.d(TAG, "mCallbackManager onCancel ==");
                }

                @Override
                public void onError(FacebookException e) {
                    Log.d(TAG, "mCallbackManager FacebookException ==" + e);

                    Toast.makeText(getApplicationContext(), "Login attempt failed.", Toast.LENGTH_LONG).show();

                }
            });

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

            /*********************************FIREBASE************************************/
            // firebase authentication listener will chek if user is authenticated.
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    Log.d(TAG, "onCreate FirebaseAuth:========================== " + firebaseAuth);

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "onAuthStateChanged:user:" + user);
                    if (user != null) {

                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid() + " Provider:" + user.getProviderId());
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid() + " Provider:" + user.getProviderId());
                        KalravApplication.getInstance().getPrefs().setIsLogin(true);
                        final UserInfo userInfo = new UserInfo();


                        try {
                            if (mAuth.getCurrentUser().getPhotoUrl() != null) {
                                KalravApplication.getInstance().getPrefs().setImage(String.valueOf(mAuth.getCurrentUser().getPhotoUrl()));
                                KalravApplication.getInstance().getPrefs().setIsFacebookLogin(false);
                            }

                            if (mAuth.getCurrentUser().getUid() != null) {
                                userInfo.setGoogle_id(mAuth.getCurrentUser().getUid());
                            }
                            if (mAuth.getCurrentUser().getEmail() != null) {
                                userInfo.setEmail(mAuth.getCurrentUser().getEmail());
                                KalravApplication.getInstance().getPrefs().setEmail(mAuth.getCurrentUser().getEmail());
                            }
                            if (mAuth.getCurrentUser().getDisplayName() != null) {
                                userInfo.setName(mAuth.getCurrentUser().getDisplayName());
                                KalravApplication.getInstance().getPrefs().setName(mAuth.getCurrentUser().getDisplayName());
                            }
                            userInfo.setLoggedIn(Constants.LOG_IN);
                            Log.v(TAG, " isConnecting -----onCreate  else ----------");


                            if(isServerDown==false){

                                if(KalravApplication.getInstance().isConnectedToInternet()){
                                    if(KalravApplication.getInstance().isServerReachable(LoginActivity.this)){
                                        if (userInfo.getEmail() != null && userInfo.getEmail().length() > 0 && KalravApplication.getInstance().getPrefs().getIsLogin() == true) {

                                            String alReadyExsistUser=properties.getProperty(Constants.API_URL_USER.post_userdetails_already_exist_url.name());
                                            callAPI.alReadyExsistUser(userInfo, fb_id, google_id,alReadyExsistUser,seatAuth, LoginActivity.this);
                                        }
                                    }
                                    else{
                                        KalravApplication.getInstance().showDialog(LoginActivity.this,getString(R.string.unknown_error));
                                    }
                                }
                                else{
                                    KalravApplication.getInstance().buildDialog(LoginActivity.this);
                                }

                            }
                        } catch (Exception e) {
                            Log.d(TAG, "firebaseAuthWithGoogle :acct Exception ..." + e);

                        }
                    }
                }
            };
        }
    }

    public void invokeFCMService() {
        Log.d(TAG, "Rest invokeFCMService ");

        if(KalravApplication.getInstance().getPrefs().getDevice_token()==null){
            Intent i= new Intent(LoginActivity.this, FCMInstanceIdService.class);
            startService(i);
        }
    }


    private void requestData(final LoginResult loginResult) {

        final UserInfo userInfo = new UserInfo();

        final GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        userInfo.setFb_id(json.getString("id"));
                        if(userInfo.getFb_id()!=null){
                            fb_id=userInfo.getFb_id();
                        }
                        else{
                            fb_id=loginResult.getAccessToken().getUserId();
                        }
                        KalravApplication.getInstance().getPrefs().setUser_id(fb_id);

                        KalravApplication.getInstance().getPrefs().setImage(userInfo.getFb_id());
                        KalravApplication.getInstance().getPrefs().setIsFacebookLogin(true);
                        userInfo.setEmail(json.getString("email"));
                        KalravApplication.getInstance().getPrefs().setEmail(userInfo.getEmail());
                        userInfo.setName(json.getString("name"));
                        userInfo.setLoggedIn(Constants.LOG_IN);
                        KalravApplication.getInstance().getPrefs().setName(userInfo.getName());
                        Log.v(TAG, " isConnecting -----requestData  ");
                        if(KalravApplication.getInstance().isConnectedToInternet()){
                            if(KalravApplication.getInstance().isServerReachable(LoginActivity.this)){
                                if(userInfo.getEmail()!=null && userInfo.getEmail().length()>0){
                                    String alReadyExsistUser=properties.getProperty(Constants.API_URL_USER.post_userdetails_already_exist_url.name());
                                    callAPI.alReadyExsistUser(userInfo, fb_id, google_id,alReadyExsistUser,seatAuth, LoginActivity.this);
                                    finish();
                                }
                            }
                            else{
                                KalravApplication.getInstance().showDialog(LoginActivity.this,getString(R.string.unknown_error));
                            }
                        }
                        else{
                            KalravApplication.getInstance().buildDialog(LoginActivity.this);
                        }


                    }
                    else {
                        Log.e(TAG, "Could not retrieve data from facebook graphapi...");
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "Facebook facebook JSONException..."+e);

                }
            }



        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email");
        request.setParameters(parameters);
        request.executeAsync();
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
            case R.id.textSkipLogin:
//                KalravApplication.getInstance().getPrefs().setIsLogin(true);
                Intent intent =new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            default:
                return;
        }
    }

    /**
     * Signin method with GoogleSignInApi
     */
    private void signIn() {
        Log.v(TAG, " isConnecting -----signIn  ");

        if(KalravApplication.getInstance().isConnectedToInternet()) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else{
            KalravApplication.getInstance().buildDialog(LoginActivity.this).show();
        }
    }


    //After redirecting from current activity (LoginActivity) to Google OAuth
    //or facebook OAuth after authentication it will call this method immediately
    //Based on the request code it will call the activity either google or facebook

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "firebaseAuthWithGoogle: onActivityResult" );

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "mCallbackManager onActivityResult requestCode ==" +requestCode+
                " resultCode ==== "+resultCode+" data === "+data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode==100) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "firebaseAuthWithGoogle: onActivityResult result "+result );
            Log.d(TAG, "firebaseAuthWithGoogle: onActivityResult result.isSuccess() "+result.isSuccess() );

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase

                Log.d(TAG, "firebaseAuthWithGoogle: onActivityResult result.getSignInAccount() "+result.getSignInAccount() );
                GoogleSignInAccount account = result.getSignInAccount();
                Log.d(TAG, "firebaseAuthWithGoogle: onActivityResult account "+account );

                firebaseAuthWithGoogle(account);

            } else {
                // Signed out, show unauthenticated UI.
//                updateUI(false);
              /*  // Google Sign In failed, update UI appropriately
                DialogFragment dialog = new NetworkStatusDialog();
                Bundle args = new Bundle();
                  *//*  args.putString("title", getString(R.string.text_google_error_title));
                    args.putString("message", getString(R.string.text_google_error_msg));*//*
                dialog.setArguments(args);
                dialog.setTargetFragment(dialog, 100);
                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment TAG");
                return;*/

            }
        }

    }



    /**
     * This method is called from onActvityResult#GoogleSignInResult
     *
     * @param acct
     */
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle: acct.getEmail -=====" + acct.getEmail());
        Log.d(TAG, "firebaseAuthWithGoogle: acct.getIdToken -=====" + acct.getIdToken());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:Google:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.authentication_failed),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            final UserInfo userInfo = new UserInfo();


                            try {
                                if( mAuth.getCurrentUser().getPhotoUrl()!=null) {
                                    KalravApplication.getInstance().getPrefs().setImage(String.valueOf(mAuth.getCurrentUser().getPhotoUrl()));
                                    KalravApplication.getInstance().getPrefs().setIsFacebookLogin(false);
                                }

                                if( mAuth.getCurrentUser().getUid()!=null) {
                                    userInfo.setGoogle_id(mAuth.getCurrentUser().getUid());
                                }
                                if( mAuth.getCurrentUser().getEmail()!=null) {
                                    userInfo.setEmail(mAuth.getCurrentUser().getEmail());
                                    KalravApplication.getInstance().getPrefs().setEmail(mAuth.getCurrentUser().getEmail());
                                }
                                if( mAuth.getCurrentUser().getDisplayName()!=null) {
                                    userInfo.setName(mAuth.getCurrentUser().getDisplayName());
                                    KalravApplication.getInstance().getPrefs().setName(mAuth.getCurrentUser().getDisplayName());
                                }
                                userInfo.setLoggedIn(Constants.LOG_IN);
                                Log.v(TAG, " isConnecting -----firebaseAuthWithGoogle  ");

                                if(KalravApplication.getInstance().isConnectedToInternet()){
                                    if(KalravApplication.getInstance().isServerReachable(LoginActivity.this)){
                                        if(userInfo.getEmail()!=null && userInfo.getEmail().length()>0 && KalravApplication.getInstance().getPrefs().getIsLogin()==true){
                                            String alReadyExsistUser=properties.getProperty(Constants.API_URL_USER.post_userdetails_already_exist_url.name());
                                            callAPI.alReadyExsistUser(userInfo, fb_id, google_id,alReadyExsistUser,seatAuth,LoginActivity.this);
                                        }
                                    }
                                    else{
                                        KalravApplication.getInstance().showDialog(LoginActivity.this,getString(R.string.unknown_error));
                                    }
                                }
                                else{
                                    KalravApplication.getInstance().buildDialog(LoginActivity.this);
                                }

                            } catch (Exception e) {
                                Log.d(TAG, "firebaseAuthWithGoogle :acct Exception ..."+e);

                            }
                        }
                    }
                });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Unresolved Error occurred....", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        super.onStart();
        startTimer();
        if(mAuthListener!=null)
            mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

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
                        Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Mirza-Regular.ttf");
                        if(hTextView!=null) {
                            hTextView.setTypeface(myTypeFace);
                            hTextView.setAnimateType(HTextViewType.TYPER);
                            hTextView.animateText(getBaseContext().getString(R.string.company_name)); // animate
                        }// be sure to set custom typeface before setting the animate type, otherwise the font may not be updated.
                    }
                });
            }
        };
    }
}
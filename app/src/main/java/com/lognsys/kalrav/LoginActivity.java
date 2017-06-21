package com.lognsys.kalrav;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.lognsys.kalrav.fragment.DramaFragment;
import com.lognsys.kalrav.model.DramaInfo;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.FontManager;
import com.lognsys.kalrav.util.KalravApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.lognsys.kalrav.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lognsys.kalrav.util.PropertyReader;

import static android.R.attr.bitmap;
import static com.lognsys.kalrav.R.id.imageView;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    //faceboook callback manager
    private CallbackManager mCallbackManager;
    private static final int RC_SIGN_IN = 100;
    private static final int RC_NETWORK_DIALOG = 101;
    Timer timer;
    TimerTask timerTask;
    TextView textSkipLogin;
    ProgressDialog progressDialog;

    //login_activity UI variable
    private ImageView fbSignIn, googSignIn;
    private LoginButton loginButton;
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    private HTextView hTextView;
    Handler mHtHandler;
    Handler mUiHandler;

    //firebase variable declaration
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  String facebookImageUrl;

    //Google API variable
    private GoogleApiClient mGoogleApiClient;


    //Shared preference variables;
    private String fb_id,google_id,email = "";
    private boolean login_status = false;
    private String oauthId = "";
    private String device_token_id = "";
    UserInfoDAOImpl userDaoImpl;
    SharedPreferences sharedpreferences;



    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "kalrav_android.properties";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating database sqlite and storing user pojo
        SQLiteHelper database = new SQLiteHelper(this);
        userDaoImpl = new UserInfoDAOImpl(this);
        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);
        //Instantiating global obj
        // globalObj = ((KalravApplication) getApplicationContext());

        //LOGIN MODULE Step 1: Check if last user loggedIN

       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.lognsys.kalrav.LoginActivity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:","KeyHash "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:","KeyHash PackageManager.NameNotFoundException "+ e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:","KeyHash NoSuchAlgorithmException "+ e);

        }*/



        UserInfo user = userDaoImpl.lastUserLoggedIn();
        Log.d(TAG, "CASE1:  KalravApplication.getInstance().getPrefs().getIsLogin()..."+ KalravApplication.getInstance().getPrefs().getIsLogin());
        Log.d(TAG, "CASE1:  user..."+ user);

        if (null != user && KalravApplication.getInstance().getPrefs().getIsLogin()) {
            Log.d(TAG, "CASE1: User Exists in database.. Setting global object...");
            if(KalravApplication.getInstance().getPrefs().getCustomer_id()!=null){
                //setting global variable
                KalravApplication.getInstance().setGlobalUserObject(user);
                KalravApplication.getInstance().invokeService(getApplicationContext());
                Log.d(TAG, "OnCreate method - User Exists in DB. " + user.toString());
                if(user.getEmail()!=null && user.getEmail().length()>0){
                    docallApi(user);

                }

//                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
            }
        }
        else {
            Log.d(TAG, "Case2: OnCreate method - Login through Facebook Auth and saving to database.");
            //Initialize Facebook sdk
            FacebookSdk.sdkInitialize(getApplicationContext());
            mCallbackManager = CallbackManager.Factory.create();

            //setting layout activity_login
            setContentView(R.layout.activity_login);

            textSkipLogin=(TextView)findViewById(R.id.textSkipLogin);
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
                public void onSuccess(LoginResult loginResult) {
                   Toast.makeText(getApplicationContext(),  "User ID: "
                           + loginResult.getAccessToken().getUserId()
                           + "\n" +
                           "Auth Token: "
                           + loginResult.getAccessToken().getToken(),Toast.LENGTH_LONG).show();

                    Log.v(TAG, " Facebook API requestData - ");
                    requestData(loginResult);
                    Log.d(TAG, "mCallbackManager loginResult ==" +loginResult);
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(),  "Login attempt canceled.",Toast.LENGTH_LONG).show();

                    Log.d(TAG, "mCallbackManager onCancel ==" );
                }

                @Override
                public void onError(FacebookException e) {
                    Log.d(TAG, "mCallbackManager FacebookException ==" +e);

                    Toast.makeText(getApplicationContext(),  "Login attempt failed.",Toast.LENGTH_LONG).show();

                }
            });
/*
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    requestData();
                }
                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_cancel), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException e) {

                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                   */
/* Log.e(TAG,"Exception isAppInstalled " +isAppInstalled(getApplicationContext(), "com.facebook.katana"));
                    if(isAppInstalled(getApplicationContext(), "com.facebook.katana")) {
                        // Do something
                        Intent receiverIntent = new Intent(Intent.ACTION_VIEW);
                        receiverIntent.setClassName("com.facebook.katana","");
                        startActivity(receiverIntent);
                    }else {
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana"));
                        startActivity(i);
                    }*//*

                    //log facebook Error
                    Log.e(TAG,"Exception" +e);
                }
            });




*/

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

            /*********************************FIREBASE************************************/

            //Initialize FirebaseAuth
            //You need to include google-services.json (downloaded from firebase console) file under the "app" folder of this project.
            mAuth = FirebaseAuth.getInstance();
            Log.d(TAG, "onAuthStateChanged:mAuth: ============================= " + mAuth);

            // firebase authentication listener will chek if user is authenticated.
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    Log.d(TAG, "onAuthStateChanged:firebaseAuth:============================= " + firebaseAuth+" firebaseAuth.getCurrentUser()getEmail:" +firebaseAuth.getCurrentUser());

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "onAuthStateChanged:user:" +user);
                    if (user != null) {

                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid()+" Provider:" + user.getProviderId());
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid()+" Provider:" + user.getProviderId());

                        KalravApplication.getInstance().getPrefs().setIsLogin(true);
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                }
            };
        }
    }



    private void requestData(final LoginResult loginResult) {

        final UserInfo userInfo = new UserInfo();

        final GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.v(TAG, " Facebook API response - " + response.toString());

                JSONObject json = response.getJSONObject();

                try {
                    if (json != null) {
                        Log.v(TAG, " Facebook API response id- " + json.getString("id"));

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

                            if(userInfo.getEmail()!=null && userInfo.getEmail().length()>0){
                                docallApi(userInfo);

                            }

//                        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
//                        i.putExtra("userInfos",userInfos);
//                        startActivity(i);
//                        finish();
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
                KalravApplication.getInstance().getPrefs().setIsLogin(true);
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
        Log.d(TAG, "firebaseAuthWithGoogle: signIn mGoogleApiClient " +mGoogleApiClient);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                                Log.d(TAG, "firebaseAuthWithGoogle: acct  mAuth.getCurrentUser().getPhotoUrl() -=====" +  mAuth.getCurrentUser().getPhotoUrl());
                                Log.d(TAG, "firebaseAuthWithGoogle: acct  mAuth.getCurrentUser().getUid() -=====" +  mAuth.getCurrentUser().getUid());
                                Log.d(TAG, "firebaseAuthWithGoogle: acct  mAuth.getCurrentUser().getEmail() -=====" +  mAuth.getCurrentUser().getEmail());
                                Log.d(TAG, "firebaseAuthWithGoogle: acct  mAuth.getCurrentUser().getDisplayName() -=====" +  mAuth.getCurrentUser().getDisplayName());
                                Log.d(TAG, "firebaseAuthWithGoogle: acct  mAuth.getCurrentUser() -=====" + mAuth.getCurrentUser());
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


                                if(userInfo.getEmail()!=null && userInfo.getEmail().length()>0){
                                    docallApi(userInfo);

                                }

                                  } catch (Exception e) {
                                        Log.d(TAG, "firebaseAuthWithGoogle :acct Exception ..."+e);

                                    }
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
                        Log.d(TAG, "signInWithCredential:onComplete Facebook:  mAuth.getCurrentUser().getUid() " +  mAuth.getCurrentUser().getUid());

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
                Log.v(TAG, " Facebook API response.getJSONObject() - " +response.getJSONObject());

                JSONObject json = response.getJSONObject();

                try {
                    if (json != null) {
                        String s = json.getString("name");
                        Log.v(TAG, " Facebook API response name - " + s);

                    }
                } catch (JSONException e) {
                    Log.v(TAG, " Facebook API JSONException - " +e);

                }
            }

        });
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

//                        hTextView.setTypeface(FontManager.getInstance(getApplicationContext().getAssets()).getFont("fonts/Mirza-Regular.ttf"));
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

    private void docallApi(UserInfo userInfo) {
//        Log.d(TAG, "Google docallApi userInfo.getEmail()..."+userInfo.getEmail());
        Log.d(TAG, "Google docallApi email................."+userInfo.getEmail());

        String alReadyExsistUser=properties.getProperty(Constants.API_URL_USER.get_userdetails_already_exist_url.name())+userInfo.getEmail();
        Log.d(TAG, "Google docallApi alReadyExsistUser..."+alReadyExsistUser);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,alReadyExsistUser,
                null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                                                   if (jsonObject != null) {

                                                            UserInfo  userInfo = new UserInfo();
                                                            userInfo.setId(jsonObject.getInt("id"));
                                                            userInfo.setName(jsonObject.getString("realname"));
                                                            userInfo.setEmail(jsonObject.getString("username"));
                                                            if (fb_id != null){
                                                                userInfo.setFb_id(fb_id);
                                                                KalravApplication.getInstance().getPrefs().setUser_id(fb_id);
                                                            }
                                                            if (google_id != null) {
                                                                userInfo.setGoogle_id(google_id);
                                                                KalravApplication.getInstance().getPrefs().setUser_id(google_id);
                                                            }
                                                            userInfo.setPhoneNo(jsonObject.getString("phone"));
                                                            userInfo.setAddress(jsonObject.getString("address"));
                                                            userInfo.setCity(jsonObject.getString("city"));
                                                            userInfo.setState(jsonObject.getString("state"));
                                                            userInfo.setZipcode(jsonObject.getString("zipcode"));
                                                          userInfo.setGroupname(jsonObject.getString("group"));
                                                          userInfo.setLoggedIn(Constants.LOG_IN);
//                                                            //save to the database
                                                          KalravApplication.getInstance().getPrefs().setUser_Group_Name(userInfo.getGroupname());
                                                          KalravApplication.getInstance().getPrefs().setEmail(userInfo.getEmail());

                                                          KalravApplication.getInstance().getPrefs().setCustomer_id(String.valueOf(userInfo.getId()));
                                                            KalravApplication.getInstance().setGlobalUserObject(userInfo);
                                                            Log.d("", "Global object Reg " + KalravApplication.getInstance().getGlobalUserObject());
                                                            userDaoImpl.addUser(userInfo);
                                                            KalravApplication.getInstance().getPrefs().setIsLogin(true);

                                                           Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                                            startActivity(i);
                                                            finish();

                                                        }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("","JSonException docallApi Exception "+e);

                    Toast.makeText(getApplicationContext(),
                            getString(R.string.no_data_available),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("","Google docallApi Error: volly Exception " + error);
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
                KalravApplication.getInstance().getPrefs().hidepDialog(getApplicationContext());
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KalravApplication.getInstance().addToRequestQueue(req);

    }
}


package com.lognsys.kalrav;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.fragment.DramaFragment;
import com.lognsys.kalrav.fragment.MyTicketFragment;
import com.lognsys.kalrav.fragment.NotificationFragment;
import com.lognsys.kalrav.fragment.PhotoFragment;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.CircleTransform;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;

import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile, shareImage;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private GoogleApiClient mGoogleApiClient;
    // urls to load navigation header background image
    // and profile image
   // private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
   // private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_DRAMA = "drama";
    private static final String TAG_PHOTO = "photo";
    private static final String TAG_BOOKMARK = "bookmark";
    private static final String TAG_NOTIFICATIONS = "notification";
//    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_DRAMA;

    private KalravApplication globalObj;
    private UserInfoDAOImpl userDaoImpl;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private static String profile_photo_url="https://graph.facebook.com/fb_id/picture?type=normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        //Instantiating global obj
        globalObj = ((KalravApplication) getApplicationContext());
        userDaoImpl = new UserInfoDAOImpl(this);


        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        TextView textHeaderName = (TextView) navHeader.findViewById(R.id.textHeaderName);
        if(KalravApplication.getInstance().getPrefs().getName()!=null)
        textHeaderName.setText("Welcome \n "+KalravApplication.getInstance().getPrefs().getName());
        else{
            textHeaderName.setText("Guest");

        }
//        txtName = (TextView) navHeader.findViewById(R.id.name);
        //txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
//        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        if(KalravApplication.getInstance().getPrefs().getIsFacebookLogin()==true){
            if(KalravApplication.getInstance().getPrefs().getImage()!=null){
                String imgUrl = "https://graph.facebook.com/" + KalravApplication.getInstance().getPrefs().getImage() + "/picture?type=large";
                Picasso.with(getApplicationContext()).load(imgUrl).into(imgNavHeaderBg);
            }
        }
        else{
            if(KalravApplication.getInstance().getPrefs().getImage()!=null){
                Picasso.with(getApplicationContext()).load(KalravApplication.getInstance().getPrefs().getImage()).into(imgNavHeaderBg);
            }
        }
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load nav menu header data
//        loadNavHeader();
        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DRAMA;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {


        // name, website
       UserInfo user = globalObj.getGlobalUserObject();

       // profile_photo_url = profile_photo_url.replace("fb_id", user.getFb_id());

        txtName.setText(user.getName());
        //txtWebsite.setText("www.androidhive.info");

        // loading header background image
        Glide.with(this).load(R.drawable.ic_share)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(profile_photo_url).placeholder(R.drawable.person_placeholder)
                .crossFade()
                .thumbnail(1)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

//        // showing dot next to notifications label
//        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG).addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();


            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        //invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Drama
                DramaFragment dramaFragment = new DramaFragment();

                return dramaFragment;
            case 1:
                // notification
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;
            case 2:
                // photos fragment
                PhotoFragment photoFragment = new PhotoFragment();

                return  photoFragment;
            default:
                return new DramaFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment fragment=null;
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_drama:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_DRAMA;
                        fragment = new DramaFragment();


                        HomeActivity.this.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_bookmark:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_BOOKMARK;
                        fragment = new BookmarkFragment();
                        HomeActivity.this.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                        break;
//                    case R.id.nav_settings:
//                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_SETTINGS;
//                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(HomeActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.logout :
                        KalravApplication.getInstance().getPrefs().setIsLogin(false);
                        FirebaseAuth.getInstance().signOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient=null;
                        KalravApplication.getInstance().getPrefs().getClear();
                        Log.d("","Logout getGlobalUserObject() "+KalravApplication.getInstance().getGlobalUserObject());
//                      retrieve userinfo obj from global object
                        if(KalravApplication.getInstance().getGlobalUserObject()!=null){
                            UserInfo userInfo = KalravApplication.getInstance().getGlobalUserObject();
                            Log.d("","Logout userInfo "+userInfo);
                            if(userInfo!=null){
                                Log.d("","Logout userInfo Constants.LOG_OUT "+Constants.LOG_OUT);
                                userInfo.setLoggedIn(Constants.LOG_OUT);
                                Log.d("","Logout userInfo userInfo.getLoggedIn() "+userInfo.getLoggedIn());
                                if(userDaoImpl!=null){
                                    Log.d("","Logout userDaoImpl "+userDaoImpl);
                                    userDaoImpl.logOut(userInfo);
                                    Log.d("","Logout userDaoImpl lastUserLoggedIn "+userDaoImpl.lastUserLoggedIn());
                                    Log.d("","Logout userDaoImpl toString "+userDaoImpl.toString());
                                    KalravApplication.getInstance().setGlobalUserObject(userInfo);
                                }
                            }

                        }
                        //set log_out in database
//                        userDaoImpl.logOut(u); //pass UserInfo object

                        //set global Object to null
//                        u = null;
//                        KalravApplication.getInstance().setGlobalUserObject(u);
                        LoginManager.getInstance().logOut();
                        // finish();
                        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();

                        return true;
                    case R.id.my_ticket :
                        fragment = new MyTicketFragment();


                    HomeActivity.this.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_DRAMA;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.menu_home, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
            switch (item.getItemId()) {
            case R.id.logout:
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
/*
              UserInfo u = KalravApplication.getInstance().getGlobalUserObject(); //retrieve userinfo obj from global object
                u.setLoggedIn(Constants.LOG_OUT);

                //set log_out in database
                userDaoImpl.logOut(u); //pass UserInfo object

                //set global Object to null
                u = null;
                KalravApplication.getInstance().setGlobalUserObject(u);
                LoginManager.getInstance().logOut();

                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);*/
                return true;

        }


        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }
    private void shareIt() {
//sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void switchContent(Fragment fff) {
        Fragment fragment = fff;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
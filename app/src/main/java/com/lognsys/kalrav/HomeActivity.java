package com.lognsys.kalrav;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.lognsys.kalrav.db.SQLiteHelper;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.fragment.DramaFragment;
import com.lognsys.kalrav.fragment.NotificationFragment;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private static final String TITLE_DRAMA = "DRAMA";
    private static final String TITLE_NEWS = "NEWS";
    private KalravApplication globalObj;
    private UserInfoDAOImpl userDaoImpl;

    /**
     *
     */
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.mipmap.ic_launcher_theatre,
            R.mipmap.ic_launcher_notif
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize Facebook

        FacebookSdk.sdkInitialize(getApplicationContext());

        //Instantiating global obj
        globalObj = ((KalravApplication) getApplicationContext());
        userDaoImpl = new UserInfoDAOImpl(this);


        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set viewpager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    /**
     * Get Icon values
     */
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }


    /**
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DramaFragment(), TITLE_DRAMA);
        adapter.addFragment(new NotificationFragment(), TITLE_NEWS);
        viewPager.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                UserInfo u = globalObj.getGlobalObject(); //retrieve userinfo obj from global object
                u.setLoggedIn(Constants.LOG_OUT);

                //set log_out in database
                userDaoImpl.logOut(u); //pass UserInfo object

                //set global Object to null
                u = null;
                globalObj.setGlobalObject(u);
                LoginManager.getInstance().logOut();

                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                break;

        }
        return true;
    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}

package com.example.bassam.sporstincmanger.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Aaa_data.GlobalVars;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.Entities.UserEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.NavigationDrawer_Fragments.*;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.TabsFragments.NotificationsReceivedFragment;
import com.example.bassam.sporstincmanger.app.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getSimpleName();

    GlobalVars globalVars;

    boolean doubleBackToExitPressedOnce = false;
    public static FragmentManager fragmentManager;
    public NavigationView navigationView;
    private ActionBar actionBar;
    private DrawerLayout drawer;
    private boolean isPickerShown = false;
    private TextView userName , userPhone;
    private ImageView profileImage;
    private int PROFILE_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        globalVars = (GlobalVars) getApplication();

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = homeFragment.class;
            int Notifi = getIntent().getIntExtra("HomePosition",0);

            if(Notifi == Config.NOTIFICATION_ID)
                fragmentClass = NotificationsReceivedFragment.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        actionBar = getSupportActionBar();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        RelativeLayout header = (RelativeLayout) navigationView.getHeaderView(0);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.drawable.rsz_logo_white2);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        profileImage = header.findViewById(R.id.profile_image);

        userName = header.findViewById(R.id.user_name);
        userPhone = header.findViewById(R.id.user_phone);
        userName.setText(globalVars.getName());
        userPhone.setText(globalVars.getPhone());

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });

        userPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });

        displayFirebaseRegId();

       // set Unread Notification Counter
       // setMenuCounter(5);

    }

    private void openProfile (){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        drawer.closeDrawer(GravityCompat.START);
        startActivityForResult(intent ,PROFILE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_CODE && resultCode == AppCompatActivity.RESULT_OK){
            userName.setText(globalVars.getName());
            userPhone.setText(globalVars.getPhone());
            String ImageUrl = globalVars.getImgUrl();
            if (ImageUrl != null && !ImageUrl.equals("")){
                Picasso.with(getApplicationContext()).load(Constants.profile_host + ImageUrl).into(profileImage);
            }
            else
                profileImage.setImageResource(R.mipmap.ic_profile_round);
        }

    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.d(TAG, "Firebase reg id: " + regId);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.press_again, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        actionBar.setTitle(R.string.app_name);

        Fragment fragment = null;
        Class fragmentClass = homeFragment.class;

        if (id == R.id.home) {
            fragmentClass = homeFragment.class;
        } else if (id == R.id.classes) {
            fragmentClass = classesFragment.class;
        } else if (id == R.id.notifications) {
            actionBar.setTitle(R.string.notifi);
            fragmentClass = NotificationsFragment.class;
        }else if (id == R.id.requests) {
            actionBar.setTitle(R.string.request);
            fragmentClass = RequestsFragment.class;
        } else if (id == R.id.reports) {
            actionBar.setTitle(R.string.report);
            fragmentClass = reportsFragment.class;
        }else if(id == R.id.link){
            // Link To the Acadmey
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thesportsinc.com/"));
            startActivity(browserIntent);
        }
        else if (id == R.id.nav_signout) {
            // LogOut From the System
            unActiveUser(globalVars.getId());
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void unActiveUser(int user_id) {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("id",user_id);

            JSONObject values = new JSONObject();
            values.put("active",0);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("values",values.toString());
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    logOut(response);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void logOut(JSONArray response) {
        if(response == null) {
            show_toast("fail to logout");
            return;
        }
        try {
            String result = String.valueOf(response.get(0));
            Log.d(TAG, result);
            if (result.equals("ERROR")) {
                show_toast("fail to logout");
                return;
            }
            SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
            preferences.clear();
            preferences.apply();

            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void show_toast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private void setMenuCounter(int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(R.id.notifications).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }
}

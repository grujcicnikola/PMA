package com.example.pawfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import com.example.pawfinder.activity.BarCodeActivity;
import com.example.pawfinder.activity.LoginActivity;
import com.example.pawfinder.activity.MissingReportFirstPage;
import com.example.pawfinder.adapters.ViewPagerAdapter;
import com.example.pawfinder.activity.PreferenceActivity;
import com.example.pawfinder.tools.LocaleUtils;
import com.example.pawfinder.tools.ThemeUtils;
import com.example.pawfinder.tools.PrefConfig;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements   SharedPreferences.OnSharedPreferenceChangeListener {


    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences sharedPreferences;
    private LocaleUtils localeUtils;
    private ThemeUtils themeUtils;
    private static PrefConfig prefConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }

        setContentView(R.layout.activity_main);
        prefConfig = new PrefConfig(this);


        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        viewPager = findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();         //da selektovan bude Missing

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // set item as selected to persist highlight
                Intent i;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_qr_code:
                        i = new Intent(getApplicationContext(), BarCodeActivity.class);
                        startActivity(i);
                        break;

                    case R.id.navigation_item_item:
                        Intent missingReport = new Intent(getApplicationContext(), MissingReportFirstPage.class);
                        startActivity(missingReport);
                        break;

                    case R.id.navigation_item_settings:
                        i = new Intent(getApplicationContext(), PreferenceActivity.class);
                        startActivity(i);
                        break;

                    case R.id.navigation_item_logout:
                        prefConfig.logout();
                        Toast.makeText(MainActivity.this, "User successfully logged out", Toast.LENGTH_SHORT).show();
                        i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        break;

                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                //Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name){
            //setovanje email-a ulogovanog korisnika
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView user_drawer = (TextView) findViewById(R.id.drawer_user);
                if(prefConfig.readLoginStatus())
                {
                    user_drawer.setText(prefConfig.readUserEmail());
                }

                invalidateOptionsMenu();
            }

        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                Intent i = new Intent(getApplicationContext(), PreferenceActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }




    public void setupSharedPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        localeUtils = new LocaleUtils(sharedPreferences,this);
        localeUtils.setLocale();
        themeUtils = new ThemeUtils(sharedPreferences, this);
        themeUtils.setTheme();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "language": {
                localeUtils.setLocale();
                finish();
                startActivity(getIntent());
                break;
            }
            case "theme":{
                themeUtils.setTheme();
                //setTheme(R.style.darktheme);
                finish();
                startActivity(getIntent());
                break;
                }
            }
        }
    }




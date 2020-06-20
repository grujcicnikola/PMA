package com.example.pawfinder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.pawfinder.activity.BarCodeActivity;
import com.example.pawfinder.activity.LoginActivity;
import com.example.pawfinder.activity.MissingReportFirstPage;
import com.example.pawfinder.activity.ViewCommentsActivity;
import com.example.pawfinder.adapters.PetsListAdapter;
import com.example.pawfinder.adapters.ViewPagerAdapter;
import com.example.pawfinder.activity.PreferenceActivity;
import com.example.pawfinder.db.DBContentProvider;
import com.example.pawfinder.fragments.MissingFragment;
import com.example.pawfinder.fragments.NearYouFragment;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.Comment;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.CommentService;
import com.example.pawfinder.service.MyFirebaseInstanceService;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.sync.PetSqlSync;
import com.example.pawfinder.tools.LocaleUtils;
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.NotificationUtils;
import com.example.pawfinder.tools.ThemeUtils;
import com.example.pawfinder.tools.PrefConfig;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hossamscott.com.github.backgroundservice.RunService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences sharedPreferences;
    private LocaleUtils localeUtils;
    private ThemeUtils themeUtils;
    private NotificationUtils notificationUtils;
    private static PrefConfig prefConfig;
    private MainActivity mainActivity;
    public static Integer nearYouRange;
    private SeekBar np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.d("TOKEN",token);
            }
        });

        prefConfig = new PrefConfig(this);
        setupSharedPreferences();
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }

        nearYouRange = 10;

        setContentView(R.layout.activity_main);
        prefConfig = new PrefConfig(this);

        mainActivity = this;
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if (position == 1) {
                    Log.i("fragment", "missing");
                    startService();
                } else {
                    Log.i("fragment", "ostalo");
                    if (alarm_receiver != null) {
                        try {
                            unregisterReceiver(alarm_receiver);
                        } catch (Exception e) {
                            Log.d("alarm", "broadcast");
                        }

                    }
                }
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        /*Menu menuNav = navigationView.getMenu();
        MenuItem mi = menuNav.findItem(R.id.item_number);
        np = (SeekBar) mi.getActionView();
        /*np.setMinValue(2);
        np.setMaxValue(20);
        np.setOnValueChangedListener(onValueChangeListener);*/
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

                    case R.id.navigation_item_range:
                        showNumberPickerDialog();
                        Log.d("RANGENUMBER", String.valueOf(nearYouRange));
                        break;

                    case R.id.navigation_item_logout:
                        prefConfig.logout();
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            //setovanje email-a ulogovanog korisnika
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView user_drawer = (TextView) findViewById(R.id.drawer_user);
                if (prefConfig.readLoginStatus()) {
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


    public void setupSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        localeUtils = new LocaleUtils(sharedPreferences, this);
        localeUtils.setLocale();
        themeUtils = new ThemeUtils(sharedPreferences, this);
        themeUtils.setTheme();
        notificationUtils = new NotificationUtils(sharedPreferences,this);
        notificationUtils.setNotification();
        String token = MyFirebaseInstanceService.getToken(this);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token) {
        // TODO: Implement this method to send token to your app server.


        User user = new User();

        if (prefConfig.readUserEmail()!=null) {
            user.setEmail(prefConfig.readUserEmail());
            user.setToken(token);
            final Call<ResponseBody> call = ServiceUtils.userService.token(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        Log.i("TOKENFIREBASE","send");
                    } else if (response.code() == 400) {
                        Log.i("TOKENFIREBASE","error");
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("TOKENFIREBASE","error");
                }
            });

        }

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
            case "theme": {
                themeUtils.setTheme();
                //setTheme(R.style.darktheme);
                finish();
                startActivity(getIntent());
                break;
            }
            case "notification": {
                notificationUtils.setNotification();

                break;
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ReactiveNetwork
                .observeNetworkConnectivity(getApplicationContext())
                .flatMapSingle(connectivity -> ReactiveNetwork.checkInternetConnectivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(final Disposable d) {
                        // this will be invoked before operation is started
                    }

                    @Override
                    public void onNext(final Boolean isConnectedToInternet) {
                        // do your action, when you're connected to the internet
                        if (isConnectedToInternet == true) {
                            Log.d("ISCONNECTED", "Meesage recieved");
                            PetSqlSync.sendUnsaved(mainActivity);
                            Boolean notification = sharedPreferences.getBoolean("notification", true);
                            if(notification){
                                String token = MyFirebaseInstanceService.getToken(getApplicationContext());
                                sendTokenToServer(token);
                            }else{
                                sendTokenToServer("");
                            }
                        }
                    }

                    @Override
                    public void onError(final Throwable e) {
                        // handle an error here <-----------------
                        //Snackbar.make(getView(), "Network timeout!", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        // this will be invoked when operation is completed
                    }

                });


        startService();
    }


    public void startService() {
        IntentFilter intentFilter = new IntentFilter("alaram_received");
        registerReceiver(alarm_receiver, intentFilter);
        RunService repeat = new RunService(this);

        repeat.call(5, true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (alarm_receiver != null) {
                try {
                    unregisterReceiver(alarm_receiver);
                } catch (Exception e) {

                }

            }
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }

    }

    public final BroadcastReceiver alarm_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // your logic here
            Log.i("alarm_received", "logic");

            int status = NetworkTool.getConnectivityStatus(getApplicationContext());
            if (status != NetworkTool.TYPE_NOT_CONNECTED) {
                Log.i("alarm_received", "success");
                //Log.i("stanje", String.valueOf(MissingFragment.pets.size()));
                //MissingFragment.updatelist();
                //getUnseenComments();
                final Call<List<Pet>> call = ServiceUtils.petService.getAll();
                call.enqueue(new Callback<List<Pet>>() {
                    @Override
                    public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                        // Log.d("Dobijeno", response.body().toString());
                        //Log.d("BROJ", "ima ih" + response.body().size());

                        MissingFragment.pets = response.body();
                        MissingFragment.adapter.updateResults(MissingFragment.pets);
                        PetSqlSync.fillDatabase((ArrayList<Pet>) MissingFragment.pets, mainActivity, 0);
                    }

                    @Override
                    public void onFailure(Call<List<Pet>> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                    }
                });
            } else {
                Log.i("alarm_received", "not connected to internet");
            }

        }
    };


    NumberPicker.OnValueChangeListener onValueChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(MainActivity.this,
                            "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT);
                    nearYouRange = numberPicker.getValue();
                }
            };



    public void showNumberPickerDialog(){
        NumberPicker picker = new NumberPicker(MainActivity.this);
        picker.setMinValue(1);
        picker.setMaxValue(50);

        FrameLayout layout = new FrameLayout(MainActivity.this);
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getText(R.string.range_title))
                .setView(layout)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // do something with picker.getValue()
                    picker.getValue();
                    nearYouRange = picker.getValue();

                    Log.d("ONRESUME", "tu sam");
                    tabLayout.getTabAt(1).select();
                    tabLayout.getTabAt(0).select();
                   /* NearYouFragment frg = (NearYouFragment) viewPagerAdapter.getItem(0);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.attach(frg);
                    ft.commit();*/
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    public Integer getNearYouRange() {
        return nearYouRange;
    }
}


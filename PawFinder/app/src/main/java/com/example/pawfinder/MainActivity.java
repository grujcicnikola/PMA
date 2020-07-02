package com.example.pawfinder;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.pawfinder.activity.BarCodeActivity;
import com.example.pawfinder.activity.ChangePasswordActivity;
import com.example.pawfinder.activity.LoginActivity;
import com.example.pawfinder.activity.MissingReportFirstPage;
import com.example.pawfinder.adapters.ViewPagerAdapter;
import com.example.pawfinder.activity.PreferenceActivity;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.MyFirebaseInstanceService;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.sync.SyncReceiver;
import com.example.pawfinder.tools.LocaleUtils;
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.NotificationUtils;
import com.example.pawfinder.tools.RangeUtils;
import com.example.pawfinder.tools.ThemeUtils;
import com.example.pawfinder.tools.PrefConfig;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
    private RangeUtils rangeUtils;
    private SeekBar np;
    public BroadcastReceiver alarm_receiver;
    private TextView user_drawer;
    private GoogleSignInClient googleClient;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.d("TOKENMOJ",token);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);

        prefConfig = new PrefConfig(this);
        setupSharedPreferences();
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }

        nearYouRange = 10;

        setContentView(R.layout.activity_main);

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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
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
                        if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                            Intent missingReport = new Intent(getApplicationContext(), MissingReportFirstPage.class);
                            startActivity(missingReport);
                        }else{
                            Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.navigation_item_change_password:
                        if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                            i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                            //i.putExtra("calling-activity", ActivityConstants.ACTIVITY_MAIN);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                        }
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

                        if(prefConfig.readUserGoogleStatus())
                        {
                            googleClient.signOut();
                        }

                        sendTokenToServer("");//problem ako nema neta
                        user_drawer.setText("");

                        Toast.makeText(MainActivity.this, getText(R.string.logout_success), Toast.LENGTH_SHORT).show();
                        i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finishAffinity();
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
                user_drawer = (TextView) findViewById(R.id.drawer_user);

                if (prefConfig.readLoginStatus()) {
                    user_drawer.setText(prefConfig.readUserEmail());

                    boolean googleLogin = prefConfig.readUserGoogleStatus();
                    if(!googleLogin)
                    {
                        Menu menuNav = navigationView.getMenu();
                        MenuItem item = menuNav.findItem(R.id.navigation_item_change_password);
                        item.setVisible(true);
                    }
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
        rangeUtils = new RangeUtils(sharedPreferences, this);
        rangeUtils.setRange();

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
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(i);
                //recreate();
                break;
            }
            case "theme": {
                themeUtils.setTheme();
                //setTheme(R.style.darktheme);
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //recreate();
                break;
            }
            case "notification": {
                /*if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                    Log.d("ima", " interneta if");*/
                    notificationUtils.setNotification();
               /* }else{
                    Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                }*/

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
                        Log.d("ISCONNECTED", "onNext");
                        if (isConnectedToInternet == true) {
                            Log.d("ISCONNECTED", "Meesage recieved");
                            //PetSqlSync.sendUnsaved(mainActivity);
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
        alarm_receiver = new SyncReceiver();
        registerReceiver(alarm_receiver, intentFilter);
        RunService repeat = new RunService(this);

        repeat.call(5, true);
    }


    @Override
    protected void onPause() {
        super.onPause();

       unregisterReceiver(alarm_receiver);

    }




    NumberPicker.OnValueChangeListener onValueChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    //Toast.makeText(MainActivity.this,
                    //        "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT);
                    nearYouRange = numberPicker.getValue();
                }
            };



    public void showNumberPickerDialog(){
        NumberPicker picker = new NumberPicker(MainActivity.this);
        picker.setMinValue(1);
        picker.setMaxValue(50);
        picker.setValue(rangeUtils.readRange());

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
                    rangeUtils.saveRange(nearYouRange);

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


package com.example.pawfinder.activity;

import android.os.Bundle;
import android.view.MenuItem;
import com.example.pawfinder.R;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceFragmentCompat;
import com.example.pawfinder.tools.FragmentTransition;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.settings_layout);
        setTitle(R.string.nav_item_settings);
        FragmentTransition.to(PrefsFragment.newInstance(), this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        FragmentTransition.remove(this);
        finish();
    }


    //PreferenceFrаgmentCompаt je ulаznа tаčkа zа korišćenje Preference biblioteke.
//  Ovаj frаgment prikаzuje hijerаrhiju Preference objekаtа premа korisniku
//  Tаkođe rukuje persisting vrednostimа uređаjа.
    public static class PrefsFragment extends PreferenceFragmentCompat {

        private static PrefsFragment newInstance() {
            Bundle args = new Bundle();

            PrefsFragment fragment = new PrefsFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preference);
        }


    }
}

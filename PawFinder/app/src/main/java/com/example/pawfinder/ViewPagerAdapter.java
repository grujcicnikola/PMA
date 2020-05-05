package com.example.pawfinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.pawfinder.fragments.MissingFragment;
import com.example.pawfinder.fragments.NearYouFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //ne treba preko konstruktora Milos rekao - Ivana izmeni (ovako lik u videu radio purger)
        NearYouFragment nearYouFragment = new NearYouFragment();
        //MissingFragment missingFragment = new MissingFragment();

        Fragment fragment = new Fragment();

        //
        Bundle bundle = new Bundle();
        bundle.putString("message", "Fragment :" + position);

        switch (position) {
            case 0:
                nearYouFragment = new NearYouFragment();
                break;
            case 1:
                fragment = new MissingFragment();
                fragment.setArguments(bundle);
                return fragment;
               // missingFragment = new MissingFragment();
                //break;
            case 2:
                fragment.setArguments(bundle);
                return fragment;

            default:
                break;
        }

        return nearYouFragment;
    }

    @Override
    public int getCount() {
        //koliko tabova imamo
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Near you";
            case 1:
                return "Missing";
            case 2:
                return "My reports";
            default:
                break;
        }

        return "Fragment";
    }
}

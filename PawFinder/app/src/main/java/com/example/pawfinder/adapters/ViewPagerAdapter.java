package com.example.pawfinder.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.pawfinder.fragments.MissingFragment;
import com.example.pawfinder.fragments.MyReportsFragment;
import com.example.pawfinder.fragments.NearYouFragment;
import com.example.pawfinder.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public ViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        NearYouFragment nearYouFragment = null;
        MissingFragment missingFragment = null;
        MyReportsFragment reportsFragment = null;

        Bundle bundle = new Bundle();
        bundle.putString("message", "Fragment :" + position);

        switch (position) {
            case 0:
                nearYouFragment = new NearYouFragment().newInstance();
                return nearYouFragment;
            case 1:
                missingFragment = new MissingFragment().newInstance(bundle);
                return missingFragment;
            //break;
            case 2:
                reportsFragment = new MyReportsFragment().newInstance(bundle);
                return reportsFragment;

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
                return context.getResources().getString(R.string.tab_near);
            case 1:
                return context.getResources().getString(R.string.tab_missing);
            case 2:
                return context.getResources().getString(R.string.tab_reports);
            default:
                break;
        }

        return context.getResources().getString(R.string.tab_fragment);
    }
}

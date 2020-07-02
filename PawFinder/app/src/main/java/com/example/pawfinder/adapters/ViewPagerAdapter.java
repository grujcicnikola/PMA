package com.example.pawfinder.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ReportFragment;

import com.example.pawfinder.fragments.MissingFragment;
import com.example.pawfinder.fragments.MyReportsFragment;
import com.example.pawfinder.fragments.NearYouFragment;
import com.example.pawfinder.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public ViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
       /* Bundle bundle = new Bundle();
        bundle.putString("message", "Fragment :" + position);*/
        NearYouFragment nearYouFragment= null;
        MissingFragment missingFragment = null;
        MyReportsFragment reportsFragment = null;

        switch (position) {
            case 0:
                nearYouFragment = new NearYouFragment().newInstance();
                return nearYouFragment;
            case 1:
                missingFragment = new MissingFragment().newInstance();
                return missingFragment;
            //break;
            case 2:
                reportsFragment = new MyReportsFragment().newInstance();
                return reportsFragment;

            default:
                break;
        }
        return  missingFragment;
        /*if (position == 0) {
            NearYouFragment nearYouFragment = new NearYouFragment().newInstance();
            return nearYouFragment;
        }else if (position == 2){
            MyReportsFragment reportsFragment = new MyReportsFragment().newInstance();
            return reportsFragment;
        }else{ //if(position == 1){
            MissingFragment missingFragment = new MissingFragment().newInstance();
            return missingFragment;
        }*/

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

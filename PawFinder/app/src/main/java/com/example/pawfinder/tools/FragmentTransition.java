package com.example.pawfinder.tools;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.pawfinder.R;

public class FragmentTransition {
    public static void to(Fragment newFragment, FragmentActivity activity) {
        to(newFragment, activity, true);
    }

    //fragmeni ne mogu da postoje nezavisno, lepimo ih na aktivnost
    public static void to(Fragment newFragment, FragmentActivity activity, boolean addToBackstack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction()//zapocinjemo transakciju - lepljenje fragmenata na aktivnosti ide u transakcionom maniru
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//kojom animacijom ce doci do primene transakcije, TRANSIT_FRAGMENT_OPEN - bice dodat na stack
                .replace(R.id.mainContent, newFragment);//dodajemo fragment na layout, tj sa metodom replace menjamo glavni sadrzaj sa fragmentom
        if (addToBackstack)
            transaction.addToBackStack(null);//By calling addToBackStack(), the replace transaction is saved to the back stack so the user can reverse the transaction and bring back the previous fragment by pressing the Back button
        transaction.commit();//ako imamo vise fragmenata, red komitovanja odredjujiu redosled view hijerarhije
    }

    public static void remove(FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStack();

    }
}
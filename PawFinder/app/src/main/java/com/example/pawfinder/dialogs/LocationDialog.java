package com.example.pawfinder.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.example.pawfinder.R;

public class LocationDialog extends AlertDialog.Builder {

    private Integer which;

    public LocationDialog(@NonNull Context context) {
        super(context);
        if (which == null) {
            setUpDialog(1);
        } else {
            setUpDialog(which);
        }

    }


    private void setUpDialog(Integer w) {
        setTitle(R.string.oops);

        switch (w) {
            case 1:
                setMessage(R.string.near_you_message);
            case 2:
                setMessage(R.string.map_message);
        }


        setCancelable(false);

        setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getContext().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
            }
        });

        setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

    }

    public AlertDialog prepareDialog(Integer i) {

        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


}

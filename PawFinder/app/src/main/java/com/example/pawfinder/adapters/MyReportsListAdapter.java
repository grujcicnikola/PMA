package com.example.pawfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawfinder.R;
import com.example.pawfinder.model.Pet;

import java.util.ArrayList;

public class MyReportsListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Pet> missingPets;

    public MyReportsListAdapter(Context mContext, ArrayList<Pet> missingPets) {
        this.mContext = mContext;
        this.missingPets = missingPets;
    }

    @Override
    public int getCount() {
        return missingPets.size();
    }

    @Override
    public Object getItem(int position) {
        return missingPets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.report_item, null);
        }else
        {
            view = convertView;
        }

        ImageView image = (ImageView) view.findViewById(R.id.report_image);
        image.setImageResource(missingPets.get(position).getImage());

        TextView pet_name_text = (TextView) view.findViewById(R.id.report_pet_name);
        pet_name_text.setText(missingPets.get(position).getName());

        return view;
    }
}

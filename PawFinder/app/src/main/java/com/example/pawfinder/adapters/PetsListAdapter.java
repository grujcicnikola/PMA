package com.example.pawfinder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawfinder.R;
import com.example.pawfinder.fragments.MissingFragment;
import com.example.pawfinder.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetsListAdapter extends BaseAdapter {

    Context mContext;
    List<Pet> missingPets;

    public PetsListAdapter(Context mContext, List<Pet> missingPets) {
        this.mContext = mContext;
        this.missingPets = missingPets;

    }

    @Override
    public int getCount() {
        return missingPets.size();
    }

    public Object getItem(int position) {
        return missingPets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.missing_pet_item, null);
        }else
        {
            view = convertView;
        }

        final TextView pet_name_text = (TextView) view.findViewById(R.id.pet_name);
        pet_name_text.setText(missingPets.get(position).getName());

        TextView pet_dateOfLost_text = (TextView) view.findViewById(R.id.pet_dateOfLost);
        pet_dateOfLost_text.setText(missingPets.get(position).getDateOfLost().toString());

        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        image.setImageResource(missingPets.get(position).getImage());


        return view;
    }
}

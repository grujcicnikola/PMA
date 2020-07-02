package com.example.pawfinder.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawfinder.R;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.service.ServiceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyReportsListAdapter extends BaseAdapter {

    Context mContext;
    List<Pet> missingPets;
    Pet pet;

    public MyReportsListAdapter(Context mContext, List<Pet> missingPets) {
        this.missingPets = missingPets;
        this.mContext = mContext;
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
        } else {
            view = convertView;
        }

        pet = missingPets.get(position);

        TextView pet_name_text = (TextView) view.findViewById(R.id.report_pet_name);
        pet_name_text.setText(pet.getName());

        ImageView image = (ImageView) view.findViewById(R.id.report_image);
        Picasso.get().load( pet.getImage()).into(image);

        TextView pet_missing_date = (TextView) view.findViewById(R.id.report_dateOfLost_text);
        pet_missing_date.setText(pet.getMissingSince());

        return view;
    }

    public void updateResults(List<Pet> updatedReports){
        List<Pet> good = new ArrayList<Pet>();
        for (Pet p :updatedReports) {
            if (p.isDeleted() == false) {
                good.add(p);
            }
        }
        missingPets = good;
        notifyDataSetChanged();
    }
}

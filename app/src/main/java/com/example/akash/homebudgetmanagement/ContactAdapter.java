package com.example.akash.homebudgetmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Akash on 6/8/2015.
 */
public class ContactAdapter extends ArrayAdapter<ContactModel> {

    private final Context context;
    private final ArrayList<ContactModel> modelsArrayList;

    public ContactAdapter(Context context, ArrayList<ContactModel> modelsArrayList) {

        super(context, R.layout.contact_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView=inflater.inflate(R.layout.contact_item, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.textName);
        TextView phoneNumberView = (TextView) rowView.findViewById(R.id.textPhoneNumber);

        nameView.setText(modelsArrayList.get(position).getName());
        phoneNumberView.setText(modelsArrayList.get(position).getPhoneNumber());

        return rowView;
    }
}

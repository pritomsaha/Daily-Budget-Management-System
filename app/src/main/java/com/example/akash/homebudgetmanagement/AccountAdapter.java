package com.example.akash.homebudgetmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Akash on 6/9/2015.
 */
public class AccountAdapter extends ArrayAdapter<AccountModel> {

    private final Context context;
    private final ArrayList<AccountModel> modelsArrayList;

    public AccountAdapter(Context context, ArrayList<AccountModel> modelsArrayList){

        super(context,R.layout.account_item,modelsArrayList);
        this.context=context;
        this.modelsArrayList=modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView=inflater.inflate(R.layout.account_item, parent, false);
        TextView accountName=(TextView)rowView.findViewById(R.id.txtAccountName);
        TextView openingBalance=(TextView)rowView.findViewById(R.id.txtopeningBalance);
        TextView currentBalance=(TextView)rowView.findViewById(R.id.txtcurrentAmount);

        accountName.setText(modelsArrayList.get(position).getAccountName());
        openingBalance.setText(modelsArrayList.get(position).getOpeningBalance());
        currentBalance.setText(modelsArrayList.get(position).getCurrentBalance());

        return rowView;
    }
}

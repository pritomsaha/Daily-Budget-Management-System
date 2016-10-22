package com.example.akash.homebudgetmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Akash on 6/10/2015.
 */
public class Income_Expense_Adapter extends ArrayAdapter<Income_ExpenseModel>{

    private final Context context;
    private final ArrayList<Income_ExpenseModel> modelsArrayList;

    public Income_Expense_Adapter(Context context, ArrayList<Income_ExpenseModel> modelsArrayList){

        super(context,R.layout.income_expense_item,modelsArrayList);
        this.context=context;
        this.modelsArrayList=modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView=inflater.inflate(R.layout.income_expense_item, parent, false);
        TextView contact=(TextView)rowView.findViewById(R.id.txtInc_exp_contact);
        TextView date=(TextView)rowView.findViewById(R.id.txtInc_exDate);
        TextView amount=(TextView)rowView.findViewById(R.id.txtInc_exp_amount);
        TextView category=(TextView)rowView.findViewById(R.id.txtInc_exp_category);

        contact.setText(modelsArrayList.get(position).getContact());
        date.setText(modelsArrayList.get(position).getDate());
        amount.setText(modelsArrayList.get(position).getAmount());
        category.setText(modelsArrayList.get(position).getCategory());

        return rowView;
    }

}
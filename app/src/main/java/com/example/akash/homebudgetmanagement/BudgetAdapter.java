package com.example.akash.homebudgetmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Akash on 6/12/2015.
 */
public class BudgetAdapter extends ArrayAdapter<BudgetModel> {

    private final Context context;
    private final ArrayList<BudgetModel> modelsArrayList;

    public BudgetAdapter(Context context, ArrayList<BudgetModel> modelsArrayList){

        super(context,R.layout.budget_item,modelsArrayList);
        this.context=context;
        this.modelsArrayList=modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView=inflater.inflate(R.layout.budget_item, parent, false);
        TextView category=(TextView)rowView.findViewById(R.id.txtBudget_category_name);
        TextView left_amount=(TextView)rowView.findViewById(R.id.txtLeftAmount);
        TextView spentAndBudget=(TextView)rowView.findViewById(R.id.txtSpentAndBudget);

        String leftOrSpent=" Left";
        Double leftAmount=Double.parseDouble(modelsArrayList.get(position).getLeft_amount());
        Double spentAmount=Double.parseDouble(modelsArrayList.get(position).getSpent_amount());
        if (leftAmount<0) {
            leftAmount*=(-1);
            leftOrSpent=" Over";
        }

        category.setText(modelsArrayList.get(position).getCategory());
        left_amount.setText(MainActivity.getDoubleText(leftAmount)+leftOrSpent);
        spentAndBudget.setText(MainActivity.getDoubleText(spentAmount)+" of "+
              modelsArrayList.get(position).getBudget_amount());

        return rowView;
    }

}

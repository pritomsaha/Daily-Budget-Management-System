package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Budget extends Activity {

    final Context context=this;
    private TextView txtCurrentMonth;
    private ListView list_budgets;
    private TextView total;
    private Button btnAddBudget;
    public static boolean isEdit;
    private BudgetDBHelper budgetDBHelper;
    public static ArrayList<BudgetModel> models=new ArrayList<>();
    public static BudgetAdapter adapter;

    private String budget_category;
    private String budget_amount;
    private String budget_notes;
    private String current_Month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        isEdit=false;
        current_Month=Income.df2.format(Income.calendar.getTime());
        txtCurrentMonth=(TextView)findViewById(R.id.txtCurrentMonth);
        list_budgets=(ListView)findViewById(R.id.listView_budget);
        total=(TextView)findViewById(R.id.textTotal);
        btnAddBudget=(Button)findViewById(R.id.btnAddBudget);

        txtCurrentMonth.setText(current_Month);

        budgetDBHelper=new BudgetDBHelper(this);
        models=budgetDBHelper.getCurrentMonthBudgets(current_Month);
        Collections.sort(models,new Comparator<BudgetModel>() {
            @Override
            public int compare(BudgetModel a, BudgetModel b) {
                if(a.getCategory().compareTo(b.getCategory())>0)return 1;
                else return -1;
            }
        });
        adapter=new BudgetAdapter(this,models);
        list_budgets.setAdapter(adapter);
        Double  bud[]=budgetDBHelper.getTotalBudget(current_Month);
        total.setText(MainActivity.getDoubleText(bud[1])+" of "+MainActivity.getDoubleText(bud[0]));

        btnAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBudget();
            }
        });

        list_budgets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                longClick_activity(position);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

    private void addBudget(){
        Intent intent = new Intent(Budget.this,Budget_adder.class);
        startActivity(intent);
    }

    private void longClick_activity(int position){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_delete_design);

        Object obj = list_budgets.getItemAtPosition(position);
        final BudgetModel model =(BudgetModel)obj;

        budget_category=model.getCategory();
        budget_amount=model.getBudget_amount();
        budget_notes=model.getNotes();

        dialog.setTitle(budget_category);
        TextView txtEdit = (TextView)dialog.findViewById(R.id.txtEdit);
        TextView txtDelete = (TextView)dialog.findViewById(R.id.txtDelete);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit=true;
                Intent intent = new Intent(Budget.this,Budget_adder.class);
                intent.putExtra("category",budget_category);
                intent.putExtra("amount",budget_amount);
                intent.putExtra("notes",budget_notes);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budgetDBHelper.deleteBudget(current_Month,budget_category);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

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

public class Expense extends Activity {

    final Context context=this;

    private ListView listExpenses;
    private TextView currentMonth;
    private Button btnaddExpense;
    private TextView totalExpense;

    public static boolean isEdit;
    public static Income_Expense_Adapter adapter;
    public static ArrayList<Income_ExpenseModel> models=new ArrayList<>();

    public  ExpenseDBHelper expenseDBHelper;
    private AccountDBHelper accountDBHelper;
    private BudgetDBHelper budgetDBHelper;

    private String current_Month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        isEdit=false;
        current_Month=Income.df2.format(Income.calendar.getTime());
        listExpenses=(ListView) findViewById(R.id.listView_expense);
        currentMonth=(TextView)findViewById(R.id.textExpCurrentmonth);
        btnaddExpense=(Button) findViewById(R.id.btnAddExpense);
        totalExpense=(TextView)findViewById(R.id.textTotalExpense);
        currentMonth.setText(current_Month);

        expenseDBHelper=new ExpenseDBHelper(this);
        accountDBHelper=new AccountDBHelper(this);
        budgetDBHelper=new BudgetDBHelper(this);
        models=expenseDBHelper.getCurrentMonthExpense(current_Month);
        Collections.sort(models,new Comparator<Income_ExpenseModel>() {
            @Override
            public int compare(Income_ExpenseModel a, Income_ExpenseModel b) {

                if(a.getDay().compareTo(b.getDay())>0) return 1;
                else if(a.getDay().compareTo(b.getDay())==0){
                    return a.getContact().compareTo(b.getContact());
                }
                else return -1;
            }
        });
        adapter=new Income_Expense_Adapter(this,models);
        listExpenses.setAdapter(adapter);

        btnaddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        listExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long_click_activity(position);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

    private void addExpense(){
        Intent intent = new Intent(Expense.this,Expense_adder.class);
        startActivity(intent);
    }

    private void long_click_activity(final int position){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_delete_design);

        Object obj = listExpenses.getItemAtPosition(position);
        final Income_ExpenseModel model =(Income_ExpenseModel)obj;

        final String account_name=model.getAccount();
        final String expense_amount=model.getAmount();
        final String expense_date=model.getDate();
        final String expense_time=model.getTime();
        final String contact_name=model.getContact();
        final String category=model.getCategory();
        final String expense_notes=model.getNotes();

        String[] token=expense_date.split(" ");
        final String expense_month=token[1]+" "+token[2];

        dialog.setTitle(account_name);
        TextView txtEdit = (TextView)dialog.findViewById(R.id.txtEdit);
        TextView txtDelete = (TextView)dialog.findViewById(R.id.txtDelete);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit=true;
                Intent intent = new Intent(Expense.this,Expense_adder.class);
                intent.putExtra("account",account_name);
                intent.putExtra("contact",contact_name);
                intent.putExtra("category",category);
                intent.putExtra("amount",expense_amount);
                intent.putExtra("date",expense_date);
                intent.putExtra("month",expense_month);
                intent.putExtra("time",expense_time);
                intent.putExtra("notes",expense_notes);

                startActivity(intent);
                dialog.dismiss();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDBHelper.deleteExpense(expense_date, expense_time);
                accountDBHelper.increaseCurrentBalance(account_name, expense_amount);
                budgetDBHelper.decreaseSpent(expense_month,category,expense_amount);
                onResume();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

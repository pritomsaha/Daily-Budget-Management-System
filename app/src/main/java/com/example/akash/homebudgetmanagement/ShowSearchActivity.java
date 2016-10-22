package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowSearchActivity extends Activity {

    private Income_Expense_Adapter income_expense_adapter;
    private  ArrayList<Income_ExpenseModel> income_expenseModels=new ArrayList<>();
    private BudgetAdapter budgetAdapter;
    private  ArrayList<BudgetModel> budgetModels=new ArrayList<>();


    private TextView DateField;
    private TextView NameField;
    private ListView listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search);

        NameField = (TextView) findViewById(R.id.txtHeader);
        DateField = (TextView) findViewById(R.id.textDate);
        listItems = (ListView) findViewById(R.id.listViewItems);

        Intent intent = getIntent();
        String name=intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        String date=intent.getStringExtra("date");

        NameField.setText(name);
        DateField.setText(date);

        if(name.compareTo("Income")==0){
            IncomeDBHelper incomeDBHelper=new IncomeDBHelper(this);

           if(type.compareTo("search_day")==0){

               income_expenseModels=incomeDBHelper.getCurrentDayIncomes(date);
               income_expense_adapter=new Income_Expense_Adapter(this,income_expenseModels);
               listItems.setAdapter(income_expense_adapter);

           }
            else{

               income_expenseModels=incomeDBHelper.getCurrentMonthIncomes(date);
               income_expense_adapter=new Income_Expense_Adapter(this,income_expenseModels);
               listItems.setAdapter(income_expense_adapter);
           }

        }

        else if(name.compareTo("Expense")==0){

            ExpenseDBHelper expenseDBHelper=new ExpenseDBHelper(this);

            if (type.compareTo("search_day")==0){

                income_expenseModels=expenseDBHelper.getCurrentDayExpenses(date);
                income_expense_adapter=new Income_Expense_Adapter(this,income_expenseModels);
                listItems.setAdapter(income_expense_adapter);
            }
            else {

                income_expenseModels=expenseDBHelper.getCurrentMonthExpense(date);
                income_expense_adapter=new Income_Expense_Adapter(this,income_expenseModels);
                listItems.setAdapter(income_expense_adapter);
            }
        }

        else {

            BudgetDBHelper budgetDBHelper=new BudgetDBHelper(this);

            budgetModels= budgetDBHelper.getCurrentMonthBudgets(date);
            budgetAdapter=new BudgetAdapter(this,budgetModels);
            listItems.setAdapter(budgetAdapter);
        }

    }
}

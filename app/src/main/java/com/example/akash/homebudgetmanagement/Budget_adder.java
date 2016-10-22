package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Budget_adder extends Activity {

    private static final int CATEGORY_NAME=1;
    private static  final int CALC=0;
    private EditText budget_category;
    private EditText budget_amount;
    private EditText budget_notes;
    private Button btnSave;
    private BudgetDBHelper budgetDBHelper;
    private ExpenseDBHelper expenseDBHelper;

    private String current_Month;
    private String oldBudget_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        budget_category=(EditText)findViewById(R.id.editText_budgetCategory);
        budget_amount=(EditText)findViewById(R.id.editText_budget_amount);
        budget_notes=(EditText)findViewById(R.id.editText_budget_notes);
        btnSave=(Button)findViewById(R.id.btnBudgetSave);

        budgetDBHelper=new BudgetDBHelper(this);
        expenseDBHelper=new ExpenseDBHelper(this);

        if(Budget.isEdit){

            oldBudget_category=getIntent().getStringExtra("category");
            budget_category.setText(oldBudget_category);
            budget_amount.setText(getIntent().getStringExtra("amount"));
            budget_notes.setText(getIntent().getStringExtra("notes"));
        }

        budget_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory();
            }
        });

        budget_amount.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                getValueFromCalculator();
                return true;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_activity();
            }
        });

    }

    private void selectCategory(){
        Categories.isItemClickable=true;
        Intent intent = new Intent(this,Categories.class);
        startActivityForResult(intent, CATEGORY_NAME);
    }

    private void getValueFromCalculator(){

        Intent intent = new Intent(Budget_adder.this,Calculator.class);
        startActivityForResult(intent,CALC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CATEGORY_NAME) {

            if (resultCode == Activity.RESULT_OK) {

                String category_name = data.getStringExtra("category");
                budget_category.setText(category_name);
            }
        } else if (requestCode == CALC) {

            if (resultCode == Activity.RESULT_OK) {

                String number = data.getStringExtra("number");
                budget_amount.setText(number);
            }
        }
    }

    private void save_activity(){

        String category=budget_category.getText().toString();
        String amount=MainActivity.deleteLeadingZero(budget_amount.getText().toString());
        String notes=budget_notes.getText().toString();

        current_Month=Income.df2.format(Income.calendar.getTime());
        Double Spent=expenseDBHelper.getCategoryTotalExpense(current_Month,category);
        Double Left=Double.parseDouble(amount)-Spent;

        if(category.compareTo("")==0)Toast.makeText(getBaseContext(),
                "Invalid category", Toast.LENGTH_SHORT).show();
        else if(!Budget.isEdit&&budgetDBHelper.search(current_Month,category))Toast.makeText(getBaseContext(),
                "Already exists", Toast.LENGTH_SHORT).show();
        else if(amount.length()==0||amount.compareTo("0")==0) Toast.makeText(getBaseContext(),
                "Please enter valid amount", Toast.LENGTH_SHORT).show();
        else if(Budget.isEdit){

            Budget.isEdit=false;
            budgetDBHelper.upDateRow(current_Month,oldBudget_category,category,amount,
                    MainActivity.getDoubleText(Spent),MainActivity.getDoubleText(Left),notes);
            finish();
        }
        else{

            budgetDBHelper.insertBudget(current_Month,category,amount,MainActivity.getDoubleText(Spent),
                    MainActivity.getDoubleText(Left),notes);
            finish();
        }
    }
}

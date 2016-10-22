package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView calculator;
    private TextView textAddIncome;
    private TextView textAddExpense;
    private TextView textAddAccounts;
    private TextView textBills;
    private TextView textAccounts;
    private TextView textCatagory;
    private TextView textContact;
    private TextView textIncome;
    private TextView textExpense;
    private TextView textBudget;
    public static TextView textPercent;
    public static TextView totalBudget;
    public static TextView totalSpent;
    private BudgetDBHelper budgetDBHelper;
    private String currentMonth;
    private TextView overView;
    private TextView textAddBudget;
    private TextView Search;
    private TextView Statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentMonth=Income.df2.format(Income.calendar.getTime());
        budgetDBHelper=new BudgetDBHelper(this);
        calculator=(TextView)findViewById(R.id.textCalc);
        textAddIncome=(TextView)findViewById(R.id.textAddIncome);
        textAddExpense=(TextView)findViewById(R.id.textAddExpense);
        textAddAccounts=(TextView)findViewById(R.id.textAddAcounts);
        textAddBudget=(TextView)findViewById(R.id.textAddBudget);
        textAccounts=(TextView)findViewById(R.id.textAccounts);
        textCatagory=(TextView)findViewById(R.id.textCatagory);
        textContact=(TextView)findViewById(R.id.textContact);
        textIncome=(TextView)findViewById(R.id.textIncome);
        textExpense=(TextView)findViewById(R.id.textExpense);
        textBudget=(TextView)findViewById(R.id.text_budget);
        textPercent=(TextView) findViewById(R.id.textPercent);
        totalBudget=(TextView)findViewById(R.id.textBudget);
        totalSpent=(TextView)findViewById(R.id.textSpent);
        overView=(TextView)findViewById(R.id.textOverview);
        Search=(TextView)findViewById(R.id.textSearch);
        Statistics=(TextView)findViewById(R.id.textStatistics);
        textBills=(TextView)findViewById(R.id.textBills);

        String bd[]=budgetDBHelper.getTotalBudgetAndSpent(currentMonth);

        totalBudget.setText("Budget:"+bd[0]);
        totalSpent.setText("Spent:"+bd[1]);
        textPercent.setText(bd[2]+"%");

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalculator();
            }
        });

        textAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
            }
        });

        textAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        textAddAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });

        textAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBudget();
            }
        });

        textAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccounts();
            }
        });

        textCatagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory();
            }
        });

        textContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContact();
            }
        });

        textIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIncomes();
            }
        });

        textExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenses();
            }
        });

        textBudget.setOnClickListener(v -> showBudgets());

        overView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverView();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_search_activity();
            }
        });

        Statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Statistics();
            }
        });
        textBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Bills.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    private void showCalculator(){

        Intent intent = new Intent(this,Calculator.class);
        startActivity(intent);
    }

    private void addIncome(){

        Intent intent = new Intent(this,Income_adder.class);
        startActivity(intent);
    }

    private void addExpense(){

        Intent intent = new Intent(this,Expense_adder.class);
        startActivity(intent);
    }

    private void addAccount(){

        Intent intent = new Intent(this,Account_adder.class);
        startActivity(intent);

    }

    private void addBudget(){

        Intent intent = new Intent(this,Budget_adder.class);
        startActivity(intent);

    }

    private void showAccounts(){

        Intent intent = new Intent(this,Account.class);
        startActivity(intent);

    }

    private void showCategory(){

        Intent intent = new Intent(this,Categories.class);
        startActivity(intent);
    }

    private void showContact(){

        Intent intent = new Intent(this,Contacts.class);
        startActivity(intent);
    }

    private void showIncomes(){
        Intent intent = new Intent(this,Income.class);
        startActivity(intent);
    }

    private void showExpenses(){
        Intent intent = new Intent(this,Expense.class);
        startActivity(intent);
    }

    private void showBudgets(){
        Intent intent = new Intent(this,Budget.class);
        startActivity(intent);
    }

    private void showOverView(){
        Intent intent=new Intent(this,OverView.class);
        startActivity(intent);
    }

    private void show_search_activity(){
        Intent intent=new Intent(this,SearchActivity.class);
        startActivity(intent);
    }

    private void show_Statistics(){

        Intent intent=new Intent(this, Statistics_activity.class);
        startActivity(intent);
    }

    public static String getDoubleText(Double num){

        String numText=num.toString();
        String[] token=numText.split("[.]");

        if(token.length==1) return numText;

        if(Double.parseDouble(token[1])==0) numText=token[0];

        return numText;
    }

    public static String deleteLeadingZero(String num){
        int len=num.length()-1;
        int i;
        for(i=0;i<len;i++)
            if(num.charAt(i)!='0')break;
        return num.substring(i);
    }
}
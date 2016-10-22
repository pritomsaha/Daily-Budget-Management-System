package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class OverView extends Activity {

    private TextView txtAccountNames;
    private TextView accountBalances;
    private TextView TotalBalance;
    private TextView textTrancMonth;
    private TextView textBudgetMonth;
    private TextView textTIncome;
    private TextView textTExpense;
    private TextView textTBudget;
    private TextView TInEx;

    private String currentMonth;
    private AccountDBHelper accountDBHelper;
    private IncomeDBHelper incomeDBHelper;
    private ExpenseDBHelper expenseDBHelper;
    private BudgetDBHelper budgetDBHelper;
    private ArrayList<AccountModel> accountsNamesAndBalances;
    private String account_Names=new String();
    private String account_Balances=new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        txtAccountNames=(TextView)findViewById(R.id.accName);
        accountBalances=(TextView)findViewById(R.id.balance);
        TotalBalance=(TextView)findViewById(R.id.txtt_Total);
        textTrancMonth=(TextView)findViewById(R.id.texttTraMonth);
        textBudgetMonth=(TextView)findViewById(R.id.texttBudgetDate);
        textTIncome=(TextView)findViewById(R.id.texttIncome);
        textTExpense=(TextView)findViewById(R.id.texttExpense);
        textTBudget=(TextView)findViewById(R.id.texttBudget);
        TInEx=(TextView)findViewById(R.id.textTotalInEx);

        accountDBHelper=new AccountDBHelper(this);
        incomeDBHelper=new IncomeDBHelper(this);
        expenseDBHelper=new ExpenseDBHelper(this);
        budgetDBHelper=new BudgetDBHelper(this);

        currentMonth=Income.df2.format(Income.calendar.getTime());

        accountsNamesAndBalances=accountDBHelper.getAllAccounts();
        Collections.sort(accountsNamesAndBalances,new Comparator<AccountModel>() {
            @Override
            public int compare(AccountModel a, AccountModel b) {
                if(a.getAccountName().compareTo(b.getAccountName())>0) return 1;
                else return -1;
            }

        });

        for (AccountModel model:accountsNamesAndBalances){

            account_Names+=("\n"+model.getAccountName()+"\n");
            account_Balances+=("\n"+model.getCurrentBalance()+"\n");
        }
        account_Names=account_Names.substring(0,account_Names.length()-2);
        account_Balances=account_Balances.substring(0,account_Balances.length()-2);
        txtAccountNames.setText(account_Names);
        accountBalances.setText(account_Balances);
        TotalBalance.setText(accountDBHelper.getTotalAccount());

        textTrancMonth.setText(currentMonth);
        textBudgetMonth.setText(currentMonth);

        Double Tincome=incomeDBHelper.getTotalIncome(currentMonth);
        Double Texpense=expenseDBHelper.getTotalExpense(currentMonth);
        Double InMEx=Tincome-Texpense;

        Double  bud[]=budgetDBHelper.getTotalBudget(currentMonth);

        textTIncome.setText(MainActivity.getDoubleText(Tincome));
        textTExpense.setText(MainActivity.getDoubleText(Texpense));
        TInEx.setText(MainActivity.getDoubleText(InMEx));
        textTBudget.setText(MainActivity.getDoubleText(bud[1])+" of "+MainActivity.getDoubleText(bud[0]));

    }

}

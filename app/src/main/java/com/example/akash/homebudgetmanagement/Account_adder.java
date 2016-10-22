package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Account_adder extends Activity {

    private static  final int calc=0;
    private EditText openingBalance;
    private EditText accountName;
    private EditText notes;
    private Button btnSave;
    private AccountDBHelper accountDBHelper;
    private IncomeDBHelper incomeDBHelper;
    private ExpenseDBHelper expenseDBHelper;

    private String oldAccountName;
    private String oldOpeningBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        accountName=(EditText)findViewById(R.id.editText_account_name);
        notes=(EditText)findViewById(R.id.editText_account_notes);
        openingBalance=(EditText)findViewById(R.id.editText_opening_balance);
        openingBalance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent(Account_adder.this,Calculator.class);
                startActivityForResult(intent, calc);
                return true;
            }
        });

        accountDBHelper=new AccountDBHelper(this);

        btnSave=(Button)findViewById(R.id.btnAccountSave);

        if(Account.isEdit==true) {
            Intent intent = getIntent();
            oldAccountName=intent.getStringExtra("accountName");
            oldOpeningBalance=intent.getStringExtra("openingBalance");
            accountName.setText(oldAccountName);
            openingBalance.setText(oldOpeningBalance);
            notes.setText(intent.getStringExtra("notes"));
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_activity();
            }
        });
    }

    private void save_activity(){

        incomeDBHelper=new IncomeDBHelper(Account_adder.this);
        expenseDBHelper=new ExpenseDBHelper(Account_adder.this);

        String AccountName=accountName.getText().toString();
        String OpeningBalance=MainActivity.deleteLeadingZero(openingBalance.getText().toString());
        String Notes=notes.getText().toString();

        if(AccountName.length()==0)Toast.makeText(getBaseContext(), "Enter a Account Name",
                Toast.LENGTH_SHORT).show();
        else if(Account.isEdit){
            Account.isEdit=false;
            accountDBHelper.upDateRow(oldAccountName,oldOpeningBalance,AccountName,OpeningBalance,Notes);
            incomeDBHelper.upDateRow(oldAccountName,AccountName);
            expenseDBHelper.upDateRow(oldAccountName,AccountName);
            finish();
        }
        else if(accountDBHelper.searchAccountName(AccountName)) Toast.makeText(getBaseContext(),
                "Account name is already exists",Toast.LENGTH_SHORT).show();
        else{
            accountDBHelper.insertAccount(AccountName,OpeningBalance,OpeningBalance,Notes);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == calc) {
            if (resultCode == Activity.RESULT_OK) {
                String number = data.getStringExtra("number");
                openingBalance.setText(number);
            }
        }
    }
}

package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class Income_adder extends Activity {

    private static  final int CALC=0;
    private static final int CATEGORY_NAME=1;
    private static final int ACCOUNT_NAME=2;
    private static final int CONTACT_NAME=3;
    static final int DATE_DIALOG_ID = 998;
    private Calendar _calendar;
    private int year;
    private int month;
    private int day;

    private EditText Amount;
    private EditText income_category;
    private EditText Account_Name;
    private EditText Contact;
    private EditText date;
    private EditText notes;
    private Button btnSave;

    private String current_Date;
    private String SelectedMonth;
    private String currentTime;
    private String oldIncome_amount;
    private String oldAccountName;
    private String oldIncome_date;
    private String oldIncome_time;

    private IncomeDBHelper incomeDBHelper;
    private AccountDBHelper accountDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        current_Date=Income.df1.format(Income.calendar.getTime());

        Amount=(EditText)findViewById(R.id.editText_income_Amount);
        income_category=(EditText) findViewById(R.id.editText_income_Category);
        Account_Name=(EditText)findViewById(R.id.income_editText_Account);
        Contact=(EditText)findViewById(R.id.income_editText_Name);
        date =(EditText)findViewById(R.id.btn_income_Date);
        notes=(EditText) findViewById(R.id.editTextIncomeNotes);
        btnSave=(Button)findViewById(R.id.btnIncomeSave);

        incomeDBHelper=new IncomeDBHelper(this);
        accountDBHelper=new AccountDBHelper(this);
        income_category.setText("No Category");

        Account_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectAccount();
                Account.isItemClickable=true;
                Intent intent = new Intent(Income_adder.this,Account.class);
                startActivityForResult(intent,ACCOUNT_NAME);
            }
        });

        Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contacts.isItemClickable=true;
                Intent intent = new Intent(Income_adder.this,Contacts.class);
                startActivityForResult(intent,CONTACT_NAME);
            }
        });

        Amount.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent(Income_adder.this,Calculator.class);
                startActivityForResult(intent,CALC);
                return true;
            }
        });

        income_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Categories.isItemClickable=true;
                Intent intent = new Intent(Income_adder.this,Categories.class);
                startActivityForResult(intent, CATEGORY_NAME);
            }
        });

        setCurrentDateOnDatePicker();
        _calendar = Calendar.getInstance(Locale.getDefault());
        date.setText(current_Date);

        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        if(Income.isEdit){

            oldAccountName=getIntent().getStringExtra("account");
            oldIncome_amount=getIntent().getStringExtra("amount");
            oldIncome_date=getIntent().getStringExtra("date");
            oldIncome_time=getIntent().getStringExtra("time");

            Account_Name.setText(oldAccountName);
            Contact.setText(getIntent().getStringExtra("contact"));
            income_category.setText(getIntent().getStringExtra("category"));
            Amount.setText(oldIncome_amount);
            date.setText(oldIncome_date);
            notes.setText(getIntent().getStringExtra("notes"));
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_activity();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CALC) {

            if (resultCode == Activity.RESULT_OK) {

                String number = data.getStringExtra("number");
                Amount.setText(number);
            }
        }
        else if(requestCode==CATEGORY_NAME){

            if (resultCode == Activity.RESULT_OK) {

                String category_name = data.getStringExtra("category");
                income_category.setText(category_name);
            }
        }
        else if(requestCode==ACCOUNT_NAME){

            if (resultCode==Activity.RESULT_OK){

                String account_Name=data.getStringExtra("accountName");
                Account_Name.setText(account_Name);
            }
        }
        else if(requestCode==CONTACT_NAME){

            if(resultCode==Activity.RESULT_OK){

                String contact = data.getStringExtra("contact");
                Contact.setText(contact);
            }
        }

    }

    private void save_activity(){

        String account_name=Account_Name.getText().toString();
        String contact_name=Contact.getText().toString();
        String category=income_category.getText().toString();
        String income_amount=MainActivity.deleteLeadingZero(Amount.getText().toString());
        String income_date=date.getText().toString();
        String income_notes=notes.getText().toString();

        currentTime=Income.df3.format(Income.calendar.getTime());
        String[] tokens=income_date.split(" ");
        SelectedMonth=tokens[1]+" "+tokens[2];

        if(account_name.length()==0)
            Toast.makeText(getBaseContext(), "Select an account ", Toast.LENGTH_SHORT).show();
        else if(contact_name.length()==0) Toast.makeText(getBaseContext(), "Select a name", Toast.LENGTH_SHORT).show();
        else if(income_amount.length()==0||income_amount.compareTo("0")==0)
            Toast.makeText(getBaseContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show();
        else if(Income.isEdit){
            Income.isEdit=false;
           // ArrayList<String> resources=new ArrayList<>(Arrays.asList(income_date,SelectedMonth,income_amount,
             //       account_name, contact_name, category,
               //     income_notes));
            incomeDBHelper.upDateRow(oldIncome_time,oldIncome_date,income_date,SelectedMonth,currentTime,
                    income_amount,account_name, contact_name, category,income_notes);
            accountDBHelper.decreaseCurrentBalance(oldAccountName,oldIncome_amount);
            accountDBHelper.increaseCurrentBalance(account_name, income_amount);

            //Intent data = new Intent();
            //data.putStringArrayListExtra("IncomeInfo", resources);
            //setResult(Activity.RESULT_OK, data);
            finish();
        }
        else {
            incomeDBHelper.insertIncome(income_date,SelectedMonth,currentTime,income_amount,
                    account_name,contact_name,category,income_notes);
            accountDBHelper.increaseCurrentBalance(account_name, income_amount);
            finish();
        }
    }

    protected Dialog onCreateDialog(int id){

        if(id==DATE_DIALOG_ID){
            return new DatePickerDialog(this, datePickerListener,year, month,
                    day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            _calendar.set(year,month,day);
            date.setText(Income.df1.format(_calendar.getTime()));
            SelectedMonth=Income.df2.format(_calendar.getTime());
        }

    };

    private void setCurrentDateOnDatePicker() {

        year = Income.calendar.get(Calendar.YEAR);
        month = Income.calendar.get(Calendar.MONTH);
        day = Income.calendar.get(Calendar.DAY_OF_MONTH);
    }
}
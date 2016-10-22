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

public class Expense_adder extends Activity {

    private static final int CATEGORY_NAME=1;
    private static  final int calc=0;
    private static final int ACCOUNT_NAME=2;
    private static final int CONTACT_NAME=3;
    private EditText addAmount;
    private EditText expense_category;
    private EditText Account_Name;
    private EditText Contact;
    private EditText date;
    private EditText notes;
    private Button btnSave;
    private String current_Month;

    private String SelectedMonth;
    private String currentTime;
    private String current_Date;
    private String oldExpense_amount;
    private String oldAccountName;
    private String oldExpense_date;
    private String oldExpense_time;
    private String oldExpense_month;
    private String oldExpense_category;


    private ExpenseDBHelper expenseDBHelper;
    private AccountDBHelper accountDBHelper;
    private BudgetDBHelper budgetDBHelper;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 998;
    private Calendar _calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        current_Date=Income.df1.format(Income.calendar.getTime());
        expense_category=(EditText) findViewById(R.id.editText_expense_Category);
        addAmount=(EditText)findViewById(R.id.editText_expense_Amount);
        Account_Name=(EditText)findViewById(R.id.editText_expense_Account);
        Contact=(EditText)findViewById(R.id.editText_expense_Name);
        date =(EditText)findViewById(R.id.btn_expense_Date);
        notes=(EditText) findViewById(R.id.editTextExpenseNotes);
        btnSave=(Button)findViewById(R.id.btnExpenseSave);
        expenseDBHelper=new ExpenseDBHelper(this);
        accountDBHelper=new AccountDBHelper(this);
        budgetDBHelper=new BudgetDBHelper(this);

        expense_category.setText("No Category");

        addAmount.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                getValueFromCalculator();
                return true;
            }
        });


        expense_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        Account_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccount();
            }
        });

        Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
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

        if(Expense.isEdit){

            oldAccountName=getIntent().getStringExtra("account");
            oldExpense_amount=getIntent().getStringExtra("amount");
            oldExpense_date=getIntent().getStringExtra("date");
            oldExpense_time=getIntent().getStringExtra("time");
            oldExpense_month=getIntent().getStringExtra("month");
            oldExpense_category=getIntent().getStringExtra("category");

            Account_Name.setText(oldAccountName);
            Contact.setText(getIntent().getStringExtra("contact"));
            expense_category.setText(oldExpense_category);
            addAmount.setText(oldExpense_amount);
            date.setText(oldExpense_date);
            notes.setText(getIntent().getStringExtra("notes"));
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_activity();
            }
        });
    }

    public void selectAccount(){
        Account.isItemClickable=true;
        Intent intent = new Intent(Expense_adder.this,Account.class);
        startActivityForResult(intent,ACCOUNT_NAME);
    }

    public void selectContact(){
        Contacts.isItemClickable=true;
        Intent intent = new Intent(Expense_adder.this,Contacts.class);
        startActivityForResult(intent,CONTACT_NAME);
    }

    private void addCategory(){
        Categories.isItemClickable=true;
        Intent intent = new Intent(this,Categories.class);
        startActivityForResult(intent, CATEGORY_NAME);
    }

    private void getValueFromCalculator(){

        Intent intent = new Intent(this,Calculator.class);
        startActivityForResult(intent,calc);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==CATEGORY_NAME){

            if (resultCode == Activity.RESULT_OK) {

                String category_name = data.getStringExtra("category");
                expense_category.setText(category_name);
            }
        }

        else if (requestCode == calc) {

            if (resultCode == Activity.RESULT_OK) {

                String number = data.getStringExtra("number");
                addAmount.setText(number);
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
        }

    };

    private void setCurrentDateOnDatePicker() {

        year = Income.calendar.get(Calendar.YEAR);
        month = Income.calendar.get(Calendar.MONTH);
        day = Income.calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void save_activity(){

        String account_name=Account_Name.getText().toString();
        String contact_name=Contact.getText().toString();
        String category=expense_category.getText().toString();
        String expense_amount=MainActivity.deleteLeadingZero(addAmount.getText().toString());
        String expense_date=date.getText().toString();
        String expense_notes=notes.getText().toString();
        current_Month=Income.df2.format(Income.calendar.getTime());
        currentTime=Income.df3.format(Income.calendar.getTime());
        String[] tokens=expense_date.split(" ");
        SelectedMonth=tokens[1]+" "+tokens[2];

        if(account_name.length()==0)
            Toast.makeText(getBaseContext(), "Select an account ", Toast.LENGTH_SHORT).show();
        else if(contact_name.length()==0) Toast.makeText(getBaseContext(), "Select a name", Toast.LENGTH_SHORT).show();
        else if(expense_amount.length()==0||expense_amount.compareTo("0")==0) Toast.makeText(getBaseContext(),
                "Enter a valid amount", Toast.LENGTH_SHORT).show();
        else if(Expense.isEdit){

            Expense.isEdit=false;
            //ArrayList<String> resources=new ArrayList<>(Arrays.asList(expense_date,SelectedMonth,expense_amount,
              //      account_name, contact_name, category,
                //    expense_notes));

            expenseDBHelper.upDateRow(oldExpense_time,oldExpense_date,expense_date,SelectedMonth,currentTime,
                    expense_amount,account_name, contact_name, category,expense_notes);

            accountDBHelper.increaseCurrentBalance(oldAccountName,oldExpense_amount);
            budgetDBHelper.decreaseSpent(oldExpense_month,oldExpense_category,oldExpense_amount);

            accountDBHelper.decreaseCurrentBalance(account_name, expense_amount);
            budgetDBHelper.spentBudget(SelectedMonth,category,expense_amount);
            //Intent data = new Intent();
            //data.putStringArrayListExtra("expenseInfo", resources);
            //setResult(Activity.RESULT_OK, data);
            finish();

        }
        else {

            expenseDBHelper.insertExpense(expense_date,SelectedMonth,currentTime,expense_amount,
                    account_name,contact_name,category,expense_notes);
            accountDBHelper.decreaseCurrentBalance(account_name,expense_amount);
            budgetDBHelper.spentBudget(SelectedMonth,category,expense_amount);
            finish();
        }
    }
}

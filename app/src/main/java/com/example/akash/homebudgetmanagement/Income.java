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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class Income extends Activity {

    final Context context=this;

    public static SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat df2 = new SimpleDateFormat("MMM yyyy");
    public static SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss");
    public static Calendar calendar = Calendar.getInstance();
    private ListView listIncomes;
    private TextView currentMonth;
    private Button btnaddIncome;
    private TextView totalIncome;
    public static Income_Expense_Adapter adapter;
    public static ArrayList<Income_ExpenseModel> models=new ArrayList<>();
    private IncomeDBHelper incomeDBHelper;
    private AccountDBHelper accountDBHelper;
    public static boolean isEdit;

    private String current_Month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        isEdit=false;
        current_Month=df2.format(calendar.getTime());
        listIncomes=(ListView) findViewById(R.id.listView_income);
        currentMonth=(TextView)findViewById(R.id.textCurrentmonth);
        btnaddIncome=(Button) findViewById(R.id.btnAddIncome);
        totalIncome=(TextView)findViewById(R.id.textTotalIncome);
        currentMonth.setText(current_Month);

        incomeDBHelper=new IncomeDBHelper(this);
        accountDBHelper=new AccountDBHelper(this);
        models=incomeDBHelper.getCurrentMonthIncomes(current_Month);
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
        listIncomes.setAdapter(adapter);

        btnaddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
            }
        });

        listIncomes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    private void addIncome(){
        Intent intent = new Intent(Income.this,Income_adder.class);
        startActivity(intent);
    }

    private void long_click_activity(final int position){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_delete_design);

        Object obj = listIncomes.getItemAtPosition(position);
        final Income_ExpenseModel model =(Income_ExpenseModel)obj;

        final String account_name=model.getAccount();
        final String income_amount=model.getAmount();
        final String income_date=model.getDate();
        final String income_time=model.getTime();

        dialog.setTitle(account_name);
        TextView txtEdit = (TextView)dialog.findViewById(R.id.txtEdit);
        TextView txtDelete = (TextView)dialog.findViewById(R.id.txtDelete);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit=true;
                String contact_name=model.getContact();
                String category=model.getCategory();
                String income_notes=model.getNotes();

                Intent intent = new Intent(Income.this,Income_adder.class);
                intent.putExtra("account",account_name);
                intent.putExtra("contact",contact_name);
                intent.putExtra("category",category);
                intent.putExtra("amount",income_amount);
                intent.putExtra("date",income_date);
                intent.putExtra("time",income_time);
                intent.putExtra("notes",income_notes);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incomeDBHelper.deleteIncome(income_date,income_time);
                accountDBHelper.decreaseCurrentBalance(account_name,income_amount);
                onResume();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

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

public class Account extends Activity {

    final Context context=this;

    private Button btnAddAccount;
    private ListView listAccounts;
    private TextView totalAccount;
    private AccountDBHelper accountDBHelper;
    private IncomeDBHelper incomeDBHelper;
    private ExpenseDBHelper expenseDBHelper;
    public static ArrayList<AccountModel> models;
    public static boolean isItemClickable=false;
    public static AccountAdapter adapter;
    public static boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        isEdit=false;
        btnAddAccount=(Button)findViewById(R.id.btnAddAccount);
        totalAccount=(TextView)findViewById(R.id.textTotalAccount);
        listAccounts=(ListView) findViewById(R.id.listViewAccounts);
        accountDBHelper=new AccountDBHelper(this);
        models=accountDBHelper.getAllAccounts();

        Collections.sort(models,new Comparator<AccountModel>() {
            @Override
            public int compare(AccountModel a, AccountModel b) {
                if(a.getAccountName().compareTo(b.getAccountName())>0) return 1;
                else return -1;
            }
        });
        adapter=new AccountAdapter(this,models);
        listAccounts.setAdapter(adapter);

        totalAccount.setText(accountDBHelper.getTotalAccount());

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit=false;
                Intent intent = new Intent(Account.this,Account_adder.class);
                startActivity(intent);
            }
        });

        listAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item_click_activity(position);
            }
        });

        listAccounts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    private void item_click_activity(int position){

        if(isItemClickable) {
            Intent intent = new Intent();
            Object obj = listAccounts.getItemAtPosition(position);
            final AccountModel model = (AccountModel) obj;
            intent.putExtra("accountName", model.getAccountName());
            setResult(Activity.RESULT_OK, intent);
            isItemClickable=false;
            finish();
        }
    }

    private void longClick_activity(final int position){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_delete_design);

        Object obj = listAccounts.getItemAtPosition(position);
        final AccountModel model =(AccountModel)obj;

        final String accountName=model.getAccountName();
        final String openingBalance=model.getOpeningBalance();
        final String notes=model.getNotes();

        dialog.setTitle(accountName);
        TextView txtEdit = (TextView)dialog.findViewById(R.id.txtEdit);
        TextView txtDelete = (TextView)dialog.findViewById(R.id.txtDelete);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEdit=true;
                Intent intent = new Intent(Account.this,Account_adder.class);
                intent.putExtra("accountName",accountName);
                intent.putExtra("openingBalance",openingBalance);
                intent.putExtra("notes",notes);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog2=new Dialog(context);
                dialog2.setContentView(R.layout.confirmation);
                dialog2.setTitle("Confirm");

                Button btnYes=(Button)dialog2.findViewById(R.id.btnYES);
                Button btnNo=(Button)dialog2.findViewById(R.id.btnNO);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        incomeDBHelper=new IncomeDBHelper(Account.this);
                        expenseDBHelper=new ExpenseDBHelper(Account.this);

                        accountDBHelper.deleteAccount(accountName);
                        incomeDBHelper.deleteIncome(accountName);
                        expenseDBHelper.deleteExpense(accountName);
                        onResume();
                        dialog2.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                dialog2.show();

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

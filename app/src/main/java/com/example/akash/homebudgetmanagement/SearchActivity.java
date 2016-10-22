package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SearchActivity extends Activity {

    final Context context=this;
    private Button btnIncomeSearch;
    private Button btnExpenseSearch;
    private Button btnBudgetSearch;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnIncomeSearch = (Button) findViewById(R.id.btnIncomeSearch);
        btnExpenseSearch = (Button) findViewById(R.id.btnExpenseSearch);
        btnBudgetSearch = (Button) findViewById(R.id.btnBudgetSearch);

        btnIncomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.income_expense_search_dialog_item);
                dialog.setTitle("Income");

                TextView txtSearchByDay=(TextView)dialog.findViewById(R.id.textSearchByDay);
                TextView txtSearchByMonth=(TextView)dialog.findViewById(R.id.textSearchByMonth);

                txtSearchByDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SearchActivity.this,SearchType_Activity.class);
                        intent.putExtra("name","Income");
                        intent.putExtra("type","search_day");
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                txtSearchByMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchActivity.this,SearchType_Activity.class);
                        intent.putExtra("name","Income");
                        intent.putExtra("type","search_month");
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        btnExpenseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.income_expense_search_dialog_item);
                dialog.setTitle("Expense");

                TextView txtSearchByDay=(TextView)dialog.findViewById(R.id.textSearchByDay);
                TextView txtSearchByMonth=(TextView)dialog.findViewById(R.id.textSearchByMonth);

                txtSearchByDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SearchActivity.this,SearchType_Activity.class);
                        intent.putExtra("name","Expense");
                        intent.putExtra("type","search_day");
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                txtSearchByMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SearchActivity.this,SearchType_Activity.class);
                        intent.putExtra("name","Expense");
                        intent.putExtra("type","search_month");
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        btnBudgetSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this,SearchType_Activity.class);
                intent.putExtra("name","Budget");
                intent.putExtra("type","search_month");
                startActivity(intent);

            }
        });

    }

}
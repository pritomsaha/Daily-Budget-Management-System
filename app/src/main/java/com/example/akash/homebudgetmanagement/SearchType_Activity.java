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
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class SearchType_Activity extends Activity {

    private TextView header;
    private TextView date;
    private TextView btnGo;
    static final int DATE_DIALOG_ID = 998;
    private Calendar _calendar;
    private int year;
    private int month;
    private int day;
    private String current_Date;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_type_);

        header=(TextView)findViewById(R.id.Header);
        date=(EditText)findViewById(R.id.eDitText_Date);
        btnGo=(Button)findViewById(R.id.btnGo);

        Intent intent=getIntent();
        header.setText(intent.getStringExtra("name"));
        type=intent.getStringExtra("type");

        setCurrentDateOnDatePicker();
        _calendar = Calendar.getInstance(Locale.getDefault());

        if(type.compareTo("search_day")==0)current_Date=Income.df1.format(Income.calendar.getTime());
        else current_Date=Income.df2.format(Income.calendar.getTime());

        date.setText(current_Date);

        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                go_activity();
            }
        });



    }

    private void go_activity(){

        Intent data=new Intent(this,ShowSearchActivity.class);
        data.putExtra("name",header.getText().toString());
        data.putExtra("type",type);
        data.putExtra("date",date.getText().toString());
        startActivity(data);

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
            if(type.compareTo("search_day")==0)date.setText(Income.df1.format(_calendar.getTime()));
            else date.setText(Income.df2.format(_calendar.getTime()));
        }

    };

    private void setCurrentDateOnDatePicker() {

        year = Income.calendar.get(Calendar.YEAR);
        month = Income.calendar.get(Calendar.MONTH);
        day = Income.calendar.get(Calendar.DAY_OF_MONTH);
    }

}

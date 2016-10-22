package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class Statistics_activity extends Activity {

    private RadioGroup radioGroup;
    private RadioButton selectButton;
    private Button btnGo;
    private EditText Month;

    static final int DATE_DIALOG_ID = 998;
    private Calendar _calendar;
    private int year;
    private int month;
    private int day;
    private String type;

    private IncomeDBHelper incomeDBHelper;
    private ExpenseDBHelper expenseDBHelper;
    private BudgetDBHelper budgetDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        btnGo=(Button)findViewById(R.id.buttonGo);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroupStat);
        Month=(EditText)findViewById(R.id.editTextSelectedMonth);
        incomeDBHelper=new IncomeDBHelper(this);
        expenseDBHelper=new ExpenseDBHelper(this);
        budgetDBHelper=new BudgetDBHelper(this);

        setCurrentDateOnDatePicker();
        _calendar = Calendar.getInstance(Locale.getDefault());
        Month.setText(Income.df2.format(_calendar.getTime()));

        Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int selectedId=radioGroup.getCheckedRadioButtonId();
                    selectButton = (RadioButton) findViewById(selectedId);
                    type=selectButton.getText().toString();
                    openChart();

                }catch (NullPointerException e){
                    Toast.makeText(getBaseContext(),"Select an Option",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void openChart(){

        if(type.compareTo("Income Vs Expense")==0){

            Double totalIncome=incomeDBHelper.getTotalIncome(Month.getText().toString());
            Double totalExpense=expenseDBHelper.getTotalExpense(Month.getText().toString());

            String[] code={"Income","Expense"};

            Double[] distribution = new Double[2];

            distribution[0] =(totalIncome*100)/(totalIncome+totalExpense);
            distribution[1]=100-distribution[0];

            int[] colors={Color.GREEN,Color.RED};

            CategorySeries distributionSeries = new CategorySeries("Income Vs Expense");

            for(int i=0 ;i < distribution.length;i++){
                // Adding a slice with its values and name to the Pie Chart
                distributionSeries.add(code[i], distribution[i]);
            }

            DefaultRenderer defaultRenderer  = new DefaultRenderer();
            for(int i = 0 ;i<distribution.length;i++){
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setColor(colors[i]);
                seriesRenderer.setDisplayChartValues(true);
                // Adding a renderer for a slice
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }

            defaultRenderer.setChartTitle(type+"\n\n"+Month.getText().toString());
            defaultRenderer.setChartTitleTextSize(30);
            defaultRenderer.getMargins();

            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setLabelsTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);
            defaultRenderer.isApplyBackgroundColor();
            defaultRenderer.setBackgroundColor(Color.CYAN);
            defaultRenderer.setLabelsColor(Color.BLACK);


            //defaultRenderer.setLabelsColor(000);

            Intent in = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");

            // Start Activity
            startActivity(in);

    }
    else if(type.compareTo("Expense Vs Budget")==0){

            Double totalBudget=budgetDBHelper.getTotalBudget(Month.getText().toString())[0];
            Double totalExpense=expenseDBHelper.getTotalExpense(Month.getText().toString());

            String[] code={"Budget","Expense"};

            Double[] distribution = new Double[2];

            distribution[0] =(totalBudget*100)/(totalBudget+totalExpense);
            distribution[1]=100-distribution[0];

            int[] colors={Color.GREEN,Color.RED};

            CategorySeries distributionSeries = new CategorySeries("Budget Vs Expense");

            for(int i=0 ;i < distribution.length;i++){
                // Adding a slice with its values and name to the Pie Chart
                distributionSeries.add(code[i], distribution[i]);
            }

            DefaultRenderer defaultRenderer  = new DefaultRenderer();
            for(int i = 0 ;i<distribution.length;i++){
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setColor(colors[i]);
                seriesRenderer.setDisplayChartValues(true);
                // Adding a renderer for a slice
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }

            defaultRenderer.setChartTitle(type+"\n\n"+Month.getText().toString());
            defaultRenderer.setChartTitleTextSize(30);
            defaultRenderer.getMargins();

            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setLabelsTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);
            defaultRenderer.setBackgroundColor(Color.CYAN);
            defaultRenderer.setLabelsColor(Color.BLACK);

            //defaultRenderer.setLabelsColor(000);

            Intent in = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");

            // Start Activity
            startActivity(in);

        }

        else if(type.compareTo("Income by Category")==0){

            Double totalIncome=incomeDBHelper.getTotalIncome(Month.getText().toString());
            Map<String,Double> categories=incomeDBHelper.getCategoryWiseIncome(Month.getText().toString());

            Set<String> set=categories.keySet();
            List<String> code=new ArrayList<>(set);
            ArrayList<Double> distribution=new ArrayList<>();


            for (String category:set){
                distribution.add(((categories.get(category)*100)/totalIncome));
            }

            CategorySeries distributionSeries = new CategorySeries(" Income by Category");

            for(int i=0 ;i < distribution.size();i++){
                // Adding a slice with its values and name to the Pie Chart
                distributionSeries.add(code.get(i),distribution.get(i));
            }

            DefaultRenderer defaultRenderer  = new DefaultRenderer();
            for(int i = 0 ;i<distribution.size();i++){
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setDisplayChartValues(true);
                seriesRenderer.setHighlighted(true);
                // Adding a renderer for a slice
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }

            defaultRenderer.setChartTitle(type+"\n\n"+Month.getText().toString());
            defaultRenderer.setChartTitleTextSize(30);
            defaultRenderer.getMargins();

            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setLabelsTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);

            //defaultRenderer.isApplyBackgroundColor();
            //defaultRenderer.setBackgroundColor(Color.CYAN);
            defaultRenderer.setLabelsColor(Color.BLACK);


            //defaultRenderer.setLabelsColor(000);

            Intent in = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");

            // Start Activity
            startActivity(in);

        }

        else if(type.compareTo("Expense by Category")==0){

            Double totalIncome=expenseDBHelper.getTotalExpense(Month.getText().toString());
            Map<String,Double> categories=expenseDBHelper.getCategoryWiseExpense(Month.getText().toString());

            Set<String> set=categories.keySet();
            List<String> code=new ArrayList<>(set);
            ArrayList<Double> distribution=new ArrayList<>();


            for (String category:set){
                distribution.add(((categories.get(category)*100)/totalIncome));
            }

            CategorySeries distributionSeries = new CategorySeries(" Expense by Category");

            for(int i=0 ;i < distribution.size();i++){
                // Adding a slice with its values and name to the Pie Chart
                distributionSeries.add(code.get(i),distribution.get(i));
            }

            DefaultRenderer defaultRenderer  = new DefaultRenderer();
            for(int i = 0 ;i<distribution.size();i++){
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setDisplayChartValues(true);
                seriesRenderer.setHighlighted(true);
                // Adding a renderer for a slice
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }

            defaultRenderer.setChartTitle(type+"\n\n"+Month.getText().toString());
            defaultRenderer.setChartTitleTextSize(30);
            defaultRenderer.getMargins();

            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setLabelsTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);

            //defaultRenderer.isApplyBackgroundColor();
            //defaultRenderer.setBackgroundColor(Color.CYAN);
            defaultRenderer.setLabelsColor(Color.BLACK);
            //defaultRenderer.setLabelsColor(000);

            Intent in = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");

            // Start Activity
            startActivity(in);

        }

        else if(type.compareTo("Income by Contact")==0){

            Double totalIncome=incomeDBHelper.getTotalIncome(Month.getText().toString());
            Map<String,Double> categories=incomeDBHelper.getContactWiseIncome(Month.getText().toString());

            Set<String> set=categories.keySet();
            List<String> code=new ArrayList<>(set);
            ArrayList<Double> distribution=new ArrayList<>();


            for (String category:set){
                distribution.add(((categories.get(category)*100)/totalIncome));
            }

            CategorySeries distributionSeries = new CategorySeries(" Income by Contact");

            for(int i=0 ;i < distribution.size();i++){
                // Adding a slice with its values and name to the Pie Chart
                distributionSeries.add(code.get(i),distribution.get(i));
            }

            DefaultRenderer defaultRenderer  = new DefaultRenderer();
            for(int i = 0 ;i<distribution.size();i++){
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setDisplayChartValues(true);
                seriesRenderer.setHighlighted(true);
                // Adding a renderer for a slice
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }

            defaultRenderer.setChartTitle(type+"\n\n"+Month.getText().toString());
            defaultRenderer.setChartTitleTextSize(30);
            defaultRenderer.getMargins();

            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setLabelsTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);

            //defaultRenderer.isApplyBackgroundColor();
            //defaultRenderer.setBackgroundColor(Color.CYAN);
            defaultRenderer.setLabelsColor(Color.BLACK);


            //defaultRenderer.setLabelsColor(000);

            Intent in = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");

            // Start Activity
            startActivity(in);

        }
        else if(type.compareTo("Expense by Contact")==0){

            Double totalIncome=expenseDBHelper.getTotalExpense(Month.getText().toString());
            Map<String,Double> categories=expenseDBHelper.getContactWiseExpense(Month.getText().toString());

            Set<String> set=categories.keySet();
            List<String> code=new ArrayList<>(set);
            ArrayList<Double> distribution=new ArrayList<>();


            for (String category:set){
                distribution.add(((categories.get(category)*100)/totalIncome));
            }

            CategorySeries distributionSeries = new CategorySeries(" Expense by Contact");

            for(int i=0 ;i < distribution.size();i++){
                // Adding a slice with its values and name to the Pie Chart
                distributionSeries.add(code.get(i),distribution.get(i));
            }

            DefaultRenderer defaultRenderer  = new DefaultRenderer();
            for(int i = 0 ;i<distribution.size();i++){
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setDisplayChartValues(true);
                seriesRenderer.setHighlighted(true);
                // Adding a renderer for a slice
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }
            defaultRenderer.setChartTitle(type+"\n\n"+Month.getText().toString());
            defaultRenderer.setChartTitleTextSize(30);
            defaultRenderer.getMargins();

            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setLabelsTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);

            //defaultRenderer.isApplyBackgroundColor();
            //defaultRenderer.setBackgroundColor(Color.CYAN);
            defaultRenderer.setLabelsColor(Color.BLACK);
            Intent in = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");

            // Start Activity
            startActivity(in);
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
            Month.setText(Income.df2.format(_calendar.getTime()));
        }

    };

    private void setCurrentDateOnDatePicker() {

        year = Income.calendar.get(Calendar.YEAR);
        month = Income.calendar.get(Calendar.MONTH);
        day = Income.calendar.get(Calendar.DAY_OF_MONTH);
    }
}

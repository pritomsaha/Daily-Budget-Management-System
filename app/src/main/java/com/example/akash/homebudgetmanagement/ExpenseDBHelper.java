package com.example.akash.homebudgetmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akash on 6/11/2015.
 */
public class ExpenseDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBExpense.db";
    public static final String TABLE_NAME = "expenses";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_EXPENSE_BALANCE = "expense";
    public static final String COLUMN_ACCOUNTS = "account";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NOTES = "notes";


    public ExpenseDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table expenses " +
                        "(id integer primary key, date text, month text, time text, expense text, account text,contact text, category text, notes text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(db);
    }

    public  boolean insertExpense(String date,String month,String time, String expense,String account,String contact,String category,String notes){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date",date);
        contentValues.put("month",month);
        contentValues.put("time",time);
        contentValues.put("expense",expense);
        contentValues.put("account",account);
        contentValues.put("contact",contact);
        contentValues.put("category",category);
        contentValues.put("notes",notes);

        db.insert("expenses", null, contentValues);
        return true;
    }

    public  void deleteExpense(String date,String time){

        String deleteExpense="DELETE FROM expenses WHERE date='" + date +"' and time='"+time+"'";

        this.getWritableDatabase().execSQL(deleteExpense);
    }

    public  void deleteExpense(String account){

        String deleteExpense="DELETE FROM expenses WHERE account='"+account+"'";

        this.getWritableDatabase().execSQL(deleteExpense);
    }


    public Double getCategoryTotalExpense(String month,String category){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM expenses WHERE month='"+month+"' and category='"+category+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        Double total=0.0;

        if(res.moveToFirst()){

            while (res.isAfterLast()==false){

                total+=Double.parseDouble(res.getString(4));
                res.moveToNext();
            }
        }

        return total;
    }

    public void upDateRow(String oldDate,String oldTime,String date,String month,String time, String expense,String account,String contact,String category,String notes){

        SQLiteDatabase db = this.getWritableDatabase();
        String selectRow="SELECT *FROM expenses WHERE date='" + oldDate +"' and time='"+oldTime+"'";
        ContentValues contentValues = new ContentValues();
        Cursor res = db.rawQuery(selectRow, null);

        if(res.moveToFirst()) {

            contentValues.put("date", date);
            contentValues.put("month", month);
            contentValues.put("time", time);
            contentValues.put("expense", expense);
            contentValues.put("account", account);
            contentValues.put("contact", contact);
            contentValues.put("category", category);
            contentValues.put("notes", notes);

            db.update("expenses", contentValues,"date =? and time =?",
                    new String[]{oldDate,oldTime});

        }
    }

    public void upDateRow(String oldAccount,String newAccount){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account",newAccount);
        db.update("expenses",contentValues,"account=?",new String[]{oldAccount});

    }

    public ArrayList<Income_ExpenseModel> getCurrentMonthExpense(String currentMonth){

        ArrayList<Income_ExpenseModel> expenseModels = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM expenses WHERE month='"+currentMonth+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if(res.moveToFirst()){

            while (res.isAfterLast()==false){

                   expenseModels.add(new Income_ExpenseModel(res.getString(1), res.getString(3), res.getString(4),
                            res.getString(5), res.getString(6), res.getString(7), res.getString(8)));

                res.moveToNext();
            }
        }

        return expenseModels;
    }

    public ArrayList<Income_ExpenseModel> getCurrentDayExpenses(String currentDay){

        ArrayList<Income_ExpenseModel> expenseModels = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM expenses WHERE date='"+currentDay+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if(res.moveToFirst()){

            while (res.isAfterLast()==false){

                expenseModels.add(new Income_ExpenseModel(res.getString(1), res.getString(3), res.getString(4),
                        res.getString(5), res.getString(6), res.getString(7), res.getString(8)));

                res.moveToNext();
            }
        }

        return expenseModels;
    }


    public Double getTotalExpense(String month){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM expenses WHERE month='"+month+"'";
        Cursor res =  db.rawQuery( selectRow, null );
        Double total=0.0;

        if (res.moveToFirst()){
            while (res.isAfterLast()==false){
                total+=Double.parseDouble(res.getString(4));
                res.moveToNext();
            }
        }
        return total;
    }

    public Map<String,Double> getCategoryWiseExpense(String month){

        Map<String,Double> categories = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM expenses WHERE month='"+month+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if (res.moveToFirst()){

            while (res.isAfterLast()==false){

                String key=res.getString(7);
                Double value=Double.parseDouble(res.getString(4));
                if(categories.containsKey(key)){
                    Double amount=categories.get(key);
                    categories.put(key,value+amount);
                }else categories.put(key,value);

                res.moveToNext();
            }
        }
        return categories;

    }

    public Map<String,Double> getContactWiseExpense(String month){

        Map<String,Double> contacts = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM expenses WHERE month='"+month+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if (res.moveToFirst()){

            while (res.isAfterLast()==false){

                String key=res.getString(6);
                Double value=Double.parseDouble(res.getString(4));
                if(contacts.containsKey(key)){
                    Double amount=contacts.get(key);
                    contacts.put(key,value+amount);
                }else contacts.put(key,value);

                res.moveToNext();
            }
        }
        return contacts;
    }
}

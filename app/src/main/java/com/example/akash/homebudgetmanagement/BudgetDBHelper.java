package com.example.akash.homebudgetmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Akash on 6/12/2015.
 */
public class BudgetDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBBudget.db";
    public static final String TABLE_NAME = "budgets";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_EXPENSE_BALANCE = "budget";
    public static final String COLUMN_SPENT = "spent";
    public static final String COLUMN_LEFT = "left";
    public static final String COLUMN_NOTES = "notes";

    public BudgetDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table budgets " +
                        "(id integer primary key, month text, category text, budget text, spent text, left text, notes text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS budgets");
        onCreate(db);
    }

    public boolean insertBudget(String month,String category,String budget,String spent,
                                String left,String notes){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("month",month);
        contentValues.put("category",category);
        contentValues.put("budget",budget);
        contentValues.put("spent",spent);
        contentValues.put("left",left);
        contentValues.put("notes",notes);

        db.insert("budgets", null, contentValues);
        return true;
    }

    public void spentBudget(String month,String category,String spentAmount){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        String selectRow="SELECT *FROM budgets WHERE month='"+month+"' and category='"+category+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if(res.moveToFirst()){

            Double Spent=Double.parseDouble(res.getString(4))+Double.parseDouble(spentAmount);
            Double Left=Double.parseDouble(res.getString(3))-Spent;
            contentValues.put("spent",Spent.toString());
            contentValues.put("left",Left.toString());

            db.update("budgets",contentValues,"month =? and category =?",new String[]{month,category});
        }

    }

    public void decreaseSpent(String month,String category,String spentAmount){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        String selectRow="SELECT *FROM budgets WHERE month='"+month+"' and category='"+category+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if(res.moveToFirst()){

            Double Spent=Double.parseDouble(res.getString(4))-Double.parseDouble(spentAmount);
            Double Left=Double.parseDouble(res.getString(3))-Spent;
            contentValues.put("spent",Spent.toString());
            contentValues.put("left",Left.toString());

            db.update("budgets",contentValues,"month =? and category =?",new String[]{month,category});
        }

    }


    public void upDateRow(String month,String oldCategory, String category,String budget,String spent,String left,String notes){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        String selectRow="SELECT *FROM budgets WHERE month='"+month+"' and category='"+oldCategory+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if(res.moveToFirst()){

            contentValues.put("category",category);
            contentValues.put("budget",budget);
            contentValues.put("spent",spent);
            contentValues.put("left",left);
            contentValues.put("notes",notes);

            db.update("budgets",contentValues,"month =? and category =?",new String[]{month,oldCategory});
        }
    }

    public void deleteBudget(String month,String category){

        String deleteBudget="DELETE FROM budgets WHERE month='" + month +"' and category='"+category+"'";

        this.getWritableDatabase().execSQL(deleteBudget);
    }

    public ArrayList<BudgetModel> getCurrentMonthBudgets(String currentMonth){

        ArrayList<BudgetModel> budgetModels = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM budgets WHERE month='"+currentMonth+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if(res.moveToFirst()){

            while (res.isAfterLast()==false){

                budgetModels.add(new BudgetModel(res.getString(2),
                        res.getString(3),res.getString(4),res.getString(5),res.getString(6)));

                res.moveToNext();
            }
        }
        return budgetModels;
    }

    public boolean search(String month,String category){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM budgets WHERE month='"+month+"' and category='"+category+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if (res.moveToFirst()) return true;

        return false;
    }

    public Double[] getTotalBudget(String month){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM budgets WHERE month='"+month+"'";
        Cursor res =  db.rawQuery( selectRow, null );
        Double[] total={0.0,0.0};

        if(res.moveToFirst()){

            while (res.isAfterLast()==false){
                total[0]+=Double.parseDouble(res.getString(3));
                total[1]+=Double.parseDouble(res.getString(4));
                res.moveToNext();
            }
        }

        return total;
   }

    public String[] getTotalBudgetAndSpent(String currentMonth){

        String bd[]={"0","0","0"};
        Double percentage=0.0,budget=0.0,spent=0.0;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRow="SELECT *FROM budgets WHERE month='"+currentMonth+"'";
        Cursor res =  db.rawQuery( selectRow, null );

        if(res.moveToFirst()){

            while (res.isAfterLast()==false){

                budget+=Double.parseDouble(res.getString(3));
                spent+=Double.parseDouble(res.getString(4));

                res.moveToNext();
            }
        }

        bd[0]=MainActivity.getDoubleText(budget);
        bd[1]=MainActivity.getDoubleText(spent);
        percentage=(spent/budget)*100;
        percentage=Math.floor(percentage);

        if (bd[1].compareTo("0")==0)bd[2]="0";
        else if(percentage>100) bd[2]=">"+100;
        else bd[2]=MainActivity.getDoubleText(percentage);

        return bd;
    }

}

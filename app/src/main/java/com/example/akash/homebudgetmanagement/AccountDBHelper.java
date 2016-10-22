package com.example.akash.homebudgetmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Akash on 6/10/2015.
 */
public class AccountDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBAccount.db";
    public static final String ACCOUNTS_TABLE_NAME = "accounts";
    public static final String ACCOUNTS_COLUMN_ID = "id";
    public static final String ACCOUNTS_COLUMN_ACCOUNT_NAME = "accountname";
    public static final String ACCOUNTS_COLUMN_OPENING_BALANCE = "openingbalance";
    public static final String ACCOUNTS_COLUMN_CURRENT_BALANCE = "currentbalance";
    public static final String ACCOUNTS_COLUMN_CURRENT_NOTES = "accountnotes";
    public AccountDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table accounts " +
                        "(id integer primary key, accountname text,openingbalance text,currentbalance text,accountnotes text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS accounts");
        onCreate(db);
    }

    public boolean insertAccount(String accountName,String openingBalance,String currentBalance,String accountNotes){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("accountname", accountName);
        contentValues.put("openingbalance", openingBalance);
        contentValues.put("currentbalance", currentBalance);
        contentValues.put("accountnotes",accountNotes);
        db.insert("accounts", null, contentValues);
        return true;
    }

    public void increaseCurrentBalance(String name,String balance){

        SQLiteDatabase db = this.getWritableDatabase();
        String CurrentBalance="0";
        String selectRow="SELECT *FROM accounts WHERE accountname='"+name+"'";
        ContentValues contentValues=new ContentValues();
        Cursor res =  db.rawQuery( selectRow, null );

        if(res.moveToFirst()) {
            CurrentBalance = res.getString(3);
            if (CurrentBalance==null)CurrentBalance="0";
            Double temp = (Double.parseDouble(CurrentBalance) + Double.parseDouble(balance));
            CurrentBalance = MainActivity.getDoubleText(temp);
            contentValues.put("currentbalance", CurrentBalance);

            db.update("accounts", contentValues, "accountname =?",
                    new String[]{name});
            //this.deleteAccount(name);
        }
    }

    public void decreaseCurrentBalance(String name,String balance) {

        SQLiteDatabase db = this.getWritableDatabase();
        String CurrentBalance = "0";
        String selectRow = "SELECT *FROM accounts WHERE accountname='" + name + "'";
        ContentValues contentValues = new ContentValues();
        Cursor res = db.rawQuery(selectRow, null);

        if (res.moveToFirst()) {
            CurrentBalance = res.getString(3);
            if (CurrentBalance==null)CurrentBalance="0";
            Double temp = (Double.parseDouble(CurrentBalance) - Double.parseDouble(balance));
            CurrentBalance = MainActivity.getDoubleText(temp);
            contentValues.put("currentbalance", CurrentBalance);

            db.update("accounts", contentValues, "accountname =?",
                    new String[]{name});
            //this.deleteAccount(name);
        }
    }


    public void upDateRow(String OldName,String OldOpeningBalance,String newName,String newOpeningBalance,String notes){

        SQLiteDatabase db = this.getWritableDatabase();
        String CurrentBalance="0";
        String selectRow="SELECT *FROM accounts WHERE accountname='"+OldName+"'";

        ContentValues contentValues=new ContentValues();
        Cursor res =  db.rawQuery( selectRow, null );
        if(res.moveToFirst()) {
            CurrentBalance = res.getString(3);

            Double temp = (Double.parseDouble(CurrentBalance) - Double.parseDouble(OldOpeningBalance)
                    + Double.parseDouble(newOpeningBalance));

            CurrentBalance = MainActivity.getDoubleText(temp);

            contentValues.put("accountname", newName);
            contentValues.put("openingbalance", newOpeningBalance);
            contentValues.put("currentbalance", CurrentBalance);
            contentValues.put("accountnotes", notes);

            db.update("accounts", contentValues, "accountname =?",
                    new String[]{OldName});
            //this.deleteAccount(OldName);
        }

    }

    public void deleteAccount(String accountName){

        String deleteAccount="DELETE FROM accounts WHERE accountname='"+accountName+"'";
        this.getWritableDatabase().execSQL(deleteAccount);
    }

    public ArrayList<AccountModel> getAllAccounts()
    {
        ArrayList<AccountModel> array_list = new ArrayList<AccountModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from accounts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new AccountModel(res.getString(res.getColumnIndex(ACCOUNTS_COLUMN_ACCOUNT_NAME)),
                    res.getString(res.getColumnIndex(ACCOUNTS_COLUMN_OPENING_BALANCE)),
                    res.getString(res.getColumnIndex(ACCOUNTS_COLUMN_CURRENT_BALANCE)),
                    res.getString(res.getColumnIndex(ACCOUNTS_COLUMN_CURRENT_NOTES))));

            res.moveToNext();
        }
        return array_list;
    }

    public String getTotalAccount(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRaw = "SELECT *FROM accounts";
        Cursor res = db.rawQuery(selectRaw,null);

        Double Total=0.0;

        if(res.moveToFirst()){
            while (res.isAfterLast()==false){
                Total+=Double.parseDouble(res.getString(3));

                res.moveToNext();
            }
        }
        return MainActivity.getDoubleText(Total);
    }

    public boolean searchAccountName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectName = "SELECT *FROM accounts WHERE accountname='"+name+"'";
        Cursor res = db.rawQuery(selectName,null);
        //res.moveToFirst();

        if(res.moveToFirst()) return true;

        return false;
    }

    public ArrayList<String> getAllAccountsNames(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectRaw = "SELECT *FROM accounts";
        Cursor res = db.rawQuery(selectRaw,null);
        ArrayList<String> stringArrayList=new ArrayList<>();

        if(res.moveToFirst()){
            while (res.isAfterLast()==false){

                stringArrayList.add(res.getString(1));
                res.moveToNext();
            }
        }

        return stringArrayList;
    }
}

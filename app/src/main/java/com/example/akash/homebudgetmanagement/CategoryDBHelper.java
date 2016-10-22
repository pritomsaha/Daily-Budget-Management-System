package com.example.akash.homebudgetmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Akash on 6/9/2015.
 */
public class CategoryDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBCategoryName.db";
    public static final String CATEGORY_TABLE_NAME = "category";
    public static final String CATEGORY_COLUMN_ID = "id";
    public static final String CATEGORY_COLUMN_CATEGORYNAME = "categoryName";

    public CategoryDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table category " +
                        "(id integer primary key,categoryName text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertCategory(String categoryName){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categoryName",categoryName);
        db.insert("category",null,contentValues);
        return true;
    }

    public void deleteCategory(String categoryName){

        String deleteCategory="DELETE FROM category WHERE categoryName='"+categoryName+"'";
        this.getWritableDatabase().execSQL(deleteCategory);
    }

    public ArrayList<String> getAllCategory()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from category", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CATEGORY_COLUMN_CATEGORYNAME)));
            res.moveToNext();
        }
        return array_list;
    }
}

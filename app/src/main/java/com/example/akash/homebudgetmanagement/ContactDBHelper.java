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
public class ContactDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBContactName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_ADDRESS = "address";

    public ContactDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,phone text,email text, address text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact  (String name, String phone, String email, String address)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("address", address);
        db.insert("contacts", null, contentValues);
        return true;
    }

    public void deleteContact(String Name){

        String deleteContact="DELETE FROM contacts WHERE name='"+Name+"'";
        this.getWritableDatabase().execSQL(deleteContact);
    }

    public void deleteContact(String name,String phoneNumber,String email,String address){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contacts","name=? and phone=? and email=? and address=?",new String[]{name,phoneNumber,email,address});
    }

    public void udDateRow(String oldName,String newName,String newPhone,String newEmail,String newAddress){

        SQLiteDatabase db = this.getWritableDatabase();
        String SelectRow="SELECT *FROM contacts WHERE name='"+oldName+"'";
        ContentValues contentValues = new ContentValues();

        Cursor res =  db.rawQuery( SelectRow, null );
        if(res.moveToFirst()) {

            contentValues.put("name",newName);
            contentValues.put("phone", newPhone);
            contentValues.put("email", newEmail);
            contentValues.put("address", newAddress);

            db.update("contacts", contentValues, "name =?",
                    new String[]{oldName});
        }

    }

    public boolean search(String name){

        SQLiteDatabase db = this.getWritableDatabase();
        String SelectRow="SELECT *FROM contacts WHERE name='"+name+"'";
        Cursor res =  db.rawQuery( SelectRow, null );

        if(res.moveToFirst()) return true;
        return false;
    }

    public ArrayList<ContactModel> getAllContacts()
    {
        ArrayList<ContactModel> array_list = new ArrayList<ContactModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );

        if(res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                array_list.add(new ContactModel(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)),
                        res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE)),
                        res.getString(res.getColumnIndex(CONTACTS_COLUMN_EMAIL)),
                        res.getString(res.getColumnIndex(CONTACTS_COLUMN_ADDRESS))));

                res.moveToNext();
            }
        }
        return array_list;
    }
}

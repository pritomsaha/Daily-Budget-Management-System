package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Contacts extends Activity {

    final Context context=this;
    private static final int contactInfo=0;
    public static boolean isItemClickable=false;

    private ContactDBHelper contactDBHelper;
    private ListView listContacts;
    private Button addContact;
    public static ArrayList<ContactModel> models = new ArrayList<>();
    private ContactAdapter adapter;
    public static boolean isEdit;
    private String elementName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        isEdit=false;
        listContacts=(ListView) findViewById(R.id.ContactlistView);
        contactDBHelper=new ContactDBHelper(this);
        models=contactDBHelper.getAllContacts();
        Collections.sort(models,new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel a, ContactModel b) {

                if (a.getName().compareTo(b.getName())>0)return 1;
                else return -1;
            }
        });

        adapter = new ContactAdapter(this,models);
        listContacts.setAdapter(adapter);

        addContact=(Button) findViewById(R.id.btnContact);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Contacts.this,Contact_adder.class);
                startActivityForResult(intent,contactInfo);
            }
        });

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item_click_activity(position);
            }
        });

        listContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==contactInfo){
            if(resultCode==Activity.RESULT_OK){
                ArrayList<String> resources = data.getStringArrayListExtra("resources");
                contactDBHelper.udDateRow(elementName,resources.get(0), resources.get(1), resources.get(2), resources.get(3));
            }
            else{
                Log.d("MainActivity", "AddCategory Activity cancelled");}
        }
    }

    private void item_click_activity(int position){

        if(isItemClickable) {
            Intent intent = new Intent();
            Object obj = listContacts.getItemAtPosition(position);
            final ContactModel model = (ContactModel) obj;
            intent.putExtra("contact", model.getName());
            setResult(Activity.RESULT_OK, intent);
            isItemClickable=false;
            finish();
        }
    }

    private void longClick_activity(final int position){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_delete_design);
        Object obj = listContacts.getItemAtPosition(position);
        final ContactModel model=(ContactModel)obj;

        elementName=model.getName();
        final String elementPhoneNumber=model.getPhoneNumber();
        final String elementEmail=model.getEmail();
        final String elementAddress=model.getAddress();

        dialog.setTitle(elementName);
        TextView txtEdit = (TextView)dialog.findViewById(R.id.txtEdit);
        TextView txtDelete = (TextView)dialog.findViewById(R.id.txtDelete);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEdit=true;
                Intent intent = new Intent(Contacts.this,Contact_adder.class);
                intent.putExtra("name",elementName);
                intent.putExtra("phoneNumber",elementPhoneNumber);
                intent.putExtra("email",elementEmail);
                intent.putExtra("address",elementAddress);
                startActivityForResult(intent, contactInfo);
                dialog.dismiss();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactDBHelper.deleteContact(elementName);
                onResume();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

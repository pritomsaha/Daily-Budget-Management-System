package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class Contact_adder extends Activity {

    private Button btnSave;
    public static EditText name;
    public static EditText contactNum;
    private EditText email;
    private EditText address;
    private ContactDBHelper contactDBHelper;
    //private ArrayList<String> stringArrayList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        name=(EditText)findViewById(R.id.editTextName);
        contactNum=(EditText)findViewById(R.id.editTextContactNum);
        email=(EditText)findViewById(R.id.editTextEmail);
        address=(EditText)findViewById(R.id.editTextAddress);
        btnSave=(Button) findViewById(R.id.btnContactSave);

        contactDBHelper=new ContactDBHelper(this);

        if(Contacts.isEdit) {
            Intent intent = getIntent();
            name.setText(intent.getStringExtra("name"));
            contactNum.setText(intent.getStringExtra("phoneNumber"));
            email.setText(intent.getStringExtra("email"));
            address.setText(intent.getStringExtra("address"));
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_activity();
            }
        });
    }

    private void save_activity(){

        String Name=name.getText().toString();
        String ConTactNum=contactNum.getText().toString();
        String Email=email.getText().toString();
        String Address=address.getText().toString();
        if(Name.length()==0) Toast.makeText(getBaseContext(), "Enter a Name", Toast.LENGTH_SHORT).show();
        else if(Contacts.isEdit){
            Contacts.isEdit=false;
            ArrayList<String> resources = new ArrayList<>(Arrays.asList(Name,ConTactNum,Email,Address));
            Intent data = new Intent();
            data.putStringArrayListExtra("resources", resources);
            setResult(Activity.RESULT_OK, data);
            finish();
        }
        else if(contactDBHelper.search(Name))Toast.makeText(getBaseContext(), "Name already exists", Toast.LENGTH_SHORT).show();
        else {

            contactDBHelper.insertContact(Name,ConTactNum,Email,Address);
            finish();
        }
    }

}

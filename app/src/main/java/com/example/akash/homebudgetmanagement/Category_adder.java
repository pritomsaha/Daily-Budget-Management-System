package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Category_adder extends Activity {

    private EditText etCategoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        etCategoryName = (EditText)findViewById(R.id.etCategoryName);
        Intent intent = getIntent();
        String editedElement=intent.getStringExtra("editedElement");
        etCategoryName.setText(editedElement);
    }

    public void save(View view){

        String name = etCategoryName.getText().toString();

        if(name.length()==0){
           Toast.makeText(getBaseContext(), "Please enter a category name", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent data = new Intent();
            data.putExtra("name",name);
            setResult(Activity.RESULT_OK,data);
            finish();
        }
    }

}

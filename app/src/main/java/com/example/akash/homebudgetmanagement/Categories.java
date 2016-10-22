package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Categories extends Activity {

    final Context context=this;
    private CategoryDBHelper ctdb;
    private ArrayList<String> Categories;
    private Button btnAddCategory;
    private ListView listCategories;
    private static final int CATEGORY_NAME=0;
    public static boolean isItemClickable=false;
    private ArrayAdapter adapter;
    private ArrayList<String> categories= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        listCategories = (ListView)findViewById(R.id.listCategories);
        ctdb=new CategoryDBHelper(this);
        Categories =ctdb.getAllCategory();
        Collections.sort(Categories);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Categories);
        listCategories.setAdapter(adapter);

        btnAddCategory = (Button)findViewById(R.id.btnAddCategory);

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click_activity(position);
            }
        });

        listCategories.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                longClick_activity(position);
                return true;
            }
        });
    }

    public void addCategory(){

        Intent intent = new Intent(this,Category_adder.class);
        startActivityForResult(intent, CATEGORY_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CATEGORY_NAME){
            if(resultCode==Activity.RESULT_OK){

                String name = data.getStringExtra("name");
                if(!Categories.contains(name)) {
                    ctdb.insertCategory(name);
                    Categories.add(name);
                    Collections.sort(categories);
                    adapter.notifyDataSetChanged();
               }
                else{
                    Toast.makeText(getBaseContext(), "Already exits", Toast.LENGTH_SHORT);
                }
            }
            else{
                Log.d("MainActivity", "AddCategory Activity cancelled");}
        }

    }

    private void click_activity(final int position){

        if(isItemClickable) {
            Intent data = new Intent();
            data.putExtra("category", listCategories.getItemAtPosition(position).toString());
            setResult(Activity.RESULT_OK, data);
            isItemClickable=false;
            finish();
        }

    }

    private void longClick_activity(final int position){


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_delete_design);
        final String elementName=listCategories.getItemAtPosition(position).toString();
        dialog.setTitle(elementName);

        TextView txtEdit = (TextView)dialog.findViewById(R.id.txtEdit);
        TextView txtDelete = (TextView)dialog.findViewById(R.id.txtDelete);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Categories.remove(elementName);
                ctdb.deleteCategory(elementName);
                Intent intent = new Intent(Categories.this,Category_adder.class);
                intent.putExtra("editedElement",elementName);
                startActivityForResult(intent, CATEGORY_NAME);
                dialog.dismiss();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Categories.remove(elementName);
                ctdb.deleteCategory(elementName);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

package com.example.gogetgroceries;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gogetgroceries.database.Item;

import java.util.List;

public class MainActivity extends DatabaseActivity implements ListRecyclerAdapter.OnListClickListener, ListRecyclerAdapter.OnLongListClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Shows all lists when the activity is opened
        initRecyclerView();

        //Initialize xml
        final EditText listName = (EditText) findViewById(R.id.input_listname);
        Button addButton = (Button) findViewById(R.id.addlist_btn);

        //Code for ADD-button - add a new list
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(listName.getText())) {                                                                        //Checks if the input field is empty
                    Item newList = new Item("", listName.getText().toString());                                            //Creates a new Item object (itemName empty)
                    db.getItemDao().insert(newList);                                                                                //and inserts it in the database
                    Intent intent = new Intent(MainActivity.this, GroceryListActivity.class);
                    intent.putExtra("selected_list", listName.getText().toString());                                          //Saves the listName
                    startActivity(intent);                                                                                          //Starts GroceryListActivity
                    initRecyclerView();
                    Toast.makeText(MainActivity.this, "List added", Toast.LENGTH_SHORT).show();
                    listName.getText().clear();
                }
                else {
                    Toast.makeText(MainActivity.this, "Name field blank, no list added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Code for CLEAR-button - clears the database (deletes all lists)
        Button clearButton = (Button) findViewById(R.id.clear_db);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog deleteDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete ALL Lists")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.getItemDao().deleteAll();
                                initRecyclerView();
                                Toast.makeText(MainActivity.this, "All Lists Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                deleteDialog.show();
            }
        });
    }

    //Shows all lists
    private void initRecyclerView() {
        List<Item> listNames = db.getItemDao().getLists();
        RecyclerView recyclerView = findViewById(R.id.list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListRecyclerAdapter adapter = new ListRecyclerAdapter(listNames, this, this);
        recyclerView.setAdapter(adapter);
    }

    //When a list is clicked, the listName is saved in an intent and GroceryListActivity starts
    @Override
    public void onListClick(int position) {
        List<Item> listNames = db.getItemDao().getLists();
        Intent intent = new Intent(this, GroceryListActivity.class);
        intent.putExtra("selected_list", listNames.get(position).getListName());
        startActivity(intent);
    }

    //When a long click on a list is registered, an AlertDialog pops up
    @Override
    public void onLongListClick(int position) {

        //Variables
        final List<Item> listNames = db.getItemDao().getLists();
        final String listName = listNames.get(position).getListName();

        //AlertDialog - Edit/Delete list
        final EditText listEditText = new EditText(this);
        listEditText.setHint("Enter new name");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(listName)
                .setMessage("Edit/Delete list")
                .setView(listEditText)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(listEditText.getText())) {
                            db.getItemDao().updateListName(listName, listEditText.getText().toString());
                            initRecyclerView();
                            Toast.makeText(MainActivity.this, "List Edited", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Nothing Happened", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog deleteDialog = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Are you sure?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.getItemDao().deleteList(listName);
                                        initRecyclerView();
                                        Toast.makeText(MainActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();                                }
                                })
                                .setNegativeButton("Cancel", null)
                                .create();
                        deleteDialog.show();
                    }
                })
                .setNeutralButton("Cancel", null)
                .create();
        dialog.show();
    }
}


package com.example.gogetgroceries;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gogetgroceries.database.Item;

import java.util.List;

public class GroceryListActivity extends DatabaseActivity implements ItemRecyclerAdapter.OnItemClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_list);
        Intent intent = getIntent();

        //Initialize xml
        final TextView listName = (TextView) findViewById(R.id.list_name);
        final EditText itemName = (EditText) findViewById(R.id.input_itemname);
        Button addBtn = (Button) findViewById(R.id.additem_btn);
        Button deleteBtn = (Button) findViewById(R.id.delete_btn);

        //Get the listName from the Intent
        listName.setText(intent.getStringExtra("selected_list"));

        //Initiate the RecyclerView, showing Item objects with listName matching the intent
        initRecyclerView(listName.getText().toString());

        //Code for ADD-button - add a new item
        addBtn.setOnClickListener(new View.OnClickListener() {
            Boolean emptyFirst = true;

            @Override
            public void onClick(View v) {
                List<Item> itemNames = db.getItemDao().getList(listName.getText().toString());
                Item newItem = new Item(itemName.getText().toString(), listName.getText().toString());

                if (!TextUtils.isEmpty(itemName.getText())) {                                                                           //Checks if the input field is empty
                    if (itemNames.get(0).getItemName().isEmpty() && emptyFirst) {                                                       //Checks if the first itemName on the list is empty
                        db.getItemDao().updateFirstItem(itemName.getText().toString(), listName.getText().toString());                  //Updates the first (empty) itemName with the users itemName
                        emptyFirst = false;                                                                                             //Allows the program to only enter this once
                    } else {
                        db.getItemDao().insert(newItem);                                                                                //Inserts the item in the selected_list
                    }
                    Toast.makeText(GroceryListActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroceryListActivity.this, "Name field blank, no item added", Toast.LENGTH_SHORT).show();
                }
                initRecyclerView(listName.getText().toString());
                itemName.getText().clear();
            }
        });

        //Code for DELETE-button - delete the current list
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog deleteDialog = new AlertDialog.Builder(GroceryListActivity.this)                                        //Creates an AlertDialog to confirm/cancel the deletion
                        .setTitle("Delete List")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.getItemDao().deleteList(listName.getText().toString());
                                Intent intent = new Intent(GroceryListActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                deleteDialog.show();
            }
        });
    }

    //Shows all items on the selected list
    private void initRecyclerView(String listName) {
        List<Item> itemNames = db.getItemDao().getList(listName);
        RecyclerView recyclerView = findViewById(R.id.item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(itemNames, listName, this);
        recyclerView.setAdapter(adapter);
    }

    //When an item is clicked, an AlertDialog pops up
    @Override
    public void onItemClick(final int position) {
        //Variable declaration
        final TextView tvListName = (TextView) findViewById(R.id.list_name);
        final String listName = tvListName.getText().toString();
        final List<Item> itemNames = db.getItemDao().getList(listName);
        final String itemName = itemNames.get(position).getItemName();
        final int itemId = itemNames.get(position).getId();

        //AlertDialog - Edit/Delete item
        final EditText itemEditText = new EditText(this);
        itemEditText.setHint("Enter new name");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(itemName)
                .setMessage("Edit/Delete item")
                .setView(itemEditText)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(itemEditText.getText())) {
                            db.getItemDao().updateItem(itemEditText.getText().toString(), listName, itemId);
                            initRecyclerView(listName);
                            Toast.makeText(GroceryListActivity.this, "Item Edited", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(GroceryListActivity.this, "Nothing Happened", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    AlertDialog deleteDialog = new AlertDialog.Builder(GroceryListActivity.this)
                            .setMessage("Are you sure?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.getItemDao().deleteItem(itemName, listName, itemId);
                                    initRecyclerView(listName);
                                    Toast.makeText(GroceryListActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                }
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
package com.example.gogetgroceries.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Item")
public class Item {

//Attributes
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String listName;

    public String itemName;

//Constructor
    public Item(String itemName, String listName) {
        this.itemName = itemName;
        this.listName = listName;
    }

//Getter and Setter
public int getId() {
    return id;
}

    public void setId(int id) {
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

package com.example.gogetgroceries.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT MIN(id) AS id, listName FROM Item GROUP BY listName")                            //Shows all lists (once). Used in MainActivity.
    List<Item> getLists();

    @Query("SELECT * FROM Item WHERE listName = :name")                                             //Gets the items on a specific list
    List<Item> getList(String name);

    @Query("UPDATE Item SET listName = :newListName WHERE listName = :oldListName")                 //Updates the listName (when edited by user). Does not ignore already existing names!!
    void updateListName(String oldListName, String newListName);

    @Query("UPDATE Item SET itemName = :itemName WHERE listName = :listName")                       //Overwrites the first blank item on a newly created list
    void updateFirstItem(String itemName, String listName);

    @Query("UPDATE Item SET itemName = :itemName WHERE listName = :listName AND id = :id")          //Updates an item
    void updateItem(String itemName, String listName, int id);

    @Query("DELETE FROM Item WHERE listName = :listName")                                           //Deletes a list
    void deleteList(String listName);

    @Query("DELETE FROM Item WHERE itemName = :itemName AND listName = :listName AND id = :id")     //Deletes an item
    void deleteItem(String itemName, String listName, int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)                                                //Inserts a new Item object
    void insert(Item item);

    @Query("DELETE FROM Item")                                                                      //Deletes all lists
    void deleteAll();
}

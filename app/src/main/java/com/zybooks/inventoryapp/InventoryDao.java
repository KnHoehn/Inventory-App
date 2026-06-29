package com.zybooks.inventoryapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
Author: Kayla Hoehn

This interface handles the SQL queries to the inventory database

 */

@Dao
public interface InventoryDao {

    // Query to select all rows from the database
    @Query("SELECT * FROM inventory_table")
    LiveData<List<Inventory>> getAllInventory();

    // Query to select the row from the database where the description matches the one provided
    @Query("SELECT * FROM inventory_table WHERE description = :description")
    Inventory getInventory(String description);

    // Query that inserts a new row
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Inventory inventory);

    // Query that updates a row
    @Update
    void update(Inventory inventory);

    // Query that deletes a row
    @Delete
    void delete(Inventory inventory);

}

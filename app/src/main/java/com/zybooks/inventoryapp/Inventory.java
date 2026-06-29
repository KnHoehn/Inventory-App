package com.zybooks.inventoryapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/*
Author: Kayla Hoehn

This class handles the database schema for the inventory table

 */

@Entity(tableName = "inventory_table")
public class Inventory {


    // Creates the "description" and "count" columns
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "description")
    private String mDescription;
    @ColumnInfo(name = "count")
    private int mCount;

    // Constructor
    public Inventory( @NonNull String description, int count) {
        this.mDescription = description;
        this.mCount = count;
    }

    // Method to return the inventory's description
    @NonNull
    public String getDescription() {

        return this.mDescription;
    }

    // Method to return the inventory's count
    public int getCount() {

        return this.mCount;
    }

    // Method to set the count of an inventory object
    public void setCount(int count) {

        this.mCount = count;
    }
}

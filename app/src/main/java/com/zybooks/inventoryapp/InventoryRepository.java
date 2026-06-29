package com.zybooks.inventoryapp;


import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

/*
Author: Kayla Hoehn

This class creates the repository for the inventory database

 */

class InventoryRepository {

    // Instantiates the inventory DAO
    private InventoryDao mInventoryDao;

    // Instantiates a live list
    private LiveData<List<Inventory>> mAllInventory;

    // Constructor
    InventoryRepository(Application application) {
        InventoryDatabase db = InventoryDatabase.getDatabase(application);
        mInventoryDao = db.inventoryDao();
        mAllInventory = mInventoryDao.getAllInventory();
    }


    // Returns the inventory contents from the database
    LiveData<List<Inventory>> getAllInventory() {

        return mAllInventory;
    }

    // Calls the DAO to insert an inventory object into the database
    void insert(Inventory inventory) {
        InventoryDatabase.databaseWriteExecutor.execute(() -> {
            mInventoryDao.insert(inventory);
        });
    }

    // Call the DAO to delete an inventory object from the database
    public void deleteInventory(Inventory inventory) {
        InventoryDatabase.databaseWriteExecutor.execute(() -> {
            mInventoryDao.delete(inventory);
        });
    }

    // Calls the DAO to update an inventory object from the database
    public void updateInventory(Inventory inventory) {
        InventoryDatabase.databaseWriteExecutor.execute(() -> {
            mInventoryDao.update(inventory);
        });
    }

    // Calls the DAO to retrieve the inventory object from the database
    Inventory getInventory(String description) {

        return mInventoryDao.getInventory(description);
    }
}

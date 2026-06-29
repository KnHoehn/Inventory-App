package com.zybooks.inventoryapp;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/*
Author: Kayla Hoehn

This class creates the inventory view model for the live data

 */
public class InventoryViewModel extends AndroidViewModel {

    // Instantiates the repository
    private InventoryRepository mRepository;

    // Instantiates a live list
    private final LiveData<List<Inventory>> mAllInventory;

    // Constructor
    public InventoryViewModel(Application application) {
        super(application);
        mRepository = new InventoryRepository(application);
        mAllInventory = mRepository.getAllInventory();
    }

    // Returns all inventory in the database // Returns the inventory contents from the database
    LiveData<List<Inventory>> getAllInventory() {

        return mAllInventory;
    }


    // Call the repository to insert a new item into the database
    void insert(Inventory inventory) {
        mRepository.insert(inventory);
    }

    // Call the repository to delete an item the database
    void delete(Inventory inventory) {
        mRepository.deleteInventory(inventory);
    }

    // Call the repository to update an item in the database
    void update(Inventory inventory) {
        mRepository.updateInventory(inventory);
    }

    // Call the repository to get the row from the database that matches the description provided
    Inventory getInventory(String description) {
        return mRepository.getInventory(description);
    }
}

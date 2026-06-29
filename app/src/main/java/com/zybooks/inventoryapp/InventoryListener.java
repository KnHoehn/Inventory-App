package com.zybooks.inventoryapp;

/*
Author: Kayla Hoehn

This interface creates a listener that will trigger certain methods in main activity if called

 */
public interface InventoryListener {

    // Calls the DeleteRow function
    void DeleteRow(Inventory inventory);

    // Calls the UpdateCount function
    void UpdateCount(Inventory inventory);

}

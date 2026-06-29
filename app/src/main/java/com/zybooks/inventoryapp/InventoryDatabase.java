package com.zybooks.inventoryapp;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Author: Kayla Hoehn

This class creates the inventory room database for the application

 */

@Database(entities = {Inventory.class}, version = 1, exportSchema = false)
abstract class InventoryDatabase extends RoomDatabase {

    // Instantiates the inventory DAO
    abstract InventoryDao inventoryDao();

    // Instantiates a database instance
    private static volatile InventoryDatabase INSTANCE;

    // Holds the fixed variable for number of threads
    private static final int NUMBER_OF_THREADS = 4;

    // Instantiates the database writable service passing in the number of threads
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Method to be able to get an instance of the database if there is one, otherwise creates the database
    static InventoryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (InventoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    InventoryDatabase.class, "inventory_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Creates the database
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                InventoryDao dao = INSTANCE.inventoryDao();


            });
        }
    };
}

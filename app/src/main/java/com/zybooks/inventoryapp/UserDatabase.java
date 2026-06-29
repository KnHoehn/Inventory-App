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

This class creates the user room database for the application

 */

@Database(entities = {User.class}, version = 1, exportSchema = false)
abstract class UserDatabase extends RoomDatabase {

    // Instantiates the user DAO
    abstract UserDao userDao();

    // Instantiates a database instance
    private static volatile UserDatabase INSTANCE;

    // Holds the fixed variable for number of threads
    private static final int NUMBER_OF_THREADS = 4;

    // Instantiates the database writable service passing in the number of threads
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Method to be able to get an instance of the database if there is one, otherwise creates the database
    static UserDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDatabase.class, "user_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Creates the database
    private static UserDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                UserDao dao = INSTANCE.userDao();

            });
        }
    };
}

package com.zybooks.inventoryapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/*
Author: Kayla Hoehn

This interface handles the SQL queries to the user database

 */

@Dao
public interface UserDao {


    // Query to select the row from the database where the username and password matches the one provided
    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    User getUser(String username, String password);

    // Query that inserts a new row
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

}

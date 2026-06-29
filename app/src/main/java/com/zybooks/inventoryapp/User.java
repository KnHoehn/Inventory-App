package com.zybooks.inventoryapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/*
Author: Kayla Hoehn

This class handles the database schema for the user table

 */
@Entity(tableName = "user_table")
public class User {

    // Creates the "username" and "password" columns
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "username")
    private String mUsername;
    @ColumnInfo(name = "password")
    private String mPassword;

    // Constructor
    public User(@NonNull String username, String password) {
        this.mUsername = username;
        this.mPassword = password;
    }

    // Method to return the user's username
    @NonNull
    public String getUsername() {

        return this.mUsername;
    }

    // Method to return the user's password
    @NonNull
    public String getPassword() {

        return this.mPassword;
    }

}

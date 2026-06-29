
package com.zybooks.inventoryapp;


import android.app.Application;

/*
Author: Kayla Hoehn

This class creates the repository for the user database

 */

class UserRepository {

    // Instantiates the user DAO
    private UserDao mUserDao;

    // Constructor
    UserRepository(Application application) {
        UserDatabase db = UserDatabase.getDatabase(application);
        mUserDao = db.userDao();
    }


    // Calls the DAO to insert a user into the database
    void insert(User user) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }

    // Calls the DAO to retrieve the user from the database
    User getUser(String username, String password) {

        return mUserDao.getUser(username, password);
    }
}


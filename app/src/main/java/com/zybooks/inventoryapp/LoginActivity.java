package com.zybooks.inventoryapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/*
Author: Kayla Hoehn

This class handles the logic for the log in page

 */

public class LoginActivity extends AppCompatActivity {

    // instantiates variables
    private EditText username;

    private EditText password;

    private UserRepository userRepository;

    private Button loginButton;

    private Button createAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Binds the variables to the corresponding xml elements
        username = findViewById(R.id.userNameText);
        password = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.buttonSignIn);
        createAccountButton = findViewById(R.id.buttonCreateAccount);

        // Instantiates the user database
        UserDatabase db = UserDatabase.getDatabase(getApplicationContext());
        // Instantiates the user repository
        userRepository = new UserRepository(getApplication());

        // Listens for changes to the username text box
        username.addTextChangedListener(new TextWatcher() {

            // Unused
            @Override
            public void afterTextChanged(Editable s) {

            }

            // Unused
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // If username and password length is greater than 0, enables the login and create account buttons
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginButton.setEnabled(username.length() > 0  && password.length() > 0);
                createAccountButton.setEnabled(username.length() > 0  && password.length() > 0);
            }
        });

        // Listens for changes to the password text box
        password.addTextChangedListener(new TextWatcher() {

            // Unused
            @Override
            public void afterTextChanged(Editable s) {

            }

            // Unused
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // If username and password length is greater than 0, enables the login and create account buttons
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginButton.setEnabled(username.length() > 0 && password.length() > 0);
                createAccountButton.setEnabled(username.length() > 0 && password.length() > 0);
            }
        });

    }

    // Handles the logic if user clicks the sign in button
    public void SignIn(View view) {

        // Assigns the text entered into the username box into a variable
        String enteredUsername = username.getText().toString().trim();

        // Assigns the text entered into the password box into a variable
        String enteredPassword = password.getText().toString().trim();


        // If the username and password variables are not empty
        if (!enteredUsername.isEmpty() && !enteredPassword.isEmpty()) {

            // Run a separate thread for actions to the database
            UserDatabase.databaseWriteExecutor.execute(() -> {

                // Get's the user from the database and assigns it to the user variable
                User user = userRepository.getUser(enteredUsername, enteredPassword);

                // Run on main thread
                runOnUiThread(() -> {

                    // If the user is found in the database
                    if (user != null) {

                        // Run on main thread
                        runOnUiThread(() -> {

                            // If user has not given the application SMS permissions, launch the SMS permissions page
                            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                Intent intent = new Intent(this, SMSPermissionActivity.class);
                                startActivity(intent);
                                finish();
                                // Else launch straight into the main page
                            } else {
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        // If user is null, display a message to the user
                    } else {
                        Toast.makeText(this, "Username and password not found", Toast.LENGTH_SHORT).show();

                    }
                });
            });

        }
    }

    // Handles the logic if user clicks the create account button
    public void CreateAccount(View view) {

        // Assigns the text entered into the username box into a variable
        String enteredUsername = username.getText().toString().trim();

        // Assigns the text entered into the password box into a variable
        String enteredPassword = password.getText().toString().trim();

        // If the username and password variables are not empty
        if (!enteredUsername.isEmpty() && !enteredPassword.isEmpty()) {

            // Runs a separate thread to perform database actions on
            UserDatabase.databaseWriteExecutor.execute(() -> {

                // Gets the user from the database and assigns it to the user variable
                User user = userRepository.getUser(enteredUsername, enteredPassword);

                // If user is not found in the database, creates a new user
                if (user == null) {
                    User newUser = new User(enteredUsername, enteredPassword);
                    userRepository.insert(newUser);

                    // Run on main thread
                    runOnUiThread(() -> {
                        // launch the SMS permissions page
                            Intent intent = new Intent(this, SMSPermissionActivity.class);
                            startActivity(intent);
                            finish();
                    });
                    // Else if user is found in the database, display a message to the user
                } else {
                    // run on main thread
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Username and password already exists", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }
}
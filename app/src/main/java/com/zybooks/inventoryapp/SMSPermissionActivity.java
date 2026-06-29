package com.zybooks.inventoryapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*
Author: Kayla Hoehn

This class handles the logic for the SMS permission page

 */

public class SMSPermissionActivity extends AppCompatActivity {

    // Instantiate variables
    private EditText phoneNumber;
    private Button allowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        // Binds the variables to the corresponding xml elements
        phoneNumber = findViewById(R.id.PhoneText);
        allowButton = findViewById(R.id.AllowButton);

        // Listens for changes to the phone number edit text box
        phoneNumber.addTextChangedListener(new TextWatcher() {
            // Unused
            @Override
            public void afterTextChanged(Editable s) {

            }

            // Unused
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            // If text has changed and the string length is not zero, enables the button
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                allowButton.setEnabled(true);
            }
        });
    }

    // Handles the logic if the user clicks the allow button
    public void AllowClicked(View view) {
        // Asks the user to allow SMS permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        // If permission granted, stores the user's phone number into shared preferences
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            String userPhoneNumber = String.valueOf(phoneNumber.getText());
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("phone_number", userPhoneNumber);
            editor.apply();
            // Launches main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Handles the logic if the user clicks the deny button
    public void DenyClicked(View view) {
        // Launches the main activity page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

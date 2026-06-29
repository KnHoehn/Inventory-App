package com.zybooks.inventoryapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Author: Kayla Hoehn

This class handles the logic for the main page

 */

public class MainActivity extends AppCompatActivity implements InventoryListener{

    // Instantiate variables
    private InventoryViewModel mInventoryViewModel;

    private EditText newItemDescription;

    private EditText newItemCount;

    private Button addNewItemButton;

    private String userPhoneNumber;

    // Creates an executor service to run sending sms messages on a separate thread
    private static final ExecutorService SmsExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Displays the recycler view items
        RecyclerView recyclerView = findViewById(R.id.inventoryRecyclerView);
        final InventoryListAdapter adapter = new InventoryListAdapter(new InventoryListAdapter.InventoryDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mInventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);

        mInventoryViewModel.getAllInventory().observe(this, inventory -> {

            adapter.submitList(inventory);
        });

        // Binds the variables to the corresponding xml elements
        newItemDescription = findViewById(R.id.newItemText);
        newItemCount = findViewById(R.id.newItemCount);
        addNewItemButton = findViewById(R.id.addItemButton);
        userPhoneNumber = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("phone_number", null);

        // Listens for changes to the new item description edit text box
        newItemDescription.addTextChangedListener(new TextWatcher() {
            // Unused
            @Override
            public void afterTextChanged(Editable s) {

            }

            // Unused
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            // If new item description and new item count's length is greater than 0, enables the add item button
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addNewItemButton.setEnabled(newItemDescription.length() > 0 && newItemCount.length() > 0);
            }
        });

        // listens for changes to the new item count edit text box
        newItemCount.addTextChangedListener(new TextWatcher() {
            // Unused
            @Override
            public void afterTextChanged(Editable s) {

            }

            // Unused
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            // If new item description and new item count's length is greater than 0, enables the add item button
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addNewItemButton.setEnabled(newItemCount.length() > 0 && newItemCount.getText() != null && newItemDescription.length() > 0 && newItemDescription.getText() != null);
            }
        });


    }

    // Handles the logic if the user clicks the sign out button
    public void SignOut(View view) {

        // Returns to the log in page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Handles the logic if the user clicks the add item button
    public void AddInventory(View view) {
        // Assigns the text entered into the new item description into a variable
        String enteredDescription = newItemDescription.getText().toString().trim();


        // If the description is not empty, capitalizes the first letter and makes all following letters lowercase
        if (!enteredDescription.isEmpty()) {

            char[] charArray = enteredDescription.toCharArray();

            for (int i = 0; i < charArray.length; i++) {
                charArray[0] = Character.toUpperCase(charArray[0]);
                if(charArray[i] == ' ') {
                    charArray[i+1] = Character.toUpperCase(charArray[i+1]);
                }
                else {
                    charArray[i] = Character.toLowerCase(charArray[i]);
                }
            }

            // Stores the parsed description string
            String parsedDescription = String.valueOf(charArray);

            // Assigns the text entered into the new item count into a variable and converts it into an integer
            int enteredCount = Integer.parseInt(newItemCount.getText().toString().trim());

            // Starts a separate thread to perform database actions
            UserDatabase.databaseWriteExecutor.execute(() -> {

                // Checks if the new inventory item is already in the database
                Inventory inventory = mInventoryViewModel.getInventory(parsedDescription);

                // Run on main thread
                runOnUiThread(() -> {

                    // If not found in the database, creates a new inventory object and inserts into the database
                    if (inventory == null) {

                        Inventory newInventory = new Inventory(parsedDescription, enteredCount);

                        mInventoryViewModel.insert(newInventory);
                    }
                    // else, display a message to the user
                    else {
                        Toast.makeText(this, "Item is already in the database", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        }


    }


    // Calls the inventory view model to delete a row from the database
    public void DeleteRow(Inventory inventory) {
        mInventoryViewModel.delete(inventory);

    }

    // Calls the inventory view model to update a row in the database
    public void UpdateCount(Inventory inventory) {
        mInventoryViewModel.update(inventory);

        // If user has allowed SMS permissions and the count has been reduced to 0, send them an SMS on a separate thread
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            if (inventory.getCount() == 0) {
                String smsMessage = inventory.getDescription() + " has been reduced to 0";

                SmsExecutor.execute(() -> {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(userPhoneNumber, null, smsMessage, null, null);
                });

            }
        }


    }



}

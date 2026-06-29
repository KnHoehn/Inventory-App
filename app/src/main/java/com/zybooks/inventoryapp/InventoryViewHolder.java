package com.zybooks.inventoryapp;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/*
Author: Kayla Hoehn

This class creates the view holder for the recycler view contents and performs view holder-specific actions

 */
class InventoryViewHolder extends RecyclerView.ViewHolder {


    // Instantiate variables
    private final TextView inventoryItemView;

    private final EditText inventoryItemEditText;

    private Button deleteButton;

    private Button incButton;

    private Button decButton;

    private Inventory currentRow;

    private InventoryListener inventoryListener;

    // Constructor
    private InventoryViewHolder(View itemView, InventoryListener inventoryListener) {
        super(itemView);

        // Binds the variables to the corresponding xml elements
        inventoryItemView = itemView.findViewById(R.id.inventoryDescription);
        inventoryItemEditText = itemView.findViewById(R.id.inventoryCountText);
        deleteButton = itemView.findViewById(R.id.deleteButton);
        incButton = itemView.findViewById(R.id.incrementButton);
        decButton = itemView.findViewById(R.id.decrementButton);

        // Assigns the inventory listener to the passed in one
        this.inventoryListener = inventoryListener;

        //
        inventoryItemEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {


                // If user clicks "done" or "next" on the keyboard
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    int newCount = Integer.parseInt(inventoryItemEditText.getText().toString().trim());

                    // Sets the new count to the current row's item
                   currentRow.setCount(newCount);

                   // Calls the inventory listener to update the current row's item in the database
                    inventoryListener.UpdateCount(currentRow);

                }

                return false;
            }
        });


        // Click listener that detects if the user has clicked the delete button
        deleteButton.setOnClickListener((v) -> {

            // Calls the inventory listener to trigger the deletion of the current row
            inventoryListener.DeleteRow(currentRow);
        });

        // Click listener that detects if the increment button has been clicked
        incButton.setOnClickListener((v) -> {

            // Increments the item's counts by one
            int newCount = currentRow.getCount() + 1;

            // Sets the new count to the current row's item
            currentRow.setCount(newCount);


            // Calls the inventory listener to update the current row's item in the database
            inventoryListener.UpdateCount(currentRow);
        });

        // Click listener that detects if the user has clicked the decrement button
        decButton.setOnClickListener((v) -> {

            // Decrements the item's count by one
            int newCount = currentRow.getCount() - 1;

            // Prevents the item's count from being negative
            if (newCount < 0) {
                newCount = 0;
            }

            // Sets the new count to the current row's item
            currentRow.setCount(newCount);

            // Calls the inventory listener to update the current row's item in the database
            inventoryListener.UpdateCount(currentRow);



        });
    }

    // Binds inventory to the current row
    public void bind(Inventory inventory) {
        // Binds inventory object to the current row
        currentRow = inventory;
        // Set's the current row's description as the inventory object's description
        inventoryItemView.setText(inventory.getDescription());

        // Avoids updating the edit text box's contents if the user is actively editing the text
        if(!inventoryItemEditText.hasFocus()) {
            // Set's the current row's edit text box as the inventory object's count
            inventoryItemEditText.setText(String.valueOf(inventory.getCount()));
        }

        // Only enables the row's decrement button if the item's count is greater than 0
        decButton.setEnabled(inventory.getCount() > 0);


    }


    // Creates the view holder
    static InventoryViewHolder create(ViewGroup parent, InventoryListener inventoryListener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new InventoryViewHolder(view, inventoryListener);

    }


}

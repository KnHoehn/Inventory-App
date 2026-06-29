package com.zybooks.inventoryapp;



import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

/*
Author: Kayla Hoehn

This class creates the adapter for the recycler view

 */


public class InventoryListAdapter extends ListAdapter<Inventory, InventoryViewHolder> {

    // Instantiates the inventory listener interface
    private final InventoryListener listener;

    // Creates the recycler view adapter that will detect changes in the recycler view
    public InventoryListAdapter(@NonNull DiffUtil.ItemCallback<Inventory> diffCallback, InventoryListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    // Creates a new view holder
    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return InventoryViewHolder.create(parent, listener);
    }

    // Binds the inventory item to the view holder
    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        Inventory current = getItem(position);
        holder.bind(current);
    }

    // Dynamically updates the recycler view if changes are detected
    static class InventoryDiff extends DiffUtil.ItemCallback<Inventory> {

        // If contents are the same, no changes to the view are needed
        @Override
        public boolean areItemsTheSame(@NonNull Inventory oldItem, @NonNull Inventory newItem) {
            return oldItem == newItem;
        }

        // If contents are not the same, update description and count
        @Override
        public boolean areContentsTheSame(@NonNull Inventory oldItem, @NonNull Inventory newItem) {
            return oldItem.getDescription().equals(newItem.getDescription()) && oldItem.getCount() == (newItem.getCount());
        }
    }
}

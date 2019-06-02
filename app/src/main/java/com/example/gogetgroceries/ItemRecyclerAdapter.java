package com.example.gogetgroceries;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gogetgroceries.database.Item;

import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {

    public List<Item> itemNames;
    public String listName;
    OnItemClickListener onItemClickListener;

    //Constructor
    public ItemRecyclerAdapter(List<Item> itemNames, String listName, OnItemClickListener onItemClickListener) {
        this.itemNames = itemNames;
        this.listName = listName;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_items, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, onItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.itemName.setText(itemNames.get(position).getItemName());
    }

    @Override
    public int getItemCount() {
        return itemNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemName;
        ConstraintLayout layout;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            itemName = itemView.findViewById(R.id.item_name);
            layout = itemView.findViewById(R.id.rview_items);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    //Interface to be used in GroceryListActivity - detects when an item is clicked
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

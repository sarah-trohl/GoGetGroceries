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

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

    public List<Item> listNames;
    OnListClickListener onListClickListener;
    OnLongListClickListener onLongListClickListener;

    //Constructor
    public ListRecyclerAdapter(List<Item> listNames, OnListClickListener onListClickListener, OnLongListClickListener onLongListClickListener) {
        this.listNames = listNames;
        this.onListClickListener = onListClickListener;
        this.onLongListClickListener = onLongListClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_lists, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, onListClickListener, onLongListClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.listName.setText(listNames.get(position).getListName());
    }

    @Override
    public int getItemCount() {
        return listNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView listName;
        ConstraintLayout layout;
        OnListClickListener onListClickListener;
        OnLongListClickListener onLongListClickListener;

        public ViewHolder(@NonNull View itemView, OnListClickListener onListClickListener, OnLongListClickListener onLongListClickListener) {
            super(itemView);
            this.onListClickListener = onListClickListener;
            this.onLongListClickListener = onLongListClickListener;
            listName = itemView.findViewById(R.id.list_name);
            layout = itemView.findViewById(R.id.rview_lists);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListClickListener.onListClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onLongListClickListener.onLongListClick(getAdapterPosition());
            return false;
        }
    }

    //Interfaces to be used in MainActivity - detects short and long clicks on lists
    public interface OnListClickListener {
        void onListClick(int position);
    }

    public interface OnLongListClickListener {
        void onLongListClick(int position);
    }
}

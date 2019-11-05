package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.voleum.ordermolder.objects.Order;

public class ObjectsChooserRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Order> objects;
    private OnEntryClickListener onEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View v, int position);
    }

    public ObjectsChooserRecyclerViewAdapter(ArrayList<Order> objects) {
        this.objects = objects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ObjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Order object = objects.get(position);
        ((ObjectViewHolder) holder).textView.setText(objects.toString());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public class ObjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ObjectViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textView = (TextView) view;
        }

        @Override
        public void onClick(View v) {
            if (onEntryClickListener != null) {
                onEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }
}

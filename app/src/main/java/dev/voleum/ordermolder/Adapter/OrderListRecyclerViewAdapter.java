package dev.voleum.ordermolder.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.voleum.ordermolder.Object.Order;

public class OrderListRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Order> orders;
    private OnEntryCLickListener onEntryCLickListener;

    public interface OnEntryCLickListener {
        void onEntryClick(View v, int position);
    }

    public OrderListRecyclerViewAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order order = orders.get(position);
        ((OrderViewHolder) holder).textView.setText(order.toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOnEntryCLickListener(OnEntryCLickListener onEntryCLickListener) {
        this.onEntryCLickListener = onEntryCLickListener;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public OrderViewHolder(@NonNull View view) {
            super(view);
            view.setOnClickListener(this);
            textView = (TextView) view;
        }

        @Override
        public void onClick(View v) {
            if (onEntryCLickListener != null) {
                onEntryCLickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

}

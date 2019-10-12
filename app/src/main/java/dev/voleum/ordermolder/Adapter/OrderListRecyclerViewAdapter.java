package dev.voleum.ordermolder.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.voleum.ordermolder.Object.Order;

public class OrderListRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Order> orders;

    public OrderListRecyclerViewAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrdersHolder extends RecyclerView.ViewHolder {

        public OrdersHolder(@NonNull View view) {
            super(view);
        }
    }

}

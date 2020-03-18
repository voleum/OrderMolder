package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.ChooserObjectHolderBinding;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.viewmodels.ObjectsChooserItemViewModel;

public class ObjectsChooserRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Order> orders;
    private OnEntryClickListener onEntryClickListener;

    public ObjectsChooserRecyclerViewAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChooserObjectHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.chooser_object_holder,
                parent,
                false);
        return new ObjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order row = orders.get(position);
        ((ObjectViewHolder) holder).binding.setRow(new ObjectsChooserItemViewModel(row));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public void setData(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public class ObjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ChooserObjectHolderBinding binding;
        ObjectViewHolder(ChooserObjectHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onEntryClickListener != null) {
                onEntryClickListener.onEntryClick(v, orders.get(getLayoutPosition()));
            }
        }
    }

    public interface OnEntryClickListener {
        void onEntryClick(View v, Order row);
    }
}

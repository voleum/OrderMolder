package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.ChooserObjectHolderBinding;
import dev.voleum.ordermolder.viewmodels.ObjectsChooserItemViewModel;

public class ObjectsChooserRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<HashMap<String, Object>> objects;
    private OnEntryClickListener onEntryClickListener;

    public ObjectsChooserRecyclerViewAdapter(List<HashMap<String, Object>> objects) {
        this.objects = objects;
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
        HashMap<String, Object> row = objects.get(position);
        ((ObjectViewHolder) holder).binding.setRow(new ObjectsChooserItemViewModel(row));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public void setData(List<HashMap<String, Object>> objects) {
        this.objects = objects;
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
                onEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    public interface OnEntryClickListener {
        void onEntryClick(View v, int position);
    }
}

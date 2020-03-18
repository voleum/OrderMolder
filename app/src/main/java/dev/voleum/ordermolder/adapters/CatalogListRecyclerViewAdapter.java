package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.CatalogHolderBinding;
import dev.voleum.ordermolder.models.Catalog;
import dev.voleum.ordermolder.viewmodels.ObjListItemViewModel;

public class CatalogListRecyclerViewAdapter<T extends Catalog> extends AbstractRecyclerViewAdapter<T> {

    private OnEntryClickListener onEntryClickListener;

    public CatalogListRecyclerViewAdapter(List<T> list) {
        super(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CatalogHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.catalog_holder,
                parent,
                false);
        return new CatalogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CatalogViewHolder) holder).binding.setRow(new ObjListItemViewModel<>((list.get(position))));
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public class CatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CatalogHolderBinding binding;
        CatalogViewHolder(CatalogHolderBinding binding) {
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

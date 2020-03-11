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
import dev.voleum.ordermolder.viewmodels.CatalogListItemViewModel;

public class CatalogListRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Catalog> catalogs;
    private OnEntryClickListener onEntryClickListener;

    public CatalogListRecyclerViewAdapter(List<Catalog> catalogs) {
        this.catalogs = catalogs;
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
        Catalog row = catalogs.get(position);
        ((CatalogViewHolder) holder).binding.setRow(new CatalogListItemViewModel(row));
    }

    @Override
    public int getItemCount() {
        return catalogs.size();
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public void setData(List<Catalog> catalogs) {
        this.catalogs = catalogs;
        notifyDataSetChanged();
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

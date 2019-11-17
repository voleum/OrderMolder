package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.voleum.ordermolder.objects.Catalog;

public class CatalogListRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Catalog> catalogs;
    private OnEntryClickListener onEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View v, int position);
    }

    public CatalogListRecyclerViewAdapter(ArrayList<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new GoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Catalog catalog = catalogs.get(position);
        ((GoodViewHolder) holder).textView.setText(catalog.toString());
    }

    @Override
    public int getItemCount() {
        return catalogs.size();
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        GoodViewHolder(View view) {
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

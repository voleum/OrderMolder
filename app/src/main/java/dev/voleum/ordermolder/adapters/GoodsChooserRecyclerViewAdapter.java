package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.ChooserGoodHolderBinding;
import dev.voleum.ordermolder.models.Price;
import dev.voleum.ordermolder.viewmodels.GoodsChooserItemViewModel;

public class GoodsChooserRecyclerViewAdapter extends AbstractRecyclerViewAdapter<Price> {

    private OnEntryClickListener onEntryClickListener;

    public GoodsChooserRecyclerViewAdapter(List<Price> list) {
        super(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChooserGoodHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.chooser_good_holder,
                parent,
                false);
        return new GoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GoodViewHolder) holder).binding.setRow(new GoodsChooserItemViewModel(list.get(position)));
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ChooserGoodHolderBinding binding;
        GoodViewHolder(ChooserGoodHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onEntryClickListener != null) {
                onEntryClickListener.onEntryClick(v, list.get(getLayoutPosition()));
            }
        }
    }

    public interface OnEntryClickListener {
        void onEntryClick(View v, Price row);
    }
}

package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.OrderGoodHolderBinding;
import dev.voleum.ordermolder.models.TableGoods;
import dev.voleum.ordermolder.viewmodels.GoodsOrderListItemViewModel;
import dev.voleum.ordermolder.viewmodels.OrderViewModel;

public class GoodsOrderRecyclerViewAdapter extends AbstractRecyclerViewAdapter<TableGoods> {

    private OnEntryLongClickListener onEntryLongClickListener;
    private OrderViewModel orderViewModel;

    public GoodsOrderRecyclerViewAdapter(List<TableGoods> list, OrderViewModel orderViewModel) {
        super(list);
        this.orderViewModel = orderViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderGoodHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.order_good_holder,
                parent,
                false);
        return new GoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GoodViewHolder) holder).binding.setRow(new GoodsOrderListItemViewModel(list.get(position), orderViewModel));
    }

    public void setOnEntryLongClickListener(OnEntryLongClickListener onEntryLongClickListener) {
        this.onEntryLongClickListener = onEntryLongClickListener;
    }

    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        OrderGoodHolderBinding binding;

        GoodViewHolder(OrderGoodHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (onEntryLongClickListener != null) {
                onEntryLongClickListener.onEntryLongClick(v, getLayoutPosition());
                return true;
            }
            return false;
        }
    }

    public interface OnEntryLongClickListener {
        void onEntryLongClick(View v, int position);
    }
}

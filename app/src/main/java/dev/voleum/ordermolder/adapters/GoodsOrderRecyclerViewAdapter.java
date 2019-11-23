package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.OrderGoodHolderBinding;
import dev.voleum.ordermolder.objects.TableGoods;
import dev.voleum.ordermolder.viewmodels.GoodsOrderListItemViewModel;
import dev.voleum.ordermolder.viewmodels.OrderViewModel;

public class GoodsOrderRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<TableGoods> goods;
    private OrderViewModel orderViewModel;

    public GoodsOrderRecyclerViewAdapter(List<TableGoods> goods, OrderViewModel orderViewModel) {
        this.goods = goods;
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
        TableGoods row = goods.get(position);
        ((GoodViewHolder) holder).binding.setRow(new GoodsOrderListItemViewModel(row, orderViewModel));
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public void setData(List<TableGoods> tableGoods) {
        this.goods = tableGoods;
        notifyDataSetChanged();
    }

    public class GoodViewHolder extends RecyclerView.ViewHolder {
        OrderGoodHolderBinding binding;

        GoodViewHolder(OrderGoodHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

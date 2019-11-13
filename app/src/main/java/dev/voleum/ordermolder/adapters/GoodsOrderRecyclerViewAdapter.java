package dev.voleum.ordermolder.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
//    private GoodsOrderRecyclerViewAdapter.OnEntryClickListener onEntryClickListener;

//    public interface OnEntryClickListener {
//        void onEntryClick(View v, int position);
//    }

    public GoodsOrderRecyclerViewAdapter(List<TableGoods> goods, OrderViewModel orderViewModel) {
        this.goods = goods;
        this.orderViewModel = orderViewModel;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderGoodHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.order_good_holder,
                parent,
                false);
        return new GoodViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TableGoods row = goods.get(position);
        ((GoodViewHolder) holder).binding.setRow(new GoodsOrderListItemViewModel(row, orderViewModel));
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

//    public void setOnEntryClickListener(GoodsOrderRecyclerViewAdapter.OnEntryClickListener onEntryClickListener) {
//        this.onEntryClickListener = onEntryClickListener;
//    }

    public void setData(List<TableGoods> tableGoods) {
        this.goods = tableGoods;
        notifyDataSetChanged();
    }

//    public List<TableGoods> getGoods() {
//        return goods;
//    }

//    public double getSum() {
//        double sum = 0.0;
//        for (int i = 0; i < goods.size(); i++) {
//            TableGoods row = goods.get(i);
//            try {
//                sum += row.getSum();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return sum;
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class GoodViewHolder extends RecyclerView.ViewHolder {
        OrderGoodHolderBinding binding;

        GoodViewHolder(OrderGoodHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

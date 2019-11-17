package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.objects.Good;

public class GoodsChooserRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<HashMap<String, Object>> goods;
    private OnEntryClickListener onEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View v, int position);
    }

    public GoodsChooserRecyclerViewAdapter(ArrayList<HashMap<String, Object>> goods) {
        this.goods = goods;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chooser_good_holder, parent, false);
        return new GoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HashMap<String, Object> binded = goods.get(position);
        try {
            Good good = (Good) binded.get("good");
            double price;
            price = (Double) binded.get("price");
            ((GoodViewHolder) holder).tvName.setText(good.toString());
            ((GoodViewHolder) holder).tvPrice.setText(String.valueOf(price));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvPrice;
        GoodViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tvName = view.findViewById(R.id.tv_chooser_name);
            tvPrice = view.findViewById(R.id.tv_chooser_price);
        }

        @Override
        public void onClick(View v) {
            if (onEntryClickListener != null) {
                onEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }
}

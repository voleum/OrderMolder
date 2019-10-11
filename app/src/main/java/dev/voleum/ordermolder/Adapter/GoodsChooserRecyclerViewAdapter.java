package dev.voleum.ordermolder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import dev.voleum.ordermolder.Object.Good;

public class GoodsChooserRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Good> goods;
    private OnEntryClickListener mOnEntryClickListener;

    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public GoodViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textView = (TextView) view;
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    public interface OnEntryClickListener {
        void onEntryClick(View v, int position);
    }

    public GoodsChooserRecyclerViewAdapter(ArrayList<Good> goods) {
        this.goods = goods;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new GoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Good good = goods.get(position);
        ((GoodViewHolder) holder).textView.setText(good.toString());
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}

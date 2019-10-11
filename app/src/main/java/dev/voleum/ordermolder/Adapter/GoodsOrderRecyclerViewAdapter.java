package dev.voleum.ordermolder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;
import java.util.HashMap;

import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.R;

public class GoodsOrderRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Good> goodsList;
    private HashMap<Good, HashMap<String, Double>> goodsValues;

    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView goodName;
        public EditText goodQuantity;
        public Button btnPlus;
        public Button btnMinus;
        public GoodViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            goodName = view.findViewById(R.id.good_name);
            goodQuantity = view.findViewById(R.id.good_quantity);
            btnPlus = view.findViewById(R.id.good_plus);
            btnMinus = view.findViewById(R.id.good_minus);
            btnPlus.setOnClickListener(this);
            btnMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int currentQuantity;
            try {
                currentQuantity = Integer.parseInt(goodQuantity.getText().toString());
            } catch (NumberFormatException e) {
                currentQuantity = 0;
            }
            String newQuantity;
            switch (v.getId()) {
                case R.id.good_plus:
                    newQuantity = String.valueOf(++currentQuantity);
                    goodQuantity.setText(newQuantity);
                    break;
                case R.id.good_minus:
                    if (currentQuantity > 1) newQuantity = String.valueOf(--currentQuantity);
                    else newQuantity = String.valueOf(1);
                    goodQuantity.setText(newQuantity);
                    break;
            }
        }
    }

    public GoodsOrderRecyclerViewAdapter(ArrayList<Good> goodsList, HashMap<Good, HashMap<String, Double>> goodsValues) {
        this.goodsList = goodsList;
        this.goodsValues = goodsValues;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_holder, parent, false);
        return new GoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Good good = goods.get(position);
        Good good = goodsList.get(position);
        ((GoodViewHolder) holder).goodName.setText(good.toString());
        ((GoodViewHolder) holder).goodQuantity.setText("1");
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<Good> getGoodsList() {
        return goodsList;
    }

    public HashMap<Good, HashMap<String, Double>> getGoodsValues() {
        return goodsValues;
    }
}

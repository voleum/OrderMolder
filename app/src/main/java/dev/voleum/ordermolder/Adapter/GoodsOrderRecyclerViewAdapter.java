package dev.voleum.ordermolder.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.R;

public class GoodsOrderRecyclerViewAdapter extends RecyclerView.Adapter {

    private HashMap<Integer, HashMap<String, Object>> goods;

    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnFocusChangeListener {
        public TextView goodName;
        public EditText goodQuantity;
        public EditText goodPrice;
        public Button btnPlus;
        public Button btnMinus;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                int position = getAdapterPosition();
                HashMap<String, Object> goodInfo = goods.get(position);
                double quantity = 0;
                double price = 0;
                View root = v.getRootView();
                switch (v.getId()) {
                    case R.id.good_quantity:
                        try {
                            quantity = Double.parseDouble(((EditText) v).getText().toString());
                        } catch (NumberFormatException e) {
                            quantity = 0;
                        }
                        goodInfo.put("quantity", quantity);
                        EditText etPrice = root.findViewById(R.id.good_price);
                        try {
                            price = Double.parseDouble((etPrice.getText().toString()));
                        } catch (NumberFormatException e) {
                            price = 0;
                        }
                        break;
                    case R.id.good_price:
                        try {
                            price = Double.parseDouble(((EditText) v).getText().toString());
                        } catch (NumberFormatException e) {
                            price = 0;
                        }
                        goodInfo.put("price", price);
                        EditText etQuantity = root.findViewById(R.id.good_price);
                        try {
                            quantity = Double.parseDouble((etQuantity.getText().toString()));
                        } catch (NumberFormatException e) {
                            quantity = 0;
                        }
                        break;
                }
                double sum = quantity * price;
                goodInfo.put("sum", sum);
            }
        }

        public GoodViewHolder(View view) {
            super(view);
            goodName = view.findViewById(R.id.good_name);
            goodQuantity = view.findViewById(R.id.good_quantity);
            goodPrice = view.findViewById(R.id.good_price);
            btnPlus = view.findViewById(R.id.good_plus);
            btnMinus = view.findViewById(R.id.good_minus);
            btnPlus.setOnClickListener(v -> onButtonClick(v));
            btnMinus.setOnClickListener(v -> onButtonClick(v));
            goodQuantity.setOnFocusChangeListener(this);
            goodPrice.setOnFocusChangeListener(this);
        }

        private void onButtonClick(View v) {
            // TODO: put quantity into goods HashMap
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

    public GoodsOrderRecyclerViewAdapter(HashMap<Integer, HashMap<String, Object>> goods) {
        this.goods = goods;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_holder, parent, false);
        return new GoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Good good = (Good) goods.get(position).get("good");
        ((GoodViewHolder) holder).goodName.setText(good.toString());
        ((GoodViewHolder) holder).goodQuantity.setText("1");
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public HashMap<Integer, HashMap<String, Object>> getGoods() {
        return goods;
    }
}

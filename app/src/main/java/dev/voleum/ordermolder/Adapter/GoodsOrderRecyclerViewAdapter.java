package dev.voleum.ordermolder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
        HashMap<String, Object> goodData = goods.get(position);
        Good good = (Good) goodData.get("good");
        double quantity = (double) goodData.get("quantity");
        double price = (double) goodData.get("price");
        ((GoodViewHolder) holder).goodName.setText(good.toString());
        ((GoodViewHolder) holder).goodQuantity.setText(String.valueOf(quantity));
        ((GoodViewHolder) holder).goodPrice.setText(String.valueOf(price));
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public HashMap<Integer, HashMap<String, Object>> getGoods() {
        return goods;
    }

    public double getSum() {
        double sum = 0.0;
        for (int i = 0; i < goods.size(); i++) {
            HashMap<String, Object> good = goods.get(i);
            sum += (Double) good.get("sum");
        }
        return sum;
    }

    public class GoodViewHolder extends RecyclerView.ViewHolder {
        public TextView goodName;
        public EditText goodQuantity;
        public EditText goodPrice;
        public Button btnPlus;
        public Button btnMinus;

        public TextView.OnEditorActionListener onEditorActionListener = (v, actionId, event) -> {
            switch (actionId) {
                case EditorInfo.IME_ACTION_DONE:
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    return true;
            }
            return false;
        };

        public View.OnFocusChangeListener onFocusChangeListener = (v, hasFocus) -> {
            if (!hasFocus) {
                int position = getAdapterPosition();
                HashMap<String, Object> goodInfo = goods.get(position);
                double quantity = 0;
                double price = 0;
                View root = v.getRootView();
                TextView tvSum = root.findViewById(R.id.order_tv_sum);
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
                        EditText etQuantity = root.findViewById(R.id.good_quantity);
                        try {
                            quantity = Double.parseDouble((etQuantity.getText().toString()));
                        } catch (NumberFormatException e) {
                            quantity = 0;
                        }
                        break;
                }
                double sum = quantity * price;
                goodInfo.put("sum", sum);
                tvSum.setText(String.valueOf(getSum()));
            }
        };

        public GoodViewHolder(View view) {
            super(view);
            goodName = view.findViewById(R.id.good_name);
            goodQuantity = view.findViewById(R.id.good_quantity);
            goodPrice = view.findViewById(R.id.good_price);
            btnPlus = view.findViewById(R.id.good_plus);
            btnMinus = view.findViewById(R.id.good_minus);
            btnPlus.setOnClickListener(this::onButtonClick);
            btnMinus.setOnClickListener(this::onButtonClick);
            goodQuantity.setOnFocusChangeListener(onFocusChangeListener);
            goodQuantity.setOnEditorActionListener(onEditorActionListener);
            goodPrice.setOnFocusChangeListener(onFocusChangeListener);
            goodPrice.setOnEditorActionListener(onEditorActionListener);
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
}

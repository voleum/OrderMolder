package dev.voleum.ordermolder.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.objects.Good;

public class GoodsOrderRecyclerViewAdapter extends RecyclerView.Adapter {

    private HashMap<Integer, HashMap<String, Object>> goods;

    public GoodsOrderRecyclerViewAdapter(HashMap<Integer, HashMap<String, Object>> goods) {
        this.goods = goods;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_good_holder, parent, false);
        return new GoodViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HashMap<String, Object> goodData = goods.get(position);
        try {
            Good good = (Good) goodData.get("good");
            double quantity = (double) goodData.get("quantity");
            double price = (double) goodData.get("price");
            ((GoodViewHolder) holder).goodName.setText(good.toString());
            ((GoodViewHolder) holder).goodQuantity.setText(String.valueOf(quantity));
            ((GoodViewHolder) holder).goodPrice.setText(String.valueOf(price));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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
            try {
                sum += (double) good.get("sum");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class GoodViewHolder extends RecyclerView.ViewHolder {
        TextView goodName;
        EditText goodQuantity;
        EditText goodPrice;
        Button btnPlus;
        Button btnMinus;

        TextView.OnEditorActionListener onEditorActionListener = (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                try {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    return true;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            return false;
        };

        View.OnFocusChangeListener onFocusChangeListener = (v, hasFocus) -> {
            if (!hasFocus) {
                int position = getAdapterPosition();
                HashMap<String, Object> goodInfo = goods.get(position);
                double quantity = 0;
                double price = 0;
                switch (v.getId()) {
                    case R.id.good_quantity:
                        try {
                            quantity = Double.parseDouble(((EditText) v).getText().toString());
                        } catch (NumberFormatException e) {
                            quantity = 0;
                        }
                        try {
                            goodInfo.put("quantity", quantity);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        price = (double) goodInfo.get("price");
                        break;
                    case R.id.good_price:
                        try {
                            price = Double.parseDouble(((EditText) v).getText().toString());
                        } catch (NumberFormatException e) {
                            price = 0;
                        }
                        try {
                            goodInfo.put("price", price);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        quantity = (double) goodInfo.get("quantity");
                        break;
                }
                double sum = quantity * price;
                try {
                    goodInfo.put("sum", sum);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                View root = v.getRootView();
                TextView tvSum = root.findViewById(R.id.order_tv_sum);
                tvSum.setText(String.valueOf(getSum()));
            }
        };

        GoodViewHolder(View view) {
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

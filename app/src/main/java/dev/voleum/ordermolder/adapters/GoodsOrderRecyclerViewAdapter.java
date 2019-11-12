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

import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.objects.TableGoods;

public class GoodsOrderRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<TableGoods> goods;
    private GoodsOrderRecyclerViewAdapter.OnEntryClickListener onEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View v, int position);
    }

    public GoodsOrderRecyclerViewAdapter(List<TableGoods> goods) {
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
        TableGoods row = goods.get(position);
        try {
            double quantity = row.getQuantity();
            double price = row.getPrice();
            ((GoodViewHolder) holder).goodName.setText(row.getGoodNane());
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

    public void setOnEntryClickListener(GoodsOrderRecyclerViewAdapter.OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public void setData(List<TableGoods> tableGoods) {
        this.goods = tableGoods;
        notifyDataSetChanged();
    }

    public List<TableGoods> getGoods() {
        return goods;
    }

    public double getSum() {
        double sum = 0.0;
        for (int i = 0; i < goods.size(); i++) {
            TableGoods row = goods.get(i);
            try {
                sum += row.getSum();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        GoodViewHolder(View view) {
            super(view);
            goodName = view.findViewById(R.id.good_name);
            goodQuantity = view.findViewById(R.id.good_quantity);
            goodPrice = view.findViewById(R.id.good_price);
            btnPlus = view.findViewById(R.id.good_plus);
            btnMinus = view.findViewById(R.id.good_minus);
            btnPlus.setOnClickListener(this::onClick);
            btnMinus.setOnClickListener(this::onClick);
            goodQuantity.setOnEditorActionListener(onEditorActionListener);
            goodPrice.setOnEditorActionListener(onEditorActionListener);
        }

        @Override
        public void onClick(View v) {
//            int position = getAdapterPosition();
//            TableGoods row = goods.get(position);
//            double currentQuantity;
//            try {
//                currentQuantity = Double.parseDouble(goodQuantity.getText().toString());
//            } catch (NumberFormatException e) {
//                currentQuantity = 0.0;
//            }
//            String newQuantity;
//            switch (v.getId()) {
//                case R.id.good_plus:
//                    newQuantity = String.valueOf(++currentQuantity);
//                    goodQuantity.setText(newQuantity);
//                    break;
//                case R.id.good_minus:
//                    if (currentQuantity > 1) newQuantity = String.valueOf(--currentQuantity);
//                    else newQuantity = String.valueOf(1.0);
//                    goodQuantity.setText(newQuantity);
//                    break;
//            }
//            try {
//                double currentPrice = row.getPrice();
//                double sum = currentQuantity * currentPrice;
//                row.setQuantity(currentQuantity);
//                row.setSum(sum);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            if (onEntryClickListener != null) {
                onEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }

        private void onButtonClick(View v) {
            int position = getAdapterPosition();
            TableGoods row = goods.get(position);
            double currentQuantity;
            try {
                currentQuantity = Double.parseDouble(goodQuantity.getText().toString());
            } catch (NumberFormatException e) {
                currentQuantity = 0.0;
            }
            String newQuantity;
            switch (v.getId()) {
                case R.id.good_plus:
                    newQuantity = String.valueOf(++currentQuantity);
                    goodQuantity.setText(newQuantity);
                    break;
                case R.id.good_minus:
                    if (currentQuantity > 1) newQuantity = String.valueOf(--currentQuantity);
                    else newQuantity = String.valueOf(1.0);
                    goodQuantity.setText(newQuantity);
                    break;
            }
            try {
                double currentPrice = row.getPrice();
                double sum = currentQuantity * currentPrice;
                row.setQuantity(currentQuantity);
                row.setSum(sum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

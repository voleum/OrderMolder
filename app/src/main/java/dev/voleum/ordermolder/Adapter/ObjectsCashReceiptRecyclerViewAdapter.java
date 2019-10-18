// TODO: EDIT ALL THE CLASS!!!
package dev.voleum.ordermolder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import dev.voleum.ordermolder.Object.Order;
import dev.voleum.ordermolder.R;

public class ObjectsCashReceiptRecyclerViewAdapter extends RecyclerView.Adapter {

    private HashMap<Integer, HashMap<String, Object>> objects;

    public ObjectsCashReceiptRecyclerViewAdapter(HashMap<Integer, HashMap<String, Object>> objects) {
        this.objects = objects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.object_holder, parent, false);
        return new ObjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HashMap<String, Object> objectData = objects.get(position);
        Order object = (Order) objectData.get("object");
        double sum = (double) objectData.get("sum_credit");
        ((ObjectViewHolder) holder).objectName.setText(object.toString());
        ((ObjectViewHolder) holder).objectSum.setText(String.valueOf(sum));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public HashMap<Integer, HashMap<String, Object>> getObjects() {
        return objects;
    }

    public double getSum() {
        double sum = 0.0;
        for (int i = 0; i < objects.size(); i++) {
            HashMap<String, Object> object = objects.get(i);
            sum += (Double) object.get("sum_credit");
        }
        return sum;
    }

    public class ObjectViewHolder extends RecyclerView.ViewHolder {
        public TextView objectName;
        public EditText objectSum;

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
                HashMap<String, Object> objectInfo = objects.get(position);
                double objectSum = 0;
                View root = v.getRootView();
                TextView tvSum = root.findViewById(R.id.cash_receipt_tv_sum);
                if (v.getId() == R.id.object_sum) {
                        try {
                            objectSum = Double.parseDouble(((EditText) v).getText().toString());
                        } catch (NumberFormatException e) {
                            objectSum = 0;
                        }
                        objectInfo.put("sum_credit", objectSum);
                }
                tvSum.setText(String.valueOf(getSum()));
            }
        };

        public ObjectViewHolder(View view) {
            super(view);
            objectName = view.findViewById(R.id.object_name);
            objectSum = view.findViewById(R.id.object_sum);
            objectSum.setOnFocusChangeListener(onFocusChangeListener);
            objectSum.setOnEditorActionListener(onEditorActionListener);
        }
    }
}

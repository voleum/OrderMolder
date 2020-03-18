package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.CashReceiptObjectHolderBinding;
import dev.voleum.ordermolder.models.TableObjects;
import dev.voleum.ordermolder.viewmodels.CashReceiptViewModel;
import dev.voleum.ordermolder.viewmodels.ObjectsCashReceiptListItemViewModel;

public class ObjectsCashReceiptRecyclerViewAdapter extends AbstractRecyclerViewAdapter<TableObjects> {

    private OnEntryLongClickListener onEntryLongClickListener;
    private CashReceiptViewModel cashReceiptViewModel;

    public ObjectsCashReceiptRecyclerViewAdapter(List<TableObjects> list, CashReceiptViewModel cashReceiptViewModel) {
        super(list);
        this.cashReceiptViewModel = cashReceiptViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CashReceiptObjectHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.cash_receipt_object_holder,
                parent,
                false);
        return new ObjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ObjectsCashReceiptRecyclerViewAdapter.ObjectViewHolder) holder)
                .binding.setRow(new ObjectsCashReceiptListItemViewModel(list.get(position), cashReceiptViewModel));
    }

    public void setOnEntryLongClickListener(OnEntryLongClickListener onEntryLongClickListener) {
        this.onEntryLongClickListener = onEntryLongClickListener;
    }

    public class ObjectViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        CashReceiptObjectHolderBinding binding;

        ObjectViewHolder(CashReceiptObjectHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (onEntryLongClickListener != null) {
                onEntryLongClickListener.onEntryLongClick(v, getLayoutPosition());
                return true;
            }
            return false;
        }
    }

    public interface OnEntryLongClickListener {
        void onEntryLongClick(View v, int position);
    }
}

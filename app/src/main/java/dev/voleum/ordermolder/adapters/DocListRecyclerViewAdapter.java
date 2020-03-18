package dev.voleum.ordermolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.DocHolderBinding;
import dev.voleum.ordermolder.models.Document;
import dev.voleum.ordermolder.viewmodels.ObjListItemViewModel;

public class DocListRecyclerViewAdapter<T extends Document> extends AbstractRecyclerViewAdapter<T> {

    private OnEntryClickListener onEntryClickListener;
    private OnEntryLongClickListener onEntryLongClickListener;

    public DocListRecyclerViewAdapter(List<T> list) {
        super(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DocHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.doc_holder,
                parent,
                false);
        return new DocViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DocViewHolder) holder).binding.setRow(new ObjListItemViewModel<>(list.get(position)));
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        this.onEntryClickListener = onEntryClickListener;
    }

    public void setOnEntryLongClickListener(OnEntryLongClickListener onEntryLongClickListener) {
        this.onEntryLongClickListener = onEntryLongClickListener;
    }

    public class DocViewHolder extends RecyclerView.ViewHolder {
        DocHolderBinding binding;
        DocViewHolder(DocHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(v -> {
                if (onEntryClickListener != null) {
                    onEntryClickListener.onEntryClick(v, getLayoutPosition());
                }
            });
            this.binding.getRoot().setOnLongClickListener(v -> {
                if (onEntryLongClickListener != null) {
                    onEntryLongClickListener.onEntryLongClick(v, getLayoutPosition());
                    return true;
                }
                return false;
            });
        }
    }

    public interface OnEntryClickListener {
        void onEntryClick(View v, int position);
    }

    public interface OnEntryLongClickListener {
        void onEntryLongClick(View v, int position);
    }
}

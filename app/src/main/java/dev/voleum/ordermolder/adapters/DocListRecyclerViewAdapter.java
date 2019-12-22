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
import dev.voleum.ordermolder.objects.Document;
import dev.voleum.ordermolder.viewmodels.DocListItemViewModel;

public class DocListRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Document> docs;
    private OnEntryCLickListener onEntryCLickListener;

    public interface OnEntryCLickListener {
        void onEntryClick(View v, int position);
    }

    public DocListRecyclerViewAdapter(List<Document> docs) {
        this.docs = docs;
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
        Document row = docs.get(position);
        ((DocViewHolder) holder).binding.setRow(new DocListItemViewModel(row));
    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    public void setOnEntryCLickListener(OnEntryCLickListener onEntryCLickListener) {
        this.onEntryCLickListener = onEntryCLickListener;
    }

    public void setData(List<Document> docs) {
        this.docs = docs;
        notifyDataSetChanged();
    }

    public class DocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        DocHolderBinding binding;
        DocViewHolder(DocHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onEntryCLickListener != null) {
                onEntryCLickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }
}

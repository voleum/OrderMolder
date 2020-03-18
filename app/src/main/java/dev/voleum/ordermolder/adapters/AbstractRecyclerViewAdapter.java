package dev.voleum.ordermolder.adapters;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.models.Obj;

public abstract class AbstractRecyclerViewAdapter<T extends Obj> extends RecyclerView.Adapter {

    protected List<T> list;

    public AbstractRecyclerViewAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}

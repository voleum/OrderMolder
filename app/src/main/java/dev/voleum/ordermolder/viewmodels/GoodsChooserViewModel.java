package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.adapters.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.ViewModelObservable;
import dev.voleum.ordermolder.models.Price;

public class GoodsChooserViewModel extends ViewModelObservable {

    private List<Price> prices;
    private GoodsChooserRecyclerViewAdapter adapter;
    private GoodsChooserRecyclerViewAdapter.OnEntryClickListener onEntryClickListener;

    public GoodsChooserViewModel() {

    }

    @Bindable
    public void setAdapter(GoodsChooserRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public GoodsChooserRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public List<Price> getGoods() {
        return prices;
    }

    @BindingAdapter("goodsData")
    public static void setData(RecyclerView recyclerView, List<Price> goods) {
        if (recyclerView.getAdapter() instanceof GoodsChooserRecyclerViewAdapter) {
            ((GoodsChooserRecyclerViewAdapter) recyclerView.getAdapter()).setData(goods);
        }
    }

    public void init() {
        initGoodList();
    }

    private void initGoodList() {

        DbRoom db = OrderMolder.getApplication().getDatabase();

        prices = db.getPriceDao().getAll();

        adapter = new GoodsChooserRecyclerViewAdapter(prices);
        adapter.setOnEntryClickListener(onEntryClickListener);
    }

//    public void setOnEntryClickListener(GoodsChooserRecyclerViewAdapter.OnEntryClickListener onEntryClickListener) {
//        this.onEntryClickListener = onEntryClickListener;
//    }
}

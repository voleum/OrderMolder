package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.adapters.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.models.Price;

public class GoodsChooserViewModel extends AbstractChooserViewModel<Price, GoodsChooserRecyclerViewAdapter> {

    @BindingAdapter("goodsData")
    public static void setData(RecyclerView recyclerView, List<Price> goods) {
        if (recyclerView.getAdapter() instanceof GoodsChooserRecyclerViewAdapter) {
            ((GoodsChooserRecyclerViewAdapter) recyclerView.getAdapter()).setData(goods);
        }
    }

    public void initList() {
        DbRoom db = OrderMolder.getApplication().getDatabase();
        setItems(db.getPriceDao().getAll());
        adapter = new GoodsChooserRecyclerViewAdapter(getItems());
    }
}

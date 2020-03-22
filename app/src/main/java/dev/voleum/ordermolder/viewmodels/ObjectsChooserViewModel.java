package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.adapters.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.adapters.ObjectsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.models.Order;

public class ObjectsChooserViewModel extends AbstractChooserViewModel<Order, ObjectsChooserRecyclerViewAdapter> {

    @BindingAdapter("ordersData")
    public static void setData(RecyclerView recyclerView, List<Order> orders) {
        if (recyclerView.getAdapter() instanceof GoodsChooserRecyclerViewAdapter) {
            ((ObjectsChooserRecyclerViewAdapter) recyclerView.getAdapter()).setData(orders);
        }
    }

    public void initList(String companyUid, String partnerUid) {
        DbRoom db = OrderMolder.getApplication().getDatabase();
        setItems(db.getOrderDao().getByCompanyAndPartner(companyUid, partnerUid));
        adapter = new ObjectsChooserRecyclerViewAdapter(getItems());
    }
}

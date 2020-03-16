package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.adapters.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.adapters.ObjectsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.helpers.ViewModelObservable;
import dev.voleum.ordermolder.models.Order;

public class ObjectsChooserViewModel extends ViewModelObservable {

    private List<Order> orders;
    private ObjectsChooserRecyclerViewAdapter adapter;

    public ObjectsChooserViewModel() {

    }

    @Bindable
    public void setAdapter(ObjectsChooserRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public ObjectsChooserRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public List<Order> getOrders() {
        return orders;
    }

    @BindingAdapter("ordersData")
    public static void setData(RecyclerView recyclerView, List<Order> orders) {
        if (recyclerView.getAdapter() instanceof GoodsChooserRecyclerViewAdapter) {
            ((ObjectsChooserRecyclerViewAdapter) recyclerView.getAdapter()).setData(orders);
        }
    }

    public void init(String companyUid, String partnerUid) {
        initObjectsList(companyUid, partnerUid);
    }

    private void initObjectsList(String companyUid, String partnerUid) {

//        objects = new ArrayList<>();
//
//        DbHelper dbHelper = DbHelper.getInstance();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String orderBy = DbHelper.COLUMN_DATE;
//        String[] selectionArgs = {companyUid, partnerUid};
//        Cursor c = db.rawQuery("SELECT *" +
//                        " FROM " + DbHelper.TABLE_ORDERS +
//                        " WHERE " + DbHelper.COLUMN_COMPANY_UID + " = ?" +
//                        " AND " + DbHelper.COLUMN_PARTNER_UID + " = ?" +
//                        " ORDER BY " + orderBy,
//                selectionArgs);
//
//        HashMap<String, Object> values;
//
//        if (c.moveToFirst()) {
//            int uidIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
//            int dateIndex = c.getColumnIndex(DbHelper.COLUMN_DATE);
//            int companyIndex = c.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
//            int partnerIndex = c.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
//            int warehouseIndex = c.getColumnIndex(DbHelper.COLUMN_WAREHOUSE_UID);
//            int sumIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
//            do {
//                values = new HashMap<>();
//                values.put(ObjectsChooserActivity.OBJECT, new Order(c.getString(uidIndex),
//                        c.getString(dateIndex),
//                        c.getString(companyIndex),
//                        c.getString(partnerIndex),
//                        c.getString(warehouseIndex),
//                        c.getDouble(sumIndex)));
//                values.put(ObjectsChooserActivity.SUM, c.getDouble(sumIndex));
//                objects.add(values);
//            } while (c.moveToNext());
//        }
//        c.close();
//        db.close();

        DbRoom db = OrderMolder.getApplication().getDatabase();

        orders = db.getOrderDao().getByCompanyAndPartner(companyUid, partnerUid);

        adapter = new ObjectsChooserRecyclerViewAdapter(orders);
    }
}

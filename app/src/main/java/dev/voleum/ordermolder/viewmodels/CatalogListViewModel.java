package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.adapters.CatalogListRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.enums.CatalogTypes;
import dev.voleum.ordermolder.models.Catalog;

public class CatalogListViewModel<T extends Catalog> extends BaseObservable {

    private List<T> catalogs;
    private CatalogListRecyclerViewAdapter adapter;
    private CatalogTypes catType;

    public CatalogListViewModel(CatalogTypes catType) {
        this.catType = catType;
        initCatalogList();
    }

    @Bindable
    public void setAdapter(CatalogListRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Bindable
    public CatalogListRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Bindable
    public List<T> getCatalogs() {
        return catalogs;
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<? extends Catalog> catalogs) {
        if (recyclerView.getAdapter() instanceof CatalogListRecyclerViewAdapter) {
            ((CatalogListRecyclerViewAdapter) recyclerView.getAdapter()).setData(catalogs);
        }
    }

    private void initCatalogList() {

        DbRoom db = OrderMolder.getApplication().getDatabase();

        switch (catType) {
            case COMPANY:
                catalogs = (List<T>) db.getCompanyDao().getAll();
                break;
            case PARTNER:
                catalogs = (List<T>) db.getPartnerDao().getAll();
                break;
            case WAREHOUSE:
                catalogs = (List<T>) db.getWarehouseDao().getAll();
                break;
            case GOOD:
                catalogs = (List<T>) db.getGoodDao().getAll();
                break;
            case UNIT:
                catalogs = (List<T>) db.getUnitDao().getAll();
                break;
        }

        adapter = new CatalogListRecyclerViewAdapter(catalogs);
    }
}

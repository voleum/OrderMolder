package dev.voleum.ordermolder.viewmodels;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.adapters.CatalogListRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbRoom;
import dev.voleum.ordermolder.enums.CatalogTypes;
import dev.voleum.ordermolder.models.Catalog;

public class CatalogListViewModel extends ObjListViewModel<Catalog, CatalogListRecyclerViewAdapter> {

    private CatalogTypes catType;

    public CatalogListViewModel(CatalogTypes catType) {
        this.catType = catType;
        initList();
    }

    @BindingAdapter("android:data")
    public static void setData(RecyclerView recyclerView, List<? extends Catalog> catalogs) {
        if (recyclerView.getAdapter() instanceof CatalogListRecyclerViewAdapter) {
            ((CatalogListRecyclerViewAdapter) recyclerView.getAdapter()).setData(catalogs);
        }
    }

    protected void initList() {

        DbRoom db = OrderMolder.getApplication().getDatabase();

        switch (catType) {
            case COMPANY:
                objs = db.getCompanyDao().getAll();
                break;
            case PARTNER:
                objs = db.getPartnerDao().getAll();
                break;
            case WAREHOUSE:
                objs = db.getWarehouseDao().getAll();
                break;
            case GOOD:
                objs = db.getGoodDao().getAll();
                break;
            case UNIT:
                objs = db.getUnitDao().getAll();
                break;
        }

        adapter = new CatalogListRecyclerViewAdapter(objs);
    }
}

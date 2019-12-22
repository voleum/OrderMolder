package dev.voleum.ordermolder.ui.catalogs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.CatalogListRecyclerViewAdapter;
import dev.voleum.ordermolder.databinding.ActivityCatListBinding;
import dev.voleum.ordermolder.enums.CatalogTypes;
import dev.voleum.ordermolder.objects.Catalog;
import dev.voleum.ordermolder.viewmodels.CatalogListViewModel;

public class CatalogListActivity extends AppCompatActivity {

    CatalogListViewModel catalogListViewModel;
    RecyclerView recyclerCatalogs;

    private CatalogTypes catalogType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        catalogType = (CatalogTypes) getIntent().getSerializableExtra(CatalogActivity.CAT_TYPE);

        catalogListViewModel = new CatalogListViewModel(catalogType);

        ActivityCatListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_cat_list);
        binding.setViewModel(catalogListViewModel);
        binding.executePendingBindings();

        recyclerCatalogs = binding.getRoot().findViewById(R.id.recycler_catalogs);
        recyclerCatalogs.setHasFixedSize(true);
        recyclerCatalogs.setLayoutManager(new LinearLayoutManager(this));

        CatalogListRecyclerViewAdapter.OnEntryClickListener onEntryClickListener = (v, position) -> {
            Catalog clickedCatalog = binding.getViewModel().getCatalogs().get(position);
            Intent intentOut = new Intent(CatalogListActivity.this, CatalogActivity.class)
                    .putExtra(CatalogActivity.CAT_TYPE, catalogType)
                    .putExtra(CatalogActivity.CAT, clickedCatalog);
            startActivity(intentOut);
        };

        binding.getViewModel().getAdapter().setOnEntryClickListener(onEntryClickListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitleDependOnType();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setTitleDependOnType() {
        switch (catalogType) {
            case COMPANY:
                setTitle(R.string.title_activity_companies);
                break;
            case PARTNER:
                setTitle(R.string.title_activity_partners);
                break;
            case GOOD:
                setTitle(R.string.title_activity_goods);
                break;
            case WAREHOUSE:
                setTitle(R.string.title_activity_warehouses);
                break;
            case UNIT:
                setTitle(R.string.title_activity_units);
                break;
        }
    }
}

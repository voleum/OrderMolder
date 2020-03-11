package dev.voleum.ordermolder.ui.catalogs;

import android.content.Context;
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
import dev.voleum.ordermolder.models.Catalog;
import dev.voleum.ordermolder.viewmodels.CatalogListViewModel;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CatalogListActivity extends AppCompatActivity {

    CatalogListViewModel catalogListViewModel;
    RecyclerView recyclerCatalogs;

    ActivityCatListBinding binding;

    private CatalogTypes catalogType;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        catalogType = (CatalogTypes) getIntent().getSerializableExtra(CatalogActivity.CAT_TYPE);
        setTitleDependOnType();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cat_list);

        initData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        binding.setViewModel(catalogListViewModel);
                        binding.executePendingBindings();

                        recyclerCatalogs = binding.getRoot().findViewById(R.id.recycler_catalogs);
                        recyclerCatalogs.setHasFixedSize(true);
                        recyclerCatalogs.setLayoutManager(new LinearLayoutManager(context));

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
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Completable initData() {
        return Completable.create(subscriber -> {
            catalogListViewModel = new CatalogListViewModel(catalogType);
            subscriber.onComplete();
        });
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

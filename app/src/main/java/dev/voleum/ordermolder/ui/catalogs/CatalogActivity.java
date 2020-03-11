package dev.voleum.ordermolder.ui.catalogs;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.ActivityCatBinding;
import dev.voleum.ordermolder.objects.Catalog;
import dev.voleum.ordermolder.viewmodels.CatalogViewModel;

public class CatalogActivity extends AppCompatActivity {

    public static final String CAT = "cat";
    public static final String CAT_TYPE = "cat_type";

    Catalog catalog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);

        catalog = (Catalog) getIntent().getSerializableExtra(CAT);

        ActivityCatBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_cat);
        CatalogViewModel viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        viewModel.setCatalog(catalog);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

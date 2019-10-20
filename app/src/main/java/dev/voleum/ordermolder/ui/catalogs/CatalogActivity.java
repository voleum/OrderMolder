package dev.voleum.ordermolder.ui.catalogs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import java.util.Objects;

import dev.voleum.ordermolder.Enums.CatalogTypes;
import dev.voleum.ordermolder.Object.Catalog;
import dev.voleum.ordermolder.Object.EconomicEntity;
import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.Object.Unit;
import dev.voleum.ordermolder.R;

public class CatalogActivity extends AppCompatActivity {

    public static final String CAT = "cat";
    public static final String CAT_TYPE = "cat_type";

    private CatalogTypes catType;

    Catalog catalog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);

        catType = (CatalogTypes) getIntent().getSerializableExtra(CAT_TYPE);
        catalog = (Catalog) getIntent().getSerializableExtra(CAT);

        Toolbar toolbar = findViewById(R.id.catalog_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ConstraintLayout layout = findViewById(R.id.catalog_constraint);

        Constraints.LayoutParams nameLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameLayoutParams.startToStart = R.id.catalog_constraint;
        nameLayoutParams.setMargins(16, 16, 16, 16);
        nameLayoutParams.topToBottom = R.id.catalog_toolbar;

        TextView tvName = new TextView(this);
        tvName.setLayoutParams(nameLayoutParams);
        tvName.setId(View.generateViewId());
        tvName.setText(catalog.getName());
        layout.addView(tvName);

        switch (catType) {
            case COMPANY:
            case PARTNER:
                Constraints.LayoutParams tinLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tinLayoutParams.startToStart = R.id.catalog_constraint;
                tinLayoutParams.setMargins(16, 16, 16, 16);
                tinLayoutParams.topToBottom = tvName.getId();

                TextView tvTin = new TextView(this);
                tvTin.setLayoutParams(tinLayoutParams);
                tvTin.setId(View.generateViewId());
                tvTin.setText(((EconomicEntity) catalog).getTin());
                layout.addView(tvTin);
                break;
            case GOOD:
                Constraints.LayoutParams groupLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                groupLayoutParams.startToStart = R.id.catalog_constraint;
                groupLayoutParams.setMargins(16, 16, 16, 16);
                groupLayoutParams.topToBottom = tvName.getId();

                TextView tvGroup = new TextView(this);
                tvGroup.setLayoutParams(groupLayoutParams);
                tvGroup.setId(View.generateViewId());
                layout.addView(tvGroup);

                Constraints.LayoutParams unitLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                unitLayoutParams.startToStart = R.id.catalog_constraint;
                unitLayoutParams.setMargins(16, 16, 16, 16);
                unitLayoutParams.topToBottom = tvGroup.getId();

                TextView tvUnit = new TextView(this);
                tvUnit.setLayoutParams(unitLayoutParams);
                tvUnit.setId(View.generateViewId());
                tvUnit.setText(((Good) catalog).getUnitUid());
                layout.addView(tvUnit);
                break;
            case WAREHOUSE:
                break;
            case UNIT:
                Constraints.LayoutParams codeLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                codeLayoutParams.startToStart = R.id.catalog_constraint;
                codeLayoutParams.setMargins(16, 16, 16, 16);
                codeLayoutParams.topToBottom = tvName.getId();

                TextView tvCode = new TextView(this);
                tvCode.setLayoutParams(codeLayoutParams);
                tvCode.setId(View.generateViewId());
                tvCode.setText(String.valueOf(((Unit) catalog).getCode()));
                layout.addView(tvCode);

                Constraints.LayoutParams fullNameLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                fullNameLayoutParams.startToStart = R.id.catalog_constraint;
                fullNameLayoutParams.setMargins(16, 16, 16, 16);
                fullNameLayoutParams.topToBottom = tvCode.getId();

                TextView tvFullName = new TextView(this);
                tvFullName.setLayoutParams(fullNameLayoutParams);
                tvFullName.setId(View.generateViewId());
                tvFullName.setText(((Unit) catalog).getFullName());
                layout.addView(tvFullName);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

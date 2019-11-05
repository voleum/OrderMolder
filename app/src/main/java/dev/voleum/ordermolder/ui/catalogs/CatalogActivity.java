package dev.voleum.ordermolder.ui.catalogs;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import java.util.Objects;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.enums.CatalogTypes;
import dev.voleum.ordermolder.objects.Catalog;
import dev.voleum.ordermolder.objects.EconomicEntity;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Unit;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ConstraintLayout layout = findViewById(R.id.catalog_constraint);

        ContextThemeWrapper contextThemeWrapperLabels = new ContextThemeWrapper(this, R.style.Widget_OrderMolder_TextView_Padding);
        ContextThemeWrapper contextThemeWrapperValues = new ContextThemeWrapper(this, R.style.Widget_OrderMolder_TextView_Padding_RoundedColored);
        int tvMargin = getResources().getDimensionPixelSize(R.dimen.rounded_textview_margin);

        Constraints.LayoutParams nameLabelLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameLabelLayoutParams.startToStart = R.id.catalog_constraint;
        nameLabelLayoutParams.topToTop = R.id.catalog_constraint;
        nameLabelLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

        TextView tvNameLabel = new TextView(contextThemeWrapperLabels);
        tvNameLabel.setLayoutParams(nameLabelLayoutParams);
        tvNameLabel.setId(View.generateViewId());
        tvNameLabel.setText(R.string.object_name);
        layout.addView(tvNameLabel);

        Constraints.LayoutParams nameValueLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameValueLayoutParams.startToEnd = tvNameLabel.getId();
        nameValueLayoutParams.topToTop = R.id.catalog_constraint;
        nameValueLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

        TextView tvNameValue = new TextView(contextThemeWrapperValues);
        tvNameValue.setLayoutParams(nameValueLayoutParams);
        tvNameValue.setId(View.generateViewId());
        tvNameValue.setText(catalog.getName());
        layout.addView(tvNameValue);

        switch (catType) {
            case COMPANY:
            case PARTNER:
                Constraints.LayoutParams tinLabelLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tinLabelLayoutParams.startToStart = R.id.catalog_constraint;
                tinLabelLayoutParams.topToBottom = tvNameLabel.getId();
                tinLabelLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvTinLabel = new TextView(contextThemeWrapperLabels);
                tvTinLabel.setLayoutParams(tinLabelLayoutParams);
                tvTinLabel.setId(View.generateViewId());
                tvTinLabel.setText(R.string.object_tin);
                layout.addView(tvTinLabel);

                Constraints.LayoutParams tinValueLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tinValueLayoutParams.startToEnd = tvTinLabel.getId();
                tinValueLayoutParams.topToBottom = tvNameValue.getId();
                tinValueLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvTinValue = new TextView(contextThemeWrapperValues);
                tvTinValue.setLayoutParams(tinValueLayoutParams);
                tvTinValue.setId(View.generateViewId());
                tvTinValue.setText(((EconomicEntity) catalog).getTin());
                layout.addView(tvTinValue);
                break;
            case GOOD:
                Constraints.LayoutParams groupLabelLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                groupLabelLayoutParams.startToStart = R.id.catalog_constraint;
                groupLabelLayoutParams.topToBottom = tvNameLabel.getId();
                groupLabelLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvGroupLabel = new TextView(contextThemeWrapperLabels);
                tvGroupLabel.setLayoutParams(groupLabelLayoutParams);
                tvGroupLabel.setId(View.generateViewId());
                tvGroupLabel.setText(R.string.object_group);
                layout.addView(tvGroupLabel);

                Constraints.LayoutParams groupValueLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                groupValueLayoutParams.startToEnd = tvGroupLabel.getId();
                groupValueLayoutParams.topToBottom = tvNameValue.getId();
                groupValueLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvGroupValue = new TextView(contextThemeWrapperValues);
                tvGroupValue.setLayoutParams(groupValueLayoutParams);
                tvGroupValue.setId(View.generateViewId());
                tvGroupValue.setText(((Good) catalog).getGroupUid());
                layout.addView(tvGroupValue);

                Constraints.LayoutParams unitLabelLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                unitLabelLayoutParams.startToStart = R.id.catalog_constraint;
                unitLabelLayoutParams.topToBottom = tvGroupLabel.getId();
                unitLabelLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvUnitLabel = new TextView(contextThemeWrapperLabels);
                tvUnitLabel.setLayoutParams(unitLabelLayoutParams);
                tvUnitLabel.setId(View.generateViewId());
                tvUnitLabel.setText(R.string.object_unit);
                layout.addView(tvUnitLabel);

                Constraints.LayoutParams unitLayoutValueParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                unitLayoutValueParams.startToEnd = tvUnitLabel.getId();
                unitLayoutValueParams.topToBottom = tvGroupValue.getId();
                unitLayoutValueParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvUnitValue = new TextView(contextThemeWrapperValues);
                tvUnitValue.setLayoutParams(unitLayoutValueParams);
                tvUnitValue.setId(View.generateViewId());
                tvUnitValue.setText(((Good) catalog).getUnitUid());
                layout.addView(tvUnitValue);
                break;
            case WAREHOUSE:
                break;
            case UNIT:
                Constraints.LayoutParams codeLabelLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                codeLabelLayoutParams.startToStart = R.id.catalog_constraint;
                codeLabelLayoutParams.topToBottom = tvNameLabel.getId();
                codeLabelLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvCodeLabel = new TextView(contextThemeWrapperLabels);
                tvCodeLabel.setLayoutParams(codeLabelLayoutParams);
                tvCodeLabel.setId(View.generateViewId());
                tvCodeLabel.setText(R.string.object_code);
                layout.addView(tvCodeLabel);

                Constraints.LayoutParams codeValueLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                codeValueLayoutParams.startToEnd = tvCodeLabel.getId();
                codeValueLayoutParams.topToBottom = tvNameValue.getId();
                codeValueLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvCodeValue = new TextView(contextThemeWrapperValues);
                tvCodeValue.setLayoutParams(codeValueLayoutParams);
                tvCodeValue.setId(View.generateViewId());
                tvCodeValue.setText(String.valueOf(((Unit) catalog).getCode()));
                layout.addView(tvCodeValue);

                Constraints.LayoutParams fullNameLabelLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                fullNameLabelLayoutParams.startToStart = R.id.catalog_constraint;
                fullNameLabelLayoutParams.topToBottom = tvCodeLabel.getId();
                fullNameLabelLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvFullNameLabel = new TextView(contextThemeWrapperLabels);
                tvFullNameLabel.setLayoutParams(fullNameLabelLayoutParams);
                tvFullNameLabel.setId(View.generateViewId());
                tvFullNameLabel.setText(R.string.object_full_name);
                layout.addView(tvFullNameLabel);

                Constraints.LayoutParams fullNameValueLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                fullNameValueLayoutParams.startToEnd = tvFullNameLabel.getId();
                fullNameValueLayoutParams.topToBottom = tvCodeValue.getId();
                fullNameValueLayoutParams.setMargins(tvMargin, tvMargin, tvMargin, tvMargin);

                TextView tvFullNameValue = new TextView(contextThemeWrapperValues);
                tvFullNameValue.setLayoutParams(fullNameValueLayoutParams);
                tvFullNameValue.setId(View.generateViewId());
                tvFullNameValue.setText(((Unit) catalog).getFullName());
                layout.addView(tvFullNameValue);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

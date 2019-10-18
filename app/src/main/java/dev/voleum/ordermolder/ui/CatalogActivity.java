package dev.voleum.ordermolder.ui;

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

import dev.voleum.ordermolder.R;

public class CatalogActivity extends AppCompatActivity {

    public static final String DOC_TYPE = "doc_type";
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_COMPANY = 0;
    public static final int TYPE_PARTNER = 1;
    public static final int TYPE_GOOD = 2;
    public static final int TYPE_UNIT = 3;

    private int docType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);

        docType = getIntent().getIntExtra(DOC_TYPE, TYPE_UNKNOWN);

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
        tvName.setText("Name");
        layout.addView(tvName);

        switch (docType) {
            case TYPE_COMPANY:
            case TYPE_PARTNER:
                Constraints.LayoutParams tinLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tinLayoutParams.startToStart = R.id.catalog_constraint;
                tinLayoutParams.setMargins(16, 16, 16, 16);
                tinLayoutParams.topToBottom = tvName.getId();

                TextView tvTin = new TextView(this);
                tvTin.setLayoutParams(tinLayoutParams);
                tvTin.setId(View.generateViewId());
                tvTin.setText("Tin");
                layout.addView(tvTin);
                break;
            case TYPE_GOOD:
                Constraints.LayoutParams groupLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                groupLayoutParams.startToStart = R.id.catalog_constraint;
                groupLayoutParams.setMargins(16, 16, 16, 16);
                groupLayoutParams.topToBottom = tvName.getId();

                TextView tvGroup = new TextView(this);
                tvGroup.setLayoutParams(groupLayoutParams);
                tvGroup.setId(View.generateViewId());
                tvGroup.setText("Group");
                layout.addView(tvGroup);

                Constraints.LayoutParams unitLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                unitLayoutParams.startToStart = R.id.catalog_constraint;
                unitLayoutParams.setMargins(16, 16, 16, 16);
                unitLayoutParams.topToBottom = tvGroup.getId();

                TextView tvUnit = new TextView(this);
                tvUnit.setLayoutParams(unitLayoutParams);
                tvUnit.setId(View.generateViewId());
                tvUnit.setText("Unit");
                layout.addView(tvUnit);
                break;
            case TYPE_UNIT:
                Constraints.LayoutParams codeLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                codeLayoutParams.startToStart = R.id.catalog_constraint;
                codeLayoutParams.setMargins(16, 16, 16, 16);
                codeLayoutParams.topToBottom = tvName.getId();

                TextView tvCode = new TextView(this);
                tvCode.setLayoutParams(codeLayoutParams);
                tvCode.setId(View.generateViewId());
                tvCode.setText("Code");
                layout.addView(tvCode);
                break;
        }
    }

    private void initializeData() {

    }
}

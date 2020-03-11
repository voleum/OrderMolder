package dev.voleum.ordermolder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import dev.voleum.ordermolder.OrderMolder;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.enums.CatalogTypes;
import dev.voleum.ordermolder.ui.catalogs.CatalogActivity;
import dev.voleum.ordermolder.ui.catalogs.CatalogListActivity;

public class FragmentCatalogs extends androidx.fragment.app.ListFragment {

    private final String[] catalogs = OrderMolder.getApplication().getResources().getStringArray(R.array.catalogs);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, catalogs);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), CatalogListActivity.class);
        CatalogTypes catType;
        switch (position) {
            case 0:
                catType = CatalogTypes.COMPANY;
                break;
            case 1:
                catType = CatalogTypes.PARTNER;
                break;
            case 2:
                catType = CatalogTypes.WAREHOUSE;
                break;
            case 3:
                catType = CatalogTypes.GOOD;
                break;
            case 4:
                catType = CatalogTypes.UNIT;
                break;
            default:
                Snackbar.make(l, R.string.snackbar_unknown_doc_type, Snackbar.LENGTH_SHORT)
                        .setGestureInsetBottomIgnored(true)
                        .show();
                return;
        }
        intent.putExtra(CatalogActivity.CAT_TYPE, catType);
        startActivity(intent);
    }
}

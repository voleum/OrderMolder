package dev.voleum.ordermolder.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import dev.voleum.ordermolder.MainActivity;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.ui.CatalogActivity;
import dev.voleum.ordermolder.ui.CatalogListActivity;

public class FragmentCatalogs extends androidx.fragment.app.ListFragment {

    private final String[] catalogs = MainActivity.getRess().getStringArray(R.array.catalogs);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, catalogs);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), CatalogListActivity.class);
        int catType;
        switch (position) {
            case 0:
                catType = CatalogActivity.TYPE_COMPANY;
                break;
            case 1:
                catType = CatalogActivity.TYPE_PARTNER;
                break;
            case 2:
                catType = CatalogActivity.TYPE_GOOD;
                break;
            case 3:
                catType = CatalogActivity.TYPE_UNIT;
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

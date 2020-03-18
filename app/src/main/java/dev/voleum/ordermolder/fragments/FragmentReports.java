package dev.voleum.ordermolder.fragments;

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

public class FragmentReports extends androidx.fragment.app.ListFragment {

    private final String[] reports = OrderMolder.getApplication().getResources().getStringArray(R.array.reports);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, reports);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Snackbar.make(v, R.string.snackbar_soon, Snackbar.LENGTH_SHORT).show();
    }
}

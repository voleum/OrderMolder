package dev.voleum.ordermolder.fragments;

import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import dev.voleum.ordermolder.R;

public class FragmentReports extends AbstractListFragmentKotlin {

    public FragmentReports() {
        super(R.array.reports);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Snackbar.make(v, R.string.snackbar_soon, Snackbar.LENGTH_SHORT).show();
    }
}

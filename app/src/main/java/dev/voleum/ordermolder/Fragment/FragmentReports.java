package dev.voleum.ordermolder.Fragment;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.Nullable;

import dev.voleum.ordermolder.MainActivity;
import dev.voleum.ordermolder.R;

public class FragmentReports extends androidx.fragment.app.ListFragment {

    private final String[] reports = MainActivity.resources.getStringArray(R.array.reports);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, reports);
        setListAdapter(adapter);
    }
}

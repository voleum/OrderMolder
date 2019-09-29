package dev.voleum.ordermolder.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import dev.voleum.ordermolder.ui.orders.OrderListListActivity;
import dev.voleum.ordermolder.MainActivity;
import dev.voleum.ordermolder.R;

public class FragmentDocuments extends androidx.fragment.app.ListFragment {

    private final String[] documents = MainActivity.resources.getStringArray(R.array.documents);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, documents);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        startActivity(new Intent(getActivity(), OrderListListActivity.class));
    }
}

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

import java.util.Objects;

import dev.voleum.ordermolder.MainActivity;
import dev.voleum.ordermolder.Object.CashReceipt;
import dev.voleum.ordermolder.Object.Order;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.ui.general.DocListActivity;

public class FragmentDocuments extends androidx.fragment.app.ListFragment {

    private final String[] documents = MainActivity.getRess().getStringArray(R.array.documents);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, documents);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), DocListActivity.class);
        switch (position) {
            case 0:
                intent.putExtra(DocListActivity.DOC_TYPE, DocListActivity.TYPE_ORDER);
                intent.putExtra(DocListActivity.DOC_ACTIVITY, Order.class);
                break;
            case 1:
                intent.putExtra(DocListActivity.DOC_TYPE, DocListActivity.TYPE_CASH_RECEIPT);
                intent.putExtra(DocListActivity.DOC_ACTIVITY, CashReceipt.class);
                break;
            default:
                Snackbar.make(l, R.string.snackbar_unknown_doc_type, Snackbar.LENGTH_SHORT)
                        .setGestureInsetBottomIgnored(true)
                        .show();
                return;
        }
        startActivity(intent);
    }
}

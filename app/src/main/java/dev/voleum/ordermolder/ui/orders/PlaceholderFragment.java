package dev.voleum.ordermolder.ui.orders;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import dev.voleum.ordermolder.Helper.DbHelper;
import dev.voleum.ordermolder.Object.Company;
import dev.voleum.ordermolder.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        View root = null;
        switch (index) {
            case 1:
                root = inflater.inflate(R.layout.fragment_order_main, container, false);
                initializeData(root);
                break;
            case 2:
                root = inflater.inflate(R.layout.fragment_goods_list, container, false);
                break;
        }

//        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }

    private void initializeData(View root) {
        // TODO: Переделать всё нахуй
        DbHelper dbHelper = DbHelper.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DbHelper.TABLE_COMPANIES, null, null, null, null, null, null, null);

        Company[] companies = null;

        if (c.moveToFirst()) {
            companies = new Company[c.getCount()];
            int i = 0;
            int idClIndex = c.getColumnIndex(DbHelper.COLUMN_ID);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            do {
                companies[i] = new Company(c.getString(idClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
                i++;
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();

        ArrayAdapter<Company> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, companies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerCompanies = (Spinner) root.findViewById(R.id.spinnerCompanies);
        spinnerCompanies.setAdapter(adapter);
    }

}
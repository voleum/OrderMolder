package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import dev.voleum.ordermolder.Helper.DbHelper;
import dev.voleum.ordermolder.Helper.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.Object.Company;
import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.Object.Partner;
import dev.voleum.ordermolder.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public static final int GOOD_CHOOSE_REQUEST = 0;
    public static final int RESULT_OK = 0;
    public static final String CHOSEN_GOOD_NUMBER = "chosen_good_number";
    public static final String CHOSEN_GOOD_NAME = "chosen_good_name";

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private RecyclerView recyclerGoods;
    private ArrayList<Good> goodsList;
    private GoodsChooserRecyclerViewAdapter adapter;

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
                goodsList = new ArrayList<>();
                recyclerGoods = (RecyclerView) root.findViewById(R.id.recycler);
                recyclerGoods.setHasFixedSize(true);
                recyclerGoods.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new GoodsChooserRecyclerViewAdapter(getContext(), goodsList);
                recyclerGoods.setAdapter(adapter);
                FloatingActionButton fab = getActivity().findViewById(R.id.fab);
                fab.setOnClickListener(
                        (view) -> startActivityForResult(new Intent(getActivity(), GoodsChooser.class), GOOD_CHOOSE_REQUEST)
                );
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GOOD_CHOOSE_REQUEST) {
            if (resultCode == OrderActivity.RESULT_OK) {
                if (data != null) {
                    // TODO: if the good already in list - increase the quantity
                    goodsList.add(goodsList.size(), new Good(data.getStringExtra(CHOSEN_GOOD_NUMBER),
                            data.getStringExtra(CHOSEN_GOOD_NAME),
                            null));
                    adapter.notifyItemInserted(goodsList.size() + 1);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initializeData(View root) {
        DbHelper dbHelper = DbHelper.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        // region Companies
        c = db.query(DbHelper.TABLE_COMPANIES, null, null, null, null, null, null, null);

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

        ArrayAdapter<Company> adapterCompany = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, companies);
        adapterCompany.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerCompanies = (Spinner) root.findViewById(R.id.spinnerCompanies);
        spinnerCompanies.setAdapter(adapterCompany);
        // endregion

        // region Partners
        c = db.query(DbHelper.TABLE_PARTNERS, null, null, null, null, null, null, null);

        Partner[] partners = null;

        if (c.moveToFirst()) {
            partners = new Partner[c.getCount()];
            int i = 0;
            int idClIndex = c.getColumnIndex(DbHelper.COLUMN_ID);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            do {
                partners[i] = new Partner(c.getString(idClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
                i++;
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();

        ArrayAdapter<Partner> adapterPartners = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, partners);
        adapterPartners.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerPartners = (Spinner) root.findViewById(R.id.spinnerPartners);
        spinnerPartners.setAdapter(adapterPartners);
        // endregion

        dbHelper.close();
    }

}
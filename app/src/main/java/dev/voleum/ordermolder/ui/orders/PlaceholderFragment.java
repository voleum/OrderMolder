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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import dev.voleum.ordermolder.Adapter.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.Helper.DbHelper;
import dev.voleum.ordermolder.Helper.SelectDateFragment;
import dev.voleum.ordermolder.Helper.SelectTimeFragment;
import dev.voleum.ordermolder.Object.Company;
import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.Object.Partner;
import dev.voleum.ordermolder.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final int GOOD_CHOOSE_REQUEST = 0;
    static final String CHOSEN_GOOD_NUMBER = "chosen_good_number";
    static final String CHOSEN_GOOD_NAME = "chosen_good_name";

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private RecyclerView recyclerGoods;
    private ArrayList<Good> goodsList;
    private GoodsOrderRecyclerViewAdapter adapter;

    static PlaceholderFragment newInstance(int index) {
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
                TextView tvDate = root.findViewById(R.id.tvDate);
                tvDate.setOnClickListener(v -> {
                    DialogFragment datePickerFragment = new SelectDateFragment();
                    datePickerFragment.show(getParentFragmentManager(), "DatePicker");
                });
                TextView tvTime = root.findViewById(R.id.tvTime);
                tvTime.setOnClickListener(v -> {
                    DialogFragment timePickerFragment = new SelectTimeFragment();
                    timePickerFragment.show(getParentFragmentManager(), "TimePicker");
                });
                break;
            case 2:
                root = inflater.inflate(R.layout.fragment_goods_list, container, false);
                goodsList = new ArrayList<>();
                recyclerGoods = root.findViewById(R.id.recycler);
                recyclerGoods.setHasFixedSize(true);
                recyclerGoods.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new GoodsOrderRecyclerViewAdapter(getContext(), goodsList);
                recyclerGoods.setAdapter(adapter);
                FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
                fab.setOnClickListener(
                        (view) -> startActivityForResult(new Intent(getActivity(), GoodsChooser.class), GOOD_CHOOSE_REQUEST)
                );
                break;
        }

        pageViewModel.getText().observe(this, s -> {
//                textView.setText(s);
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

        assert companies != null;
        ArrayAdapter<Company> adapterCompany = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, companies);
        adapterCompany.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerCompanies = root.findViewById(R.id.spinnerCompanies);
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

        assert partners != null;
        ArrayAdapter<Partner> adapterPartners = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, partners);
        adapterPartners.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerPartners = root.findViewById(R.id.spinnerPartners);
        spinnerPartners.setAdapter(adapterPartners);
        // endregion

        dbHelper.close();
    }

}
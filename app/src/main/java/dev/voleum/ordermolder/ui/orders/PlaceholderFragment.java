package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Objects;

import dev.voleum.ordermolder.Adapter.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.Database.DbHelper;
import dev.voleum.ordermolder.Fragment.SelectDateFragment;
import dev.voleum.ordermolder.Fragment.SelectTimeFragment;
import dev.voleum.ordermolder.Object.Company;
import dev.voleum.ordermolder.Object.Good;
import dev.voleum.ordermolder.Object.Partner;
import dev.voleum.ordermolder.Object.Warehouse;
import dev.voleum.ordermolder.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final int GOOD_CHOOSE_REQUEST = 0;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private RecyclerView recyclerGoods;
    private HashMap<Integer, HashMap<String, Object>> goods;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                TextView tvDate = root.findViewById(R.id.order_tv_date);
                tvDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance()));
                tvDate.setOnClickListener(v -> {
                    // TODO: date from TextView to DatePicker
                    DialogFragment datePickerFragment = new SelectDateFragment();
                    datePickerFragment.setTargetFragment(this, 0);
                    datePickerFragment.show(getParentFragmentManager(), "DatePicker");
                });
                TextView tvTime = root.findViewById(R.id.order_tv_time);
                tvTime.setText(new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance()));
                tvTime.setOnClickListener(v -> {
                    // TODO: time from TextView to TimePicker
                    DialogFragment timePickerFragment = new SelectTimeFragment();
                    timePickerFragment.setTargetFragment(this, 0);
                    timePickerFragment.show(getParentFragmentManager(), "TimePicker");
                });
                break;
            case 2:
                root = inflater.inflate(R.layout.fragment_goods_list, container, false);
                goods = new HashMap<>();
                recyclerGoods = root.findViewById(R.id.recycler_goods);
                recyclerGoods.setHasFixedSize(true);
                recyclerGoods.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new GoodsOrderRecyclerViewAdapter(goods);
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
                    Good chosenGood = (Good) data.getSerializableExtra("good");
                    int position = goods.size();
                    HashMap<String, Object> values = new HashMap<>();
                    values.put("good", chosenGood);
                    values.put("price", 0.0);
                    values.put("quantity", 1.0);
                    values.put("sum", 0.0);
                    goods.put(position, values);
                    adapter.notifyItemInserted(position + 1);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public double getSum() {
        return adapter.getSum();
    }

    private void initializeData(View root) {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        // region Companies
        c = db.query(DbHelper.TABLE_COMPANIES, null, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            Company[] companies = new Company[c.getCount()];
            int i = 0;
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                companies[i] = new Company(c.getString(uidClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
                i++;
            } while (c.moveToNext());

            ArrayAdapter<Company> adapterCompany = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, companies);
            adapterCompany.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinnerCompanies = root.findViewById(R.id.order_spinner_companies);
            spinnerCompanies.setAdapter(adapterCompany);
        }
        // endregion

        // region Partners
        c = db.query(DbHelper.TABLE_PARTNERS, null, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            Partner[] partners = new Partner[c.getCount()];
            int i = 0;
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                partners[i] = new Partner(c.getString(uidClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
                i++;
            } while (c.moveToNext());

            ArrayAdapter<Partner> adapterPartners = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, partners);
            adapterPartners.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinnerPartners = root.findViewById(R.id.order_spinner_partners);
            spinnerPartners.setAdapter(adapterPartners);
        }
        // endregion

        // region Warehouses
        c = db.query(DbHelper.TABLE_WAREHOUSES, null, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            Warehouse[] warehouses = new Warehouse[c.getCount()];
            int i = 0;
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                warehouses[i] = new Warehouse(c.getString(uidClIndex), c.getString(nameClIndex));
                i++;
            } while (c.moveToNext());

            ArrayAdapter<Warehouse> adapterWarehouses = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, warehouses);
            adapterWarehouses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinnerWarehouses = root.findViewById(R.id.order_spinner_warehouses);
            spinnerWarehouses.setAdapter(adapterWarehouses);
        }
        // endregion

        c.close();
        dbHelper.close();
    }

}
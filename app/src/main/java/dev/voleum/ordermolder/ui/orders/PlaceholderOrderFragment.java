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

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.GoodsOrderRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.fragments.SelectDateFragment;
import dev.voleum.ordermolder.fragments.SelectTimeFragment;
import dev.voleum.ordermolder.objects.Company;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.objects.Partner;
import dev.voleum.ordermolder.objects.Warehouse;
import dev.voleum.ordermolder.viewmodels.PageViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderOrderFragment extends Fragment {

    private static final int GOOD_CHOOSE_REQUEST = 0;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private RecyclerView recyclerGoods;
    private HashMap<Integer, HashMap<String, Object>> goods;

    private Company[] companies;
    private Partner[] partners;
    private Warehouse[] warehouses;

    private GoodsOrderRecyclerViewAdapter adapter;
    private ArrayAdapter<Company> adapterCompany;
    private ArrayAdapter<Partner> adapterPartners;
    private ArrayAdapter<Warehouse> adapterWarehouses;

    private HashMap<String, Integer> hashCompanies;
    private HashMap<String, Integer> hashPartners;
    private HashMap<String, Integer> hashWarehouses;

    private Spinner spinnerCompanies;
    private Spinner spinnerPartners;
    private Spinner spinnerWarehouses;

    public static PlaceholderOrderFragment newInstance(int index) {
        PlaceholderOrderFragment fragment = new PlaceholderOrderFragment();
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
        Order orderObj = null;
        try {
            orderObj = ((OrderActivity) getActivity()).getOrderObj();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        switch (index) {
            case 1:
                root = inflater.inflate(R.layout.fragment_order_main, container, false);
                TextView tvDate = root.findViewById(R.id.tv_date);
                TextView tvTime = root.findViewById(R.id.tv_time);
                TextView tvSum = root.findViewById(R.id.tv_sum);
                spinnerPartners = root.findViewById(R.id.spinner_partners);
                spinnerCompanies = root.findViewById(R.id.spinner_companies);
                spinnerWarehouses = root.findViewById(R.id.spinner_warehouses);
                initData(root);
                tvDate.setOnClickListener(v -> {
                    DialogFragment datePickerFragment = new SelectDateFragment(tvDate.getText().toString().substring(0, 10));
                    datePickerFragment.setTargetFragment(this, 0);
                    datePickerFragment.show(getParentFragmentManager(), "DatePicker");
                });
                tvTime.setOnClickListener(v -> {
                    DialogFragment timePickerFragment = new SelectTimeFragment(tvTime.getText().toString().substring(0, 5));
                    timePickerFragment.setTargetFragment(this, 0);
                    timePickerFragment.show(getParentFragmentManager(), "TimePicker");
                });
                if (orderObj == null) {
                    tvDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance()));
                    tvTime.setText(new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance()));
                    tvSum.setText("0.0");
                } else {
                    tvDate.setText(orderObj.getDate().substring(0, 10).replace("-", "."));
                    tvTime.setText(orderObj.getDate().substring(11, 19));
                    tvSum.setText(String.valueOf(orderObj.getSum()));
                    try {
                        spinnerCompanies.setSelection(hashCompanies.get(orderObj.getCompanyUid()));
                        spinnerPartners.setSelection(hashPartners.get(orderObj.getPartnerUid()));
                        spinnerWarehouses.setSelection(hashWarehouses.get(orderObj.getWarehouseUid()));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                root = inflater.inflate(R.layout.fragment_tabdoc_list, container, false);
                goods = new HashMap<>();
                if (orderObj != null) {
                    fillGoodList(orderObj.getUid());
                }
                recyclerGoods = root.findViewById(R.id.recycler_tabdoc);
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

//        pageViewModel.getText().observe(this, s -> {
//                textView.setText(s);
//        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GOOD_CHOOSE_REQUEST) {
            if (resultCode == OrderActivity.RESULT_OK) {
                if (data != null) {
                    // TODO: if the good already in list - increase the quantity
                    Good chosenGood = (Good) data.getSerializableExtra("good");
                    double quantity = data.getDoubleExtra("quantity", 1.0);
                    double price = data.getDoubleExtra("price", 1.0);
                    int position = goods.size();
                    HashMap<String, Object> values = new HashMap<>();
                    values.put("good", chosenGood);
                    values.put("quantity", quantity);
                    values.put("price", price);
                    values.put("sum", quantity * price);
                    goods.put(position, values);
                    try {
                        ((TextView) getActivity().findViewById(R.id.tv_sum)).setText(String.valueOf(getSum()));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyItemInserted(position + 1);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public double getSum() {
        return adapter.getSum();
    }

    private void initData(View root) {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c;

        // region Companies
        c = db.query(DbHelper.TABLE_COMPANIES, null, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            companies = new Company[c.getCount()];
            hashCompanies = new HashMap<>();
            int i = 0;
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                companies[i] = new Company(c.getString(uidClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
                hashCompanies.put(companies[i].getUid(), i);
                i++;
            } while (c.moveToNext());

            adapterCompany = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, companies);
            adapterCompany.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCompanies.setAdapter(adapterCompany);
        }
        // endregion

        // region Partners
        c = db.query(DbHelper.TABLE_PARTNERS, null, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            partners = new Partner[c.getCount()];
            hashPartners = new HashMap<>();
            int i = 0;
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int tinClIndex = c.getColumnIndex(DbHelper.COLUMN_TIN);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                partners[i] = new Partner(c.getString(uidClIndex), c.getString(nameClIndex), c.getString(tinClIndex));
                hashPartners.put(partners[i].getUid(), i);
                i++;
            } while (c.moveToNext());

            adapterPartners = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, partners);
            adapterPartners.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPartners.setAdapter(adapterPartners);
        }
        // endregion

        // region Warehouses
        c = db.query(DbHelper.TABLE_WAREHOUSES, null, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            warehouses = new Warehouse[c.getCount()];
            hashWarehouses = new HashMap<>();
            int i = 0;
            int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
            int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
            do {
                warehouses[i] = new Warehouse(c.getString(uidClIndex), c.getString(nameClIndex));
                hashWarehouses.put(warehouses[i].getUid(), i);
                i++;
            } while (c.moveToNext());

            adapterWarehouses = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, warehouses);
            adapterWarehouses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerWarehouses.setAdapter(adapterWarehouses);
        }
        // endregion

        c.close();
        dbHelper.close();
    }

    private void fillGoodList(String uid) {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] selectionArgs = { uid };
        String sql = "SELECT *"
                + " FROM " + DbHelper.TABLE_GOODS_TABLE
                + " LEFT JOIN " + DbHelper.TABLE_GOODS
                + " ON " + DbHelper.COLUMN_GOOD_UID + " = " + DbHelper.COLUMN_UID
                + " WHERE " + DbHelper.COLUMN_ORDER_UID + " = ?"
                + " ORDER BY " + DbHelper.COLUMN_POSITION;
        Cursor c = db.rawQuery(sql, selectionArgs);
        int positionClIndex = c.getColumnIndex(DbHelper.COLUMN_POSITION);
        int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
        int groupClIndex = c.getColumnIndex(DbHelper.COLUMN_GROUP_UID);
        int nameClIndex = c.getColumnIndex(DbHelper.COLUMN_NAME);
        int unitClIndex = c.getColumnIndex(DbHelper.COLUMN_UNIT_UID);
        int quantityClIndex = c.getColumnIndex(DbHelper.COLUMN_QUANTITY);
        int priceClIndex = c.getColumnIndex(DbHelper.COLUMN_PRICE);
        int sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
        if (c.moveToFirst()) {
            HashMap<String, Object> goodUidHash;
           do {
               goodUidHash = new HashMap<>();
               goodUidHash.put("good", new Good(c.getString(uidClIndex),
                       c.getString(groupClIndex),
                       c.getString(nameClIndex),
                       c.getString(unitClIndex)));
               goodUidHash.put("quantity", c.getDouble(quantityClIndex));
               goodUidHash.put("price", c.getDouble(priceClIndex));
               goodUidHash.put("sum", c.getDouble(sumClIndex));
               goods.put(c.getInt(positionClIndex), goodUidHash);
           } while (c.moveToNext());
        }
        c.close();
        db.close();
    }
}
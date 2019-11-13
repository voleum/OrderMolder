package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Objects;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.databinding.FragmentDocSecondaryPageBinding;
import dev.voleum.ordermolder.databinding.FragmentOrderMainBinding;
import dev.voleum.ordermolder.fragments.SelectDateFragment;
import dev.voleum.ordermolder.fragments.SelectTimeFragment;
import dev.voleum.ordermolder.objects.Company;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Partner;
import dev.voleum.ordermolder.objects.Warehouse;
import dev.voleum.ordermolder.viewmodels.OrderViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderOrderFragment extends Fragment {

    private static final int GOOD_CHOOSE_REQUEST = 0;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OrderViewModel orderViewModel;

    private RecyclerView recyclerGoods;

    private Company[] companies;
    private Partner[] partners;
    private Warehouse[] warehouses;

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
                orderViewModel = ((OrderActivity) getActivity()).getOrderViewModel();
                FragmentOrderMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_main, null, false);
                binding.setViewModel(orderViewModel);
                root = binding.getRoot();
                TextView tvDate = root.findViewById(R.id.tv_date);
                TextView tvTime = root.findViewById(R.id.tv_time);
                spinnerPartners = root.findViewById(R.id.spinner_partners);
                spinnerCompanies = root.findViewById(R.id.spinner_companies);
                spinnerWarehouses = root.findViewById(R.id.spinner_warehouses);
                initData(root);
                tvDate.setOnClickListener(v -> {
                    DialogFragment datePickerFragment = new SelectDateFragment(orderViewModel.getDate());
                    datePickerFragment.setTargetFragment(this, 0);
                    datePickerFragment.show(getParentFragmentManager(), "DatePicker");
                });
                tvTime.setOnClickListener(v -> {
                    DialogFragment timePickerFragment = new SelectTimeFragment(orderViewModel.getTime());
                    timePickerFragment.setTargetFragment(this, 0);
                    timePickerFragment.show(getParentFragmentManager(), "TimePicker");
                });
                break;
            case 2:
                orderViewModel = ((OrderActivity) getActivity()).getOrderViewModel();
                FragmentDocSecondaryPageBinding bindingRecycler = DataBindingUtil.inflate(inflater, R.layout.fragment_doc_secondary_page, null, false);
                bindingRecycler.setViewModel(orderViewModel);
                root = bindingRecycler.getRoot();
                recyclerGoods = root.findViewById(R.id.recycler_tabdoc);
                recyclerGoods.setHasFixedSize(true);
                recyclerGoods.setLayoutManager(new LinearLayoutManager(getContext()));

//                orderViewModel.getAdapter().setOnEntryClickListener((v, position) -> {
//                    switch (v.getId()) {
//                        case R.id.good_plus:
//                            orderViewModel.increaseQuantityInRow(position);
//                            break;
//                        case R.id.good_minus:
//                            orderViewModel.decreaseQuantityInRow(position);
//                            break;
//                    }
//                    orderViewModel.countSum();
//                });

                FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
                fab.setOnClickListener(
                        (view) -> startActivityForResult(new Intent(getActivity(), GoodsChooser.class), GOOD_CHOOSE_REQUEST)
                );
                break;
        }
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GOOD_CHOOSE_REQUEST) {
            if (resultCode == OrderActivity.RESULT_OK) {
                if (data != null) {
                    orderViewModel.onAddGood((Good) data.getSerializableExtra("good"),
                            data.getDoubleExtra("quantity", 1.0),
                            data.getDoubleExtra("price", 1.0));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
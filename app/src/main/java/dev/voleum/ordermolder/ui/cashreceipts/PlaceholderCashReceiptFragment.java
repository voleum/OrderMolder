package dev.voleum.ordermolder.ui.cashreceipts;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
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
import dev.voleum.ordermolder.adapters.ObjectsCashReceiptRecyclerViewAdapter;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.fragments.SelectDateFragment;
import dev.voleum.ordermolder.fragments.SelectTimeFragment;
import dev.voleum.ordermolder.objects.CashReceipt;
import dev.voleum.ordermolder.objects.Company;
import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.objects.Partner;
import dev.voleum.ordermolder.ui.general.PageViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderCashReceiptFragment extends Fragment {

    private static final int OBJECT_CHOOSE_REQUEST = 0;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private RecyclerView recyclerObjects;
    private HashMap<Integer, HashMap<String, Object>> objects;

    private Company[] companies;
    private Partner[] partners;

    private ObjectsCashReceiptRecyclerViewAdapter adapter;
    private ArrayAdapter<Company> adapterCompany;
    private ArrayAdapter<Partner> adapterPartners;

    private HashMap<String, Integer> hashCompanies;
    private HashMap<String, Integer> hashPartners;

    private Spinner spinnerCompanies;
    private Spinner spinnerPartners;

    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    public static PlaceholderCashReceiptFragment newInstance(int index) {
        PlaceholderCashReceiptFragment fragment = new PlaceholderCashReceiptFragment();
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
        CashReceipt cashReceiptObj = null;
        try {
            cashReceiptObj = ((CashReceiptActivity) getActivity()).getCashReceiptObj();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        switch (index) {
            case 1:
                onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (parent.getId()) {
                            case R.id.spinner_companies:
                                try {
                                    ((CashReceiptActivity) getActivity()).setCompanyUid(companies[position].getUid());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case R.id.spinner_partners:
                                try {
                                    ((CashReceiptActivity) getActivity()).setPartnerUid(partners[position].getUid());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };

                root = inflater.inflate(R.layout.fragment_cash_receipt_main, container, false);
                @SuppressLint("CutPasteId") TextView tvDate = root.findViewById(R.id.tv_date);
                @SuppressLint("CutPasteId") TextView tvTime = root.findViewById(R.id.tv_time);
                TextView tvSum = root.findViewById(R.id.tv_sum);
                spinnerPartners = root.findViewById(R.id.spinner_partners);
                spinnerCompanies = root.findViewById(R.id.spinner_companies);
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
                if (cashReceiptObj == null) {
                    tvDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance()));
                    tvTime.setText(new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance()));
                    tvSum.setText("0.0");
                } else {
                    tvDate.setText(cashReceiptObj.getDate().substring(0, 10).replace("-", "."));
                    tvTime.setText(cashReceiptObj.getDate().substring(11, 19));
                    tvSum.setText(String.valueOf(cashReceiptObj.getSum()));
                    try {
                        spinnerCompanies.setSelection(hashCompanies.get(cashReceiptObj.getCompanyUid()));
                        spinnerPartners.setSelection(hashPartners.get(cashReceiptObj.getPartnerUid()));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                spinnerPartners.setOnItemSelectedListener(onItemSelectedListener);
                spinnerCompanies.setOnItemSelectedListener(onItemSelectedListener);
                break;
            case 2:
                root = inflater.inflate(R.layout.fragment_tabdoc_list, container, false);
                objects = new HashMap<>();
                if (cashReceiptObj != null) {
                    fillObjectList(cashReceiptObj.getUid());
                }
                recyclerObjects = root.findViewById(R.id.recycler_tabdoc);
                recyclerObjects.setHasFixedSize(true);
                recyclerObjects.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new ObjectsCashReceiptRecyclerViewAdapter(objects);
                recyclerObjects.setAdapter(adapter);

                FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
                fab.setOnClickListener((view) -> {
                    Intent intentOut = new Intent(getActivity(), ObjectsChooser.class);
                    intentOut.putExtra(DbHelper.COLUMN_COMPANY_UID, ((CashReceiptActivity) getActivity()).getCompanyUid());
                    intentOut.putExtra(DbHelper.COLUMN_PARTNER_UID, ((CashReceiptActivity) getActivity()).getPartnerUid());
                    startActivityForResult(intentOut, OBJECT_CHOOSE_REQUEST);
                        }
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
        if (requestCode == OBJECT_CHOOSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // TODO: if the object already in list - ignore
                    Order chosenOrder = (Order) data.getSerializableExtra("object");
                    double sum = data.getDoubleExtra("sum_credit", 1.0);
                    int position = objects.size();
                    HashMap<String, Object> values = new HashMap<>();
                    values.put("object", chosenOrder);
                    values.put("sum_credit", sum);
                    objects.put(position, values);
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
        DbHelper dbHelper = DbHelper.getInstance(getContext());
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

        c.close();
        dbHelper.close();
    }

    private void fillObjectList(String uid) {
        // TODO: AsyncTask
        DbHelper dbHelper = DbHelper.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] selectionArgs = { uid };
        String sql = "SELECT *"
                + " FROM " + DbHelper.TABLE_OBJECTS_TABLE
                + " LEFT JOIN " + DbHelper.TABLE_ORDERS
                + " ON " + DbHelper.COLUMN_ORDER_UID + " = " + DbHelper.COLUMN_UID
                + " WHERE " + DbHelper.COLUMN_CASH_RECEIPT_UID + " = ?"
                + " ORDER BY " + DbHelper.COLUMN_POSITION;
        Cursor c = db.rawQuery(sql, selectionArgs);
        int positionClIndex = c.getColumnIndex(DbHelper.COLUMN_POSITION);
        int uidClIndex = c.getColumnIndex(DbHelper.COLUMN_UID);
        int dateClIndex = c.getColumnIndex(DbHelper.COLUMN_DATE);
        int companyClIndex = c.getColumnIndex(DbHelper.COLUMN_COMPANY_UID);
        int partnerClIndex = c.getColumnIndex(DbHelper.COLUMN_PARTNER_UID);
        int warehouseClIndex = c.getColumnIndex(DbHelper.COLUMN_WAREHOUSE_UID);
        int sumCreditClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM_CREDIT);
        int sumClIndex = c.getColumnIndex(DbHelper.COLUMN_SUM);
        if (c.moveToFirst()) {
            HashMap<String, Object> objectUidHash;
           do {
               objectUidHash = new HashMap<>();
               objectUidHash.put("object", new Order(c.getString(uidClIndex),
                       c.getString(dateClIndex),
                       c.getString(companyClIndex),
                       c.getString(partnerClIndex),
                       c.getString(warehouseClIndex),
                       c.getDouble(sumCreditClIndex)));
               objectUidHash.put("sum_credit", c.getDouble(sumClIndex));
               objects.put(c.getInt(positionClIndex), objectUidHash);
           } while (c.moveToNext());
        }
        c.close();
        db.close();
    }
}
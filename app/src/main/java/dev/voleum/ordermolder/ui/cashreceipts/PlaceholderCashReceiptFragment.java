package dev.voleum.ordermolder.ui.cashreceipts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.callbacks.TableItemTouchHelperCallback;
import dev.voleum.ordermolder.databinding.FragmentCashReceiptMainBinding;
import dev.voleum.ordermolder.databinding.FragmentCashReceiptSecondaryPageBinding;
import dev.voleum.ordermolder.fragments.SelectDateFragment;
import dev.voleum.ordermolder.fragments.SelectTimeFragment;
import dev.voleum.ordermolder.models.Order;
import dev.voleum.ordermolder.viewmodels.CashReceiptViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderCashReceiptFragment extends Fragment {

    private static final int OBJECT_CHOOSE_REQUEST = 0;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private CashReceiptViewModel cashReceiptViewModel;

    private RecyclerView recyclerObjects;

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
                cashReceiptViewModel = ((CashReceiptActivity) getActivity()).getDocViewModel();
                FragmentCashReceiptMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_receipt_main, null, false);
                binding.setViewModel(cashReceiptViewModel);
                root = binding.getRoot();
                TextView tvDate = root.findViewById(R.id.tv_date);
                TextView tvTime = root.findViewById(R.id.tv_time);
                tvDate.setOnClickListener(v -> {
                    DialogFragment datePickerFragment = new SelectDateFragment(cashReceiptViewModel.getDate());
                    datePickerFragment.setTargetFragment(this, 0);
                    datePickerFragment.show(getParentFragmentManager(), "DatePicker");
                });
                tvTime.setOnClickListener(v -> {
                    DialogFragment timePickerFragment = new SelectTimeFragment(cashReceiptViewModel.getTime());
                    timePickerFragment.setTargetFragment(this, 0);
                    timePickerFragment.show(getParentFragmentManager(), "TimePicker");
                });
                break;
            case 2:
                cashReceiptViewModel = ((CashReceiptActivity) getActivity()).getDocViewModel();
                FragmentCashReceiptSecondaryPageBinding bindingRecycler
                        = DataBindingUtil.inflate(inflater,
                            R.layout.fragment_cash_receipt_secondary_page,
                            null,
                            false);
                bindingRecycler.setViewModel(cashReceiptViewModel);
                root = bindingRecycler.getRoot();

                recyclerObjects = root.findViewById(R.id.recycler_tab_cash_receipt);
                recyclerObjects.setHasFixedSize(true);
                recyclerObjects.setLayoutManager(new LinearLayoutManager(getContext()));

                ItemTouchHelper.Callback callback = new TableItemTouchHelperCallback(cashReceiptViewModel);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(recyclerObjects);

                FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
                fab.setOnClickListener(view -> {
                            Intent intentOut = new Intent(getActivity(), ObjectsChooserActivity.class);
                            intentOut.putExtra(ObjectsChooserActivity.COMPANY_UID, (cashReceiptViewModel.getDocument().getCompanyUid()));
                            intentOut.putExtra(ObjectsChooserActivity.PARTNER_UID, (cashReceiptViewModel.getDocument().getPartnerUid()));
                            startActivityForResult(intentOut, OBJECT_CHOOSE_REQUEST);
                        }
                );
                break;
        }
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OBJECT_CHOOSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                        Order order = (Order) data.getSerializableExtra(ObjectsChooserActivity.ORDER);
                        if (order != null) cashReceiptViewModel.addRow(order);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

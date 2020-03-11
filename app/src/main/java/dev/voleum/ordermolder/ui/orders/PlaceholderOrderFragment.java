package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.FragmentOrderMainBinding;
import dev.voleum.ordermolder.databinding.FragmentOrderSecondaryPageBinding;
import dev.voleum.ordermolder.fragments.SelectDateFragment;
import dev.voleum.ordermolder.fragments.SelectTimeFragment;
import dev.voleum.ordermolder.models.Good;
import dev.voleum.ordermolder.viewmodels.OrderViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderOrderFragment extends Fragment {

    private static final int GOOD_CHOOSE_REQUEST = 0;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private OrderViewModel orderViewModel;

    private RecyclerView recyclerGoods;

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
                FragmentOrderSecondaryPageBinding bindingRecycler
                        = DataBindingUtil.inflate(inflater,
                            R.layout.fragment_order_secondary_page,
                            null,
                            false);
                bindingRecycler.setViewModel(orderViewModel);
                root = bindingRecycler.getRoot();
                recyclerGoods = root.findViewById(R.id.recycler_tab_order);
                recyclerGoods.setHasFixedSize(true);
                recyclerGoods.setLayoutManager(new LinearLayoutManager(getContext()));

                bindingRecycler.getViewModel().getAdapter().setOnEntryLongClickListener((v, position) -> {
                    ((OrderActivity) getActivity()).setRecyclerPosition(position);
                    v.showContextMenu();
                });

                registerForContextMenu(recyclerGoods);

                FloatingActionButton fab = Objects.requireNonNull(getActivity()).findViewById(R.id.fab);
                fab.setOnClickListener(view -> {
                            startActivityForResult(new Intent(getActivity(), GoodsChooserActivity.class), GOOD_CHOOSE_REQUEST);
                        }
                );
                break;
        }
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GOOD_CHOOSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Good good = (Good) data.getSerializableExtra(GoodsChooserActivity.GOOD);
                    if (good != null) {
                        orderViewModel.addGood(good,
                                data.getDoubleExtra(GoodsChooserActivity.QUANTITY, 1.0),
                                data.getDoubleExtra(GoodsChooserActivity.PRICE, 0.0));
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            OrderActivity orderActivity = (OrderActivity) getActivity();
            if (orderActivity != null) {
                orderViewModel.removeGood(orderActivity.getRecyclerPosition());
                return true;
            }
        }
        return false;
    }
}

package dev.voleum.ordermolder.ui.orders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.ActivityDocBinding;
import dev.voleum.ordermolder.ui.general.DocListActivity;
import dev.voleum.ordermolder.ui.general.SectionsPagerAdapter;
import dev.voleum.ordermolder.viewmodels.OrderViewModel;

public class OrderActivity extends AppCompatActivity {

    private OrderViewModel orderViewModel;

    protected ViewPager viewPager;
    protected FloatingActionButton fab;
    protected SectionsPagerAdapter sectionsPagerAdapter;

    private boolean isCreating;
    private boolean savedWithoutClosing;

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 1) {
                fab.show();
            }
            else {
                fab.hide();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        isCreating = (getIntent().getBooleanExtra(DocListActivity.IS_CREATING, true));
        savedWithoutClosing = false;

        orderViewModel = new OrderViewModel(getIntent().getStringExtra(DocListActivity.DOC));
        ActivityDocBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_doc);
        binding.setViewModel(orderViewModel);
        binding.executePendingBindings();

        sectionsPagerAdapter = new SectionsPagerAdapter(
                this,
                getSupportFragmentManager(),
                SectionsPagerAdapter.TYPE_ORDER);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (orderViewModel.saveOrder()) {
                        Intent intent = new Intent();
                        intent.putExtra("doc", orderViewModel.getOrder());
                        int result = isCreating ? DocListActivity.RESULT_CREATED : DocListActivity.RESULT_SAVED;
                        setResult(result, intent);
                        finish();
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    if (savedWithoutClosing) {
                        Intent intent = new Intent();
                        intent.putExtra("doc", orderViewModel.getOrder());
                        setResult(isCreating ? DocListActivity.RESULT_CREATED : DocListActivity.RESULT_SAVED, intent);
                    }
                    finish();
                    break;
                default:
                    dialog.cancel();
            }
        };
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_save_doc)
                .setPositiveButton(R.string.dialog_yes, dialogClickListener)
                .setNegativeButton(R.string.dialog_no, dialogClickListener)
                .setNeutralButton(R.string.dialog_cancel, dialogClickListener)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        View focusedView = getCurrentFocus();
        if (focusedView != null) focusedView.clearFocus();
        try {
            InputMethodManager imm = (InputMethodManager) viewPager.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.doc_save:
                if (orderViewModel.saveOrder()) savedWithoutClosing = true;
                break;
            default:
                break;
        }
        return true;
    }

    public OrderViewModel getOrderViewModel() {
        return orderViewModel;
    }
}

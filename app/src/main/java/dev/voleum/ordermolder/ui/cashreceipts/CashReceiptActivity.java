package dev.voleum.ordermolder.ui.cashreceipts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.databinding.ActivityCashReceiptBinding;
import dev.voleum.ordermolder.ui.general.DocListActivity;
import dev.voleum.ordermolder.ui.general.SectionsPagerAdapter;
import dev.voleum.ordermolder.viewmodels.CashReceiptViewModel;

public class CashReceiptActivity extends AppCompatActivity {

    private CashReceiptViewModel cashReceiptViewModel;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_receipt);

        isCreating = (getIntent().getBooleanExtra(DocListActivity.IS_CREATING, true));
        savedWithoutClosing = false;

        if (isCreating) cashReceiptViewModel = new CashReceiptViewModel();
        else cashReceiptViewModel = new CashReceiptViewModel(getIntent().getStringExtra(DocListActivity.DOC));
        ActivityCashReceiptBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_cash_receipt);
        binding.setViewModel(cashReceiptViewModel);
        binding.executePendingBindings();

        sectionsPagerAdapter = new SectionsPagerAdapter(
                this,
                getSupportFragmentManager(),
                SectionsPagerAdapter.TYPE_CASH_RECEIPT);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (cashReceiptViewModel.saveCashReceipt()) {
                        Intent intent = new Intent();
                        intent.putExtra(DocListActivity.DOC, cashReceiptViewModel.getCashReceipt());
                        int result = isCreating ? DocListActivity.RESULT_CREATED : DocListActivity.RESULT_SAVED;
                        setResult(result, intent);
                        finish();
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    if (savedWithoutClosing) {
                        Intent intent = new Intent();
                        intent.putExtra(DocListActivity.DOC, cashReceiptViewModel.getCashReceipt());
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.doc_save:
                if (cashReceiptViewModel.getTableObjects().isEmpty()) {
                    Snackbar.make(fab, R.string.snackbar_empty_goods_list, Snackbar.LENGTH_SHORT).show();
                }
                // TODO: Async
                if (cashReceiptViewModel.saveCashReceipt()) {
                    savedWithoutClosing = true;
                    Snackbar.make(fab, R.string.snackbar_doc_saved, Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public CashReceiptViewModel getCashReceiptViewModel() {
        return cashReceiptViewModel;
    }
}
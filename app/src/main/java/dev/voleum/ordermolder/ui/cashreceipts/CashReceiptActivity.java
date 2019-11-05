package dev.voleum.ordermolder.ui.cashreceipts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.database.DbAsyncSaveCashReceipt;
import dev.voleum.ordermolder.objects.CashReceipt;
import dev.voleum.ordermolder.ui.general.DocListActivity;
import dev.voleum.ordermolder.ui.general.SectionsPagerAdapter;

public class CashReceiptActivity extends AppCompatActivity {

    protected ViewPager viewPager;
    protected FloatingActionButton fab;
    protected SectionsPagerAdapter sectionsPagerAdapter;

    private CashReceipt cashReceiptObj;
    private String companyUid;
    private String partnerUid;

    private boolean isCreating;
    private boolean createdWithoutClosing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                    View focusedView = getCurrentFocus();
                    if (focusedView != null) focusedView.clearFocus();
                    double sum = sectionsPagerAdapter.getSum();
                    ((TextView) findViewById(R.id.cash_receipt_tv_sum)).setText(String.valueOf(sum));
                    try {
                        InputMethodManager imm = (InputMethodManager) viewPager.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getIntent().getBooleanExtra(DocListActivity.IS_CREATING, true)) {
            isCreating = true;
            setTitle(R.string.title_new_cash_receipt);
        } else {
            isCreating = false;
            cashReceiptObj = (CashReceipt) getIntent().getSerializableExtra(DocListActivity.DOC);
            try {
                String title = cashReceiptObj.getDate().substring(0, 19).replace("-", ".");
                setTitle(title);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        createdWithoutClosing = false;
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (saveDoc()) {
                        Intent intent = new Intent();
                        intent.putExtra(DocListActivity.DOC, cashReceiptObj);
                        int result = isCreating ? DocListActivity.RESULT_CREATED : DocListActivity.RESULT_SAVED;
                        setResult(result, intent);
                        finish();
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    if (createdWithoutClosing) {
                        Intent intent = new Intent();
                        intent.putExtra("doc", cashReceiptObj);
                        setResult(DocListActivity.RESULT_CREATED, intent);
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
                if (isCreating) createdWithoutClosing = true;
                saveDoc();
                break;
            default:
                break;
        }
        return true;
    }

    protected CashReceipt getCashReceiptObj() {
        return cashReceiptObj;
    }

    public String getCompanyUid() {
        return companyUid;
    }

    public void setCompanyUid(String companyUid) {
        this.companyUid = companyUid;
    }

    public String getPartnerUid() {
        return partnerUid;
    }

    public void setPartnerUid(String partnerUid) {
        this.partnerUid = partnerUid;
    }

    private boolean saveDoc() {
        HashMap<Integer, HashMap<String, Object>> objectsInfo = null;
        try {
            objectsInfo = sectionsPagerAdapter.getObjectsInfo();
            if (objectsInfo.isEmpty()) {
                Snackbar.make(findViewById(R.id.view_pager), R.string.snackbar_empty_objects_list, Snackbar.LENGTH_SHORT)
                        .setGestureInsetBottomIgnored(true)
                        .show();
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> mainInfo = sectionsPagerAdapter.getCashReceiptMainInfo();

        if (cashReceiptObj == null) {
            cashReceiptObj = new CashReceipt();
            cashReceiptObj.setUid(UUID.randomUUID().toString());
        }
        try {
            cashReceiptObj.setDate((String) mainInfo.get("date"));
            cashReceiptObj.setCompanyUid((String) mainInfo.get("company_uid"));
            cashReceiptObj.setPartnerUid((String) mainInfo.get("partner_uid"));
            cashReceiptObj.setSum((Double) mainInfo.get("sum"));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mainInfo.put("uid", cashReceiptObj.getUid());

        HashMap<String, Map> docInfo = new HashMap<>();
        docInfo.put("main_info", mainInfo);
        docInfo.put("objects_info", objectsInfo);
        DbAsyncSaveCashReceipt dbAsyncSaveCashReceipt = new DbAsyncSaveCashReceipt(this);
        dbAsyncSaveCashReceipt.execute(docInfo);

        return true;
    }
}
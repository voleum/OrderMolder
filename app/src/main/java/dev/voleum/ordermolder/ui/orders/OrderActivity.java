package dev.voleum.ordermolder.ui.orders;

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

import dev.voleum.ordermolder.Database.DbAsyncSaveOrder;
import dev.voleum.ordermolder.Object.Order;
import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.ui.general.DocListActivity;
import dev.voleum.ordermolder.ui.general.SectionsPagerAdapter;

public class OrderActivity extends AppCompatActivity {

    protected ViewPager viewPager;
    protected FloatingActionButton fab;
    protected SectionsPagerAdapter sectionsPagerAdapter;

    private Order orderObj;

    private boolean isCreating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        sectionsPagerAdapter = new SectionsPagerAdapter(
                this,
                getSupportFragmentManager(),
                SectionsPagerAdapter.TYPE_ORDER);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        fab = findViewById(R.id.fab);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            fab.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
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
                    ((TextView) findViewById(R.id.order_tv_sum)).setText(String.valueOf(sum));
                    InputMethodManager imm = (InputMethodManager) viewPager.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getIntent().getBooleanExtra(DocListActivity.IS_CREATING, true)) {
            isCreating = true;
            setTitle(R.string.title_new_order);
        } else {
            isCreating = false;
            orderObj = (Order) getIntent().getSerializableExtra("doc");
            String title = orderObj.getDate().substring(0, 19).replace("-", ".");
            setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (saveDoc()) {
                        Intent intent = new Intent();
                        intent.putExtra("doc", orderObj);
                        int result = isCreating ? DocListActivity.RESULT_CREATED : DocListActivity.RESULT_SAVED;
                        setResult(result, intent);
                        finish();
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
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
        InputMethodManager imm = (InputMethodManager) viewPager.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.doc_save:
                saveDoc();
                break;
            default:
                break;
        }
        return true;
    }

    protected Order getOrderObj() {
        return orderObj;
    }

    private boolean saveDoc() {
        HashMap<Integer, HashMap<String, Object>> goodsInfo = sectionsPagerAdapter.getGoodsInfo();
        if (goodsInfo.isEmpty()) {
            Snackbar.make(findViewById(R.id.view_pager), R.string.snackbar_empty_goods_list, Snackbar.LENGTH_SHORT)
                    .setGestureInsetBottomIgnored(true)
                    .show();
            return false;
        }
        HashMap<String, Object> mainInfo = sectionsPagerAdapter.getOrderMainInfo();

        if (orderObj == null) {
            orderObj = new Order();
            orderObj.setUid(UUID.randomUUID().toString());
        }
        orderObj.setDate((String) mainInfo.get("date"));
        orderObj.setCompanyUid((String) mainInfo.get("company_uid"));
        orderObj.setPartnerUid((String) mainInfo.get("partner_uid"));
        orderObj.setWarehouseUid((String) mainInfo.get("warehouse_uid"));
        orderObj.setSum((Double) mainInfo.get("sum"));

        mainInfo.put("uid", orderObj.getUid());

        HashMap<String, Map> orderInfo = new HashMap<>();
        orderInfo.put("main_info", mainInfo);
        orderInfo.put("goods_info", goodsInfo);
        DbAsyncSaveOrder dbAsyncSaveOrder = new DbAsyncSaveOrder(this);
        dbAsyncSaveOrder.execute(orderInfo);

        return true;
    }
}
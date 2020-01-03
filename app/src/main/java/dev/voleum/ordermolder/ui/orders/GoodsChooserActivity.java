package dev.voleum.ordermolder.ui.orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.GoodsChooserRecyclerViewAdapter;
import dev.voleum.ordermolder.databinding.ActivityGoodsChooserBinding;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.viewmodels.GoodsChooserViewModel;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoodsChooserActivity extends AppCompatActivity {

    public static final String GOOD = "good";
    public static final String QUANTITY = "quantity";
    public static final String PRICE = "price";
    public static final String SUM = "sum";

    private ActivityGoodsChooserBinding binding;

    private GoodsChooserViewModel goodsChooserViewModel;

    private RecyclerView recyclerGoods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_chooser);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Completable initData() {
        return Completable.create(subscriber -> {
            GoodsChooserRecyclerViewAdapter.OnEntryClickListener onEntryClickListener = ((v, row) -> {
                double price = (double) row.get(PRICE);
                setResult(RESULT_OK, new Intent()
                        .putExtra(GOOD, (Good) row.get(GOOD))
                        .putExtra(QUANTITY, 1.0)
                        .putExtra(PRICE, price)
                        .putExtra(SUM, price));
                finish();
            });

            goodsChooserViewModel = new GoodsChooserViewModel(onEntryClickListener);

            binding.setViewModel(goodsChooserViewModel);
            binding.executePendingBindings();

            recyclerGoods = binding.getRoot().findViewById(R.id.recycler_goods_chooser);
            recyclerGoods.setHasFixedSize(true);
            recyclerGoods.setLayoutManager(new LinearLayoutManager(this));

            setTitle(R.string.title_activity_goods);
        });
    }
}

package dev.voleum.ordermolder.ui.cashreceipts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.database.DbHelper;
import dev.voleum.ordermolder.databinding.ActivityObjectsChooserBinding;
import dev.voleum.ordermolder.objects.Order;
import dev.voleum.ordermolder.viewmodels.ObjectsChooserViewModel;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ObjectsChooserActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    public static final String SUM = "sum";

    private ActivityObjectsChooserBinding binding;

    private ObjectsChooserViewModel objectsChooserViewModel;

    private RecyclerView recyclerObjects;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_objects_chooser);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        objectsChooserViewModel = new ViewModelProvider(this).get(ObjectsChooserViewModel.class);

        if (objectsChooserViewModel.getObjects() == null) {
            initData()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            completeOnCreate();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        } else {
            completeOnCreate();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Completable initData() {
        return Completable.create(subscriber -> {
            objectsChooserViewModel.init(getIntent().getStringExtra(DbHelper.COLUMN_COMPANY_UID),
                    getIntent().getStringExtra(DbHelper.COLUMN_PARTNER_UID));
            subscriber.onComplete();
        });
    }

    private void completeOnCreate() {

        binding.setViewModel(objectsChooserViewModel);
        binding.executePendingBindings();

        recyclerObjects = binding.getRoot().findViewById(R.id.recycler_objects_chooser);
        recyclerObjects.setHasFixedSize(true);
        recyclerObjects.setLayoutManager(new LinearLayoutManager(this));

        binding.getViewModel().getAdapter().setOnEntryClickListener((v, position) -> {
            HashMap<String, Object> chosen = binding.getViewModel().getObjects().get(position);
            double sum = (double) chosen.get(ObjectsChooserActivity.SUM);
            setResult(RESULT_OK, new Intent()
                    .putExtra(OBJECT, (Order) chosen.get(OBJECT))
                    .putExtra(SUM, sum));
            finish();
        });
        setTitle(R.string.title_activity_orders);
    }
}

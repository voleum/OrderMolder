package dev.voleum.ordermolder.ui.general;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dev.voleum.ordermolder.R;
import dev.voleum.ordermolder.adapters.DocListRecyclerViewAdapter;
import dev.voleum.ordermolder.databinding.ActivityDocListBinding;
import dev.voleum.ordermolder.enums.DocumentTypes;
import dev.voleum.ordermolder.models.Document;
import dev.voleum.ordermolder.ui.cashreceipts.CashReceiptActivity;
import dev.voleum.ordermolder.ui.orders.OrderActivity;
import dev.voleum.ordermolder.viewmodels.DocListViewModel;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DocListActivity extends AppCompatActivity {

    public final static String IS_CREATING = "is_creating";
    public final static String DOC = "doc";
    public final static String DOC_ACTIVITY = "doc_activity";
    public final static String DOC_TYPE = "doc_type";
    public final static String POSITION = "position";

    public final static int REQUEST_CODE = 0;
    public final static int RESULT_SAVED = 2;
    public final static int RESULT_CREATED = 3;

    private DocListViewModel docListViewModel;

    private RecyclerView recyclerDocs;
    private ActivityDocListBinding binding;

    private DocumentTypes docType;
    private Context context = this;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        docType = (DocumentTypes) getIntent().getSerializableExtra(DOC_TYPE);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_doc_list);

        initData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                        binding.setViewModel(docListViewModel);
                        binding.executePendingBindings();

                        recyclerDocs = binding.getRoot().findViewById(R.id.recycler_docs);
                        recyclerDocs.setHasFixedSize(true);
                        recyclerDocs.setLayoutManager(new LinearLayoutManager(context));

                        DocListRecyclerViewAdapter.OnEntryClickListener onEntryClickListener = (v, position) -> {
                            Document clickedDoc = (Document) docListViewModel.getDocs().get(position);
                            Intent intentOut;
                            switch (docType) {
                                case ORDER:
                                    intentOut = new Intent(DocListActivity.this, OrderActivity.class);
                                    break;
                                case CASH_RECEIPT:
                                    intentOut = new Intent(DocListActivity.this, CashReceiptActivity.class);
                                    break;
                                default:
                                    intentOut = null;
                            }
                            intentOut.putExtra(IS_CREATING, false);
                            intentOut.putExtra(DOC, clickedDoc.getUid());
                            intentOut.putExtra(POSITION, position);
                            startActivityForResult(intentOut, REQUEST_CODE);
                        };

                        registerForContextMenu(recyclerDocs);

                        Toolbar toolbar = findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                        docListViewModel.getAdapter().setOnEntryClickListener(onEntryClickListener);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        View.OnClickListener fabClickListener = v -> {
            Intent intentOut;
            switch (docType) {
                case ORDER:
                    intentOut = new Intent(DocListActivity.this, OrderActivity.class);
                    break;
                case CASH_RECEIPT:
                    intentOut = new Intent(DocListActivity.this, CashReceiptActivity.class);
                    break;
                default:
                    intentOut = null;
            }
            intentOut.putExtra(IS_CREATING, true);
            intentOut.putExtra(POSITION, docListViewModel.getDocs().size());
            startActivityForResult(intentOut, REQUEST_CODE);
        };

        fab = findViewById(R.id.doc_list_fab);
        fab.setOnClickListener(fabClickListener);

        setTitleDependOnType();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0 && data != null) {
            Document doc = (Document) data.getSerializableExtra(DOC);
            switch (resultCode) {
                case RESULT_SAVED:
                    docListViewModel.editDoc(doc, data.getIntExtra(POSITION, -1));
                    break;
                case RESULT_CREATED:
                    docListViewModel.addDoc(doc);
                    break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            docListViewModel.removeDoc();
            return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setTitleDependOnType() {
        switch (docType) {
            case ORDER:
                setTitle(R.string.title_activity_orders);
                break;
            case CASH_RECEIPT:
                setTitle(R.string.title_activity_cash_receipts);
                break;
            default:
                setTitle(R.string.title_activity_unknown_doc);
        }
    }

    private Completable initData() {
        return Completable.create(subscriber -> {
            docListViewModel = new DocListViewModel(docType);
            subscriber.onComplete();
        });
    }
}

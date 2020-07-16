package dev.voleum.ordermolder.ui.cashreceipts

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.voleum.ordermolder.OrderMolder
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.ActivityCashReceiptBinding
import dev.voleum.ordermolder.models.CashReceipt
import dev.voleum.ordermolder.ui.general.AbstractDocActivity
import dev.voleum.ordermolder.ui.general.DocListActivity
import dev.voleum.ordermolder.ui.general.SectionsPagerAdapter
import dev.voleum.ordermolder.viewmodels.CashReceiptViewModel
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CashReceiptActivity : AbstractDocActivity<CashReceipt, CashReceiptViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        docViewModel = ViewModelProvider(this).get(CashReceiptViewModel::class.java)

        val binding: ActivityCashReceiptBinding = DataBindingUtil.setContentView(this, R.layout.activity_cash_receipt)
        binding.viewModel = docViewModel
        binding.executePendingBindings()

        fab = binding.fab
        progressLayout = binding.progressLayout
        viewPager = binding.viewPager

        setCashReceipt()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {

                    override fun onComplete() {
                        docViewModel.adapter?.notifyDataSetChanged()
                        progressLayout.visibility = View.GONE
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Log.e(OrderMolder.LOG_TAG, e.message.toString())
                    }
                })

        sectionPagerAdapter = SectionsPagerAdapter(this,
                supportFragmentManager,
                SectionsPagerAdapter.TYPE_CASH_RECEIPT)

        viewPager.adapter = sectionPagerAdapter
        binding.tabs.setupWithViewPager(viewPager)
        setupToolbar()

        viewPager.addOnPageChangeListener(onPageChangeListener)
    }

    private fun setCashReceipt(): Completable {
        return Completable.create {
            if (isCreating) docViewModel.setCashReceipt()
            else docViewModel.setCashReceipt(intent.getStringExtra(DocListActivity.DOC))
            it.onComplete()
        }
    }
}
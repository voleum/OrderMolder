package dev.voleum.ordermolder.ui.cashreceipts

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.ActivityCashReceiptBinding
import dev.voleum.ordermolder.models.CashReceipt
import dev.voleum.ordermolder.ui.general.AbstractDocActivity
import dev.voleum.ordermolder.ui.general.DocListActivity
import dev.voleum.ordermolder.ui.general.SectionsPagerAdapter
import dev.voleum.ordermolder.viewmodels.CashReceiptViewModel

class CashReceiptActivity : AbstractDocActivity<CashReceipt, CashReceiptViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        docViewModel = ViewModelProvider(this).get(CashReceiptViewModel::class.java)
        if (isCreating) docViewModel.setCashReceipt()
        else docViewModel.setCashReceipt(intent.getStringExtra(DocListActivity.DOC))

        val binding: ActivityCashReceiptBinding = DataBindingUtil.setContentView(this, R.layout.activity_cash_receipt)
        binding.viewModel = docViewModel
        binding.executePendingBindings()

        fab = binding.fab
        progressLayout = binding.progressLayout
        viewPager = binding.viewPager

        sectionPagerAdapter = SectionsPagerAdapter(this,
                supportFragmentManager,
                SectionsPagerAdapter.TYPE_CASH_RECEIPT)

        viewPager.adapter = sectionPagerAdapter
        binding.tabs.setupWithViewPager(viewPager)
        setupToolbar()

        viewPager.addOnPageChangeListener(onPageChangeListener)
    }
}
package dev.voleum.ordermolder.ui.orders

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.databinding.ActivityOrderBinding
import dev.voleum.ordermolder.models.Order
import dev.voleum.ordermolder.ui.general.AbstractDocActivity
import dev.voleum.ordermolder.ui.general.DocListActivity
import dev.voleum.ordermolder.ui.general.SectionsPagerAdapter
import dev.voleum.ordermolder.viewmodels.OrderViewModel

class OrderActivity : AbstractDocActivity<Order, OrderViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        docViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        if (isCreating) docViewModel.setOrder()
        else docViewModel.setOrder(intent.getStringExtra(DocListActivity.DOC))

        val binding: ActivityOrderBinding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        binding.viewModel = docViewModel
        binding.executePendingBindings()

        fab = binding.fab
        progressLayout = binding.progressLayout
        viewPager = binding.viewPager

        sectionPagerAdapter = SectionsPagerAdapter(this,
                supportFragmentManager,
                SectionsPagerAdapter.TYPE_ORDER)

        viewPager.adapter = sectionPagerAdapter
        binding.tabs.setupWithViewPager(viewPager)
        setupToolbar()

        viewPager.addOnPageChangeListener(onPageChangeListener)
    }
}
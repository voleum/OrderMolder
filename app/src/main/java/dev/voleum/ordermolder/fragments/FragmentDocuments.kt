package dev.voleum.ordermolder.fragments

import android.content.Intent
import android.view.View
import android.widget.ListView
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.enums.DocumentTypes
import dev.voleum.ordermolder.ui.cashreceipts.CashReceiptActivity
import dev.voleum.ordermolder.ui.general.DocListActivity
import dev.voleum.ordermolder.ui.orders.OrderActivity

class FragmentDocuments : AbstractListFragment(R.array.documents) {

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val intent = Intent(activity, DocListActivity::class.java)

        val docType = if (position == 0) DocumentTypes.ORDER else DocumentTypes.CASH_RECEIPT
        val docActivity = if (position == 0) OrderActivity::class.java else CashReceiptActivity::class.java

        intent.putExtra(DocListActivity.DOC_TYPE, docType)
        intent.putExtra(DocListActivity.DOC_ACTIVITY, docActivity)

        startActivity(intent)
    }
}
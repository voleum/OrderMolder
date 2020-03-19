package dev.voleum.ordermolder.fragments

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.fragment.app.ListFragment
import dev.voleum.ordermolder.OrderMolder

abstract class AbstractListFragment(@ArrayRes itemList: Int) : ListFragment() {

    val list: Array<String> = OrderMolder.getApplication().resources.getStringArray(itemList)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, list)
        listAdapter = adapter
    }
}
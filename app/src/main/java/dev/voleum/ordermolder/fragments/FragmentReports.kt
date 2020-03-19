package dev.voleum.ordermolder.fragments

import android.view.View
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import dev.voleum.ordermolder.R

class FragmentReports : AbstractListFragment(R.array.reports) {

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        Snackbar.make(v, R.string.snackbar_soon, Snackbar.LENGTH_SHORT).show()
    }
}
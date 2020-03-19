package dev.voleum.ordermolder.fragments

import android.content.Intent
import android.view.View
import android.widget.ListView
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.enums.CatalogTypes
import dev.voleum.ordermolder.ui.catalogs.CatalogActivity
import dev.voleum.ordermolder.ui.catalogs.CatalogListActivity

class FragmentCatalogs : AbstractListFragment(R.array.catalogs) {

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val intent = Intent(activity, CatalogListActivity::class.java)

        val catType = when (position) {
            0 -> CatalogTypes.COMPANY
            1 -> CatalogTypes.PARTNER
            2 -> CatalogTypes.WAREHOUSE
            3 -> CatalogTypes.GOOD
            else -> CatalogTypes.UNIT
        }

        intent.putExtra(CatalogActivity.CAT_TYPE, catType)
        startActivity(intent)
    }
}
package dev.voleum.ordermolder.helpers

interface TableViewModelItemTouchHelper : DocListViewModelItemTouchHelper {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
}
package dev.voleum.ordermolder.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.text.SimpleDateFormat
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import dev.voleum.ordermolder.R
import java.util.*

class SelectDateFragment() : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var yy = 0
    private var mm = 0
    private var dd = 0

    init {
        setCurrentDate()
    }

    constructor(date: String) : this() {
        yy = date.substring(6, 10).toInt()
        mm = date.substring(3, 5).toInt() - 1
        dd = date.substring(0, 2).toInt()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(activity!!, this, yy, mm, dd)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val cal = GregorianCalendar(year, month, dayOfMonth)
        val df = SimpleDateFormat("dd.MM.yyyy")
        val newDate = df.format(cal)
        targetFragment!!.activity!!.findViewById<TextView>(R.id.tv_date).text = newDate
    }

    fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        yy = calendar.get(Calendar.YEAR)
        mm = calendar.get(Calendar.MONTH)
        dd = calendar.get(Calendar.DAY_OF_MONTH)
    }
}
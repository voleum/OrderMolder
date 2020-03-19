package dev.voleum.ordermolder.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import dev.voleum.ordermolder.R

class SelectTimeFragment() : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    var hh = 0
    var mm = 0

    init {
        setCurrentTime()
    }

    constructor(time: String) : this() {
        hh = time.substring(0, 2).toInt()
        hh = time.substring(3, 5).toInt()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, this, hh, mm, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hh = hourOfDay
        mm = minute
        val newTime = "${if (hh < 10) "0" else ""}$hh:${if (mm < 10) "0" else ""}:00"
        targetFragment!!.activity!!.findViewById<TextView>(R.id.tv_time).text = newTime
    }

    fun setCurrentTime() {
        val calendar = Calendar.getInstance()
        hh = calendar.get(Calendar.HOUR_OF_DAY)
        mm = calendar.get(Calendar.MINUTE)
    }
}
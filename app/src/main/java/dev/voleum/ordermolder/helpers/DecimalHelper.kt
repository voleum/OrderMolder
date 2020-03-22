package dev.voleum.ordermolder.helpers

import android.icu.math.BigDecimal
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import java.util.*

class DecimalHelper {

    companion object {

        fun moneyFieldFormat(): DecimalFormat {
            val format = "0.00"
            val otherSymbols = DecimalFormatSymbols(Locale.getDefault())
            otherSymbols.decimalSeparator = '.'
            otherSymbols.groupingSeparator = ','
            return DecimalFormat(format, otherSymbols)
        }

        fun round(value: Double, places: Int): Double {

            if (places < 0) throw IllegalArgumentException()

            return BigDecimal.valueOf(value)
                    .setScale(places, BigDecimal.ROUND_HALF_UP)
                    .toDouble()
        }
    }
}
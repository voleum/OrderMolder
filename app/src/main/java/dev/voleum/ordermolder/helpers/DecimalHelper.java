package dev.voleum.ordermolder.helpers;

import android.icu.math.BigDecimal;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;

import java.util.Locale;

public class DecimalHelper {

    public static DecimalFormat newMoneyFieldFormat() {
        String format = "0.00";
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        return new DecimalFormat(format, otherSymbols);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}

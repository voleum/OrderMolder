package dev.voleum.ordermolder.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import dev.voleum.ordermolder.R;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int yy;
    private int mm;
    private int dd;

    public SelectDateFragment() {
        setCurrentDate();
    }

    public SelectDateFragment(String date) {
        try {
            yy = Integer.parseInt(date.substring(6, 10));
            mm = Integer.parseInt(date.substring(3, 5)) - 1;
            dd = Integer.parseInt(date.substring(0, 2));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            setCurrentDate();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, yy, mm, dd);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String newDate = df.format(cal);
        ((TextView) getTargetFragment().getActivity().findViewById(R.id.order_tv_date)).setText(newDate);
    }

    private void setCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        yy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd = calendar.get(Calendar.DAY_OF_MONTH);
    }
}

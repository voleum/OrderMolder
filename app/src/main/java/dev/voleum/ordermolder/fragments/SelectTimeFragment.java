package dev.voleum.ordermolder.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import dev.voleum.ordermolder.R;

public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int hh;
    private int mm;

    public SelectTimeFragment() {
        setCurrentTime();
    }

    public SelectTimeFragment(String time) {
        try {
            hh = Integer.parseInt(time.substring(0, 2));
            mm = Integer.parseInt(time.substring(3, 5));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            setCurrentTime();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), this, hh, mm, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String h = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
        String m = minute < 10 ? "0" + minute : String.valueOf(minute);
        String newTime = h + ":" + m + ":00";
        try {
            ((TextView) Objects.requireNonNull(getTargetFragment()).getActivity().findViewById(R.id.tv_time)).setText(newTime);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentTime() {
        final Calendar calendar = Calendar.getInstance();
        hh = calendar.get(Calendar.HOUR_OF_DAY);
        mm = calendar.get(Calendar.MINUTE);
    }
}

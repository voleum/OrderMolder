package dev.voleum.ordermolder;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.takisoft.preferencex.EditTextPreference;
import com.takisoft.preferencex.PreferenceFragmentCompat;

@Deprecated
public class SettingsActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            EditTextPreference passwordPreference = findPreference("password");
            setPasswordSummary(passwordPreference, passwordPreference.getText());
            try {
                passwordPreference.setOnPreferenceChangeListener(this);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            ListPreference themePreference = findPreference("theme");
            try {
                themePreference.setOnPreferenceChangeListener(this);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "password":
                    setPasswordSummary((EditTextPreference) preference, newValue.toString());
                    break;
                case "theme":
                    AppCompatDelegate.setDefaultNightMode(Integer.parseInt((String) newValue));
                    break;
                default:
                    return false;
            }
            return true;
        }

        private void setPasswordSummary(EditTextPreference preference, CharSequence value) {
            if (value != null) {
                EditText edit = preference.getEditText();
                String pref = edit.getTransformationMethod().getTransformation(value, edit).toString();
                preference.setSummary(pref);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

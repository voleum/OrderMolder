package dev.voleum.ordermolder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.takisoft.preferencex.EditTextPreference
import com.takisoft.preferencex.PreferenceFragmentCompat
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.SettingsActivityJava


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsActivityJava.SettingsFragment())
                .commit()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

        override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val passwordPreference = findPreference<EditTextPreference>("password")
            setPasswordSummary(passwordPreference!!, passwordPreference.text)
            passwordPreference.onPreferenceChangeListener = this
            val themePreference = findPreference<ListPreference>("theme")
            themePreference!!.onPreferenceChangeListener = this
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            when (preference!!.key) {
                "password" -> setPasswordSummary(preference as EditTextPreference, newValue as String)
                "theme"    -> AppCompatDelegate.setDefaultNightMode((newValue as String).toInt())
                else       -> return false
            }
            return true
        }

        private fun setPasswordSummary(preference: EditTextPreference, value: String?) {
            preference.editText.transformationMethod.getTransformation(value, preference.editText)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
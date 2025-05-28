package my.dir.catwikiprototype.ui.theme.settings

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import my.dir.catwikiprototype.R
import my.dir.catwikiprototype.data.local.RoomCatRepository

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Confirmation dialog when “reset data” is clicked
        findPreference<Preference>("pref_reset_data")
            ?.setOnPreferenceClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Скинути всі дані?")
                    .setMessage("Ви впевнені, що хочете видалити всіх котів :(")
                    .setPositiveButton("Так") { _, _ ->
                        RoomCatRepository(requireContext()).resetAll()
                    }
                    .setNegativeButton("Ні", null)
                    .show()
                true
            }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences
            ?.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == "pref_dark_mode") {
            val enabled = sharedPreferences.getBoolean(key, false)
            AppCompatDelegate.setDefaultNightMode(
                if (enabled) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}
package my.dir.catwikiprototype.ui.theme.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.dir.catwikiprototype.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // В activity_settings.xml просто FrameLayout із id=container
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Налаштування"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
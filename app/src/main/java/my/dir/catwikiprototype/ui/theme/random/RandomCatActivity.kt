package my.dir.catwikiprototype.ui.theme.random

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.dir.catwikiprototype.R

class RandomCatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_random_cat)

        // Настраиваем тулбар
        setSupportActionBar(findViewById(R.id.toolbar_random))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Показываем фрагмент только один раз
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.randomFragmentContainer, RandomCatFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
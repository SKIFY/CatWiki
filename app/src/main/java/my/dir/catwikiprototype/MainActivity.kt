package my.dir.catwikiprototype

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.dir.catwikiprototype.ui.theme.favorites.FavoritesFragment
import my.dir.catwikiprototype.ui.theme.main.MainFragment
import my.dir.catwikiprototype.ui.theme.random.RandomCatActivity
import my.dir.catwikiprototype.ui.theme.settings.SettingsActivity

/**
 * Інтерфейс для фрагментів, які підтримують пошук.
 */
interface Searchable {
    fun onQueryChanged(query: String)
}

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav).apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_catalog -> {
                        replaceFragment(MainFragment())
                    }
                    R.id.nav_random -> {
                        // запускаємо окрему Activity для випадкової породи
                        startActivity(Intent(this@MainActivity, RandomCatActivity::class.java))
                    }
                    R.id.nav_favorites -> {
                        replaceFragment(FavoritesFragment())
                    }
                    else -> return@setOnItemSelectedListener false
                }
                true
            }
            // При першому запуску обираємо каталог
            if (savedInstanceState == null) {
                selectedItemId = R.id.nav_catalog
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Після повернення з будь-якого екрану знову показуємо каталог
        bottomNav.selectedItemId = R.id.nav_catalog
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_global, menu)
        // Підключаємо глобальний SearchView
        (menu.findItem(R.id.action_search).actionView as? SearchView)?.apply {
            queryHint = "Пошук порід…"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    // Делегуємо запит в активний фрагмент
                    (supportFragmentManager
                        .findFragmentById(R.id.fragmentContainer) as? Searchable)
                        ?.onQueryChanged(newText)
                    return true
                }
                override fun onQueryTextSubmit(query: String) = false
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Запускаємо екран налаштувань
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
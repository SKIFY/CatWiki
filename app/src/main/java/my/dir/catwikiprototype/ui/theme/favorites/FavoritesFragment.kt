package my.dir.catwikiprototype.ui.theme.favorites

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import my.dir.catwikiprototype.R
import my.dir.catwikiprototype.Searchable
import my.dir.catwikiprototype.data.Cat
import my.dir.catwikiprototype.data.local.RoomCatRepository
import my.dir.catwikiprototype.ui.theme.details.CatDetailFragment
import my.dir.catwikiprototype.ui.theme.main.CatAdapter
import my.dir.catwikiprototype.utils.SortOption

class FavoritesFragment : Fragment(), Searchable {

    private val repo by lazy { RoomCatRepository(requireContext()) }
    private lateinit var rv: RecyclerView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var emptyView: TextView

    private var currentSort = SortOption.ALPHABETICAL
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_favorites, container, false).apply {
        swipe     = findViewById(R.id.swipeRefreshFavorites)
        rv        = findViewById(R.id.recyclerViewFavorites)
        emptyView = findViewById(R.id.tvEmptyFavorites)

        rv.layoutManager = LinearLayoutManager(context)
        swipe.setOnRefreshListener {
            refreshList()
            swipe.isRefreshing = false
        }

        refreshList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // ті самі пункти сортування, але без "Улюблені зверху"
        inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.sort_favorites)?.isVisible = false

        // делегуємо глобальний SearchView в onQueryChanged
        (activity as? Searchable)?.let { /* nothing */ }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.sort_alpha -> {
            currentSort = SortOption.ALPHABETICAL
            refreshList()
            true
        }
        R.id.sort_reverse -> {
            currentSort = SortOption.REVERSE_ALPHABETICAL
            refreshList()
            true
        }
        else -> false
    }

    override fun onQueryChanged(query: String) {
        currentQuery = query
        refreshList()
    }

    private fun refreshList() {
        val all = if (currentQuery.isBlank())
            repo.getCats(currentSort)
        else
            repo.searchCats(currentQuery)

        val favs = all.filter { it.isFavorite }
        if (favs.isEmpty()) {
            rv.visibility = GONE
            emptyView.visibility = VISIBLE
        } else {
            emptyView.visibility = GONE
            rv.visibility = VISIBLE
            rv.adapter = CatAdapter(
                cats = favs,
                onItemClick = { cat ->
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CatDetailFragment.newInstance(cat))
                        .addToBackStack(null)
                        .commit()
                },
                onDeleteClick = { cat ->
                    // --- ВСТАВИЛИ DIALOG ПОДТВЕРЖДЕНИЯ ---
                    androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Видалити з улюблених?")
                        .setMessage("Ви справді хочете прибрати «${cat.name}» з улюблених?")
                        .setPositiveButton("Так") { _, _ ->
                            repo.toggleFavorite(cat.id)
                            refreshList()
                        }
                        .setNegativeButton("Ні", null)
                        .show()
                }
            )
        }
    }
}
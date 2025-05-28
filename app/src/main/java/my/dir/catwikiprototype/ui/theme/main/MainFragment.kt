package my.dir.catwikiprototype.ui.theme.main

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import my.dir.catwikiprototype.R
import my.dir.catwikiprototype.Searchable
import my.dir.catwikiprototype.data.Cat
import my.dir.catwikiprototype.data.CatRepository
import my.dir.catwikiprototype.data.local.RoomCatRepository
import my.dir.catwikiprototype.ui.theme.add.AddCatDialogFragment
import my.dir.catwikiprototype.ui.theme.details.CatDetailFragment
import my.dir.catwikiprototype.utils.SortOption

class MainFragment : Fragment(), Searchable {

    private lateinit var repo: CatRepository
    private lateinit var rv: RecyclerView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var emptyState: LinearLayout
    private lateinit var emptyAddBtn: Button
    private lateinit var fab: FloatingActionButton

    private var currentSort = SortOption.ALPHABETICAL
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        repo = RoomCatRepository(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_main, container, false).apply {
        swipe       = findViewById(R.id.swipeRefreshMain)
        rv          = findViewById(R.id.recyclerView)
        emptyState  = findViewById(R.id.emptyState)
        emptyAddBtn = findViewById(R.id.btnEmptyAdd)
        fab         = findViewById(R.id.fab_add)

        rv.layoutManager = LinearLayoutManager(context)

        // pull-to-refresh
        swipe.setOnRefreshListener {
            refreshList()
            swipe.isRefreshing = false
        }

        // добавить новую породу
        val showAdd = {
            AddCatDialogFragment { refreshList() }
                .show(parentFragmentManager, "add_cat")
        }
        fab.setOnClickListener     { showAdd() }
        emptyAddBtn.setOnClickListener { showAdd() }

        refreshList()
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // меню сортировки + добавить
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_alpha    -> currentSort = SortOption.ALPHABETICAL
            R.id.sort_reverse  -> currentSort = SortOption.REVERSE_ALPHABETICAL
            R.id.sort_favorites-> currentSort = SortOption.FAVORITES_FIRST
        }
        refreshList()
        return true
    }

    override fun onQueryChanged(query: String) {
        currentQuery = query
        refreshList()
    }

    private fun refreshList() {
        // фильтрация + сортировка
        val list = if (currentQuery.isBlank()) {
            repo.getCats(currentSort)
        } else {
            repo.searchCats(currentQuery).let { filtered ->
                when (currentSort) {
                    SortOption.ALPHABETICAL        -> filtered.sortedBy { it.name.lowercase() }
                    SortOption.REVERSE_ALPHABETICAL -> filtered.sortedByDescending { it.name.lowercase() }
                    SortOption.FAVORITES_FIRST     -> filtered.sortedWith(
                        compareByDescending<Cat> { it.isFavorite }
                            .thenBy { it.name.lowercase() }
                    )
                }
            }
        }

        if (list.isEmpty()) {
            rv.visibility         = GONE
            emptyState.visibility = VISIBLE
        } else {
            emptyState.visibility = GONE
            rv.visibility         = VISIBLE
            rv.adapter = CatAdapter(
                cats = list,
                onItemClick = { cat ->
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CatDetailFragment.newInstance(cat))
                        .addToBackStack(null)
                        .commit()
                },
                onDeleteClick = { cat ->
                    // подтверждение удаления
                    AlertDialog.Builder(requireContext())
                        .setTitle("Видалити породу?")
                        .setMessage("Ви впевнені, що хочете видалити «${cat.name}»?")
                        .setPositiveButton("Так") { _, _ ->
                            repo.deleteCat(cat.id)
                            refreshList()
                        }
                        .setNegativeButton("Ні", null)
                        .show()
                }
            )
        }
    }
}
package my.dir.catwikiprototype.ui.theme.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import my.dir.catwikiprototype.R
import my.dir.catwikiprototype.data.Cat
import my.dir.catwikiprototype.data.local.RoomCatRepository
import my.dir.catwikiprototype.ui.theme.edit.EditCatDialogFragment
import my.dir.catwikiprototype.utils.ImageUtils

class CatDetailFragment : Fragment() {

    companion object {
        private const val ARG_CAT = "arg_cat"
        fun newInstance(cat: Cat) = CatDetailFragment().apply {
            arguments = Bundle().apply { putParcelable(ARG_CAT, cat) }
        }
    }

    private lateinit var repo: RoomCatRepository
    private lateinit var cat: Cat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // цей фрагмент не має свого меню
        setHasOptionsMenu(false)

        repo = RoomCatRepository(requireContext())
        cat  = requireArguments().getParcelable(ARG_CAT)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_cat_detail, container, false).apply {
        // заповнюємо дані
        findViewById<TextView>(R.id.catDetailName).text = cat.name
        findViewById<TextView>(R.id.catDetailDesc).text = cat.description
        ImageUtils.loadImage(findViewById<ImageView>(R.id.catDetailImage), cat.imageUrl)

        // кнопка «У обране»
        val favBtn = findViewById<Button>(R.id.addToFavoritesButton)
        favBtn.text = if (cat.isFavorite) "Вилучити з улюблених" else "У обране"
        favBtn.setOnClickListener {
            repo.toggleFavorite(cat.id)
            cat = cat.copy(isFavorite = !cat.isFavorite)
            favBtn.text = if (cat.isFavorite) "Вилучити з улюблених" else "У обране"
        }

        // кнопка «Поділитися»
        findViewById<Button>(R.id.btnShare).setOnClickListener {
            shareCat(cat)
        }

        // кнопка «Редагувати»
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            EditCatDialogFragment(cat) {
                // після редагування оновлюємо UI
                cat = repo.getCats().first { it.id == cat.id }
                findViewById<TextView>(R.id.catDetailName).text = cat.name
                findViewById<TextView>(R.id.catDetailDesc).text = cat.description
                ImageUtils.loadImage(findViewById(R.id.catDetailImage), cat.imageUrl)
            }.show(parentFragmentManager, "edit_cat")
        }
    }

    private fun shareCat(cat: Cat) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            // якщо URL починається з http, то ділимось лише текстом
            if (cat.imageUrl.startsWith("http")) {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Порода: ${cat.name}\n${cat.description}\nФото: ${cat.imageUrl}"
                )
                cat.videoUrl?.let { putExtra(Intent.EXTRA_TEXT, "\nВідео: $it") }
            } else {
                // локальна картинка — ділимося самою зображенням
                val uri = cat.imageUrl.takeIf { it.isNotBlank() }?.let(Uri::parse)
                if (uri != null) {
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    // додатково додаємо текст
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Порода: ${cat.name}\n${cat.description}"
                    )
                } else {
                    // fallback — просто текст
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Порода: ${cat.name}\n${cat.description}"
                    )
                }
            }
        }
        startActivity(Intent.createChooser(intent, "Поділитися через"))
    }
}
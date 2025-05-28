package my.dir.catwikiprototype.ui.theme.random

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
import my.dir.catwikiprototype.utils.ImageUtils
import kotlin.random.Random

class RandomCatFragment : Fragment() {

    private val repo by lazy { RoomCatRepository(requireContext()) }
    private var current: Cat? = null

    private lateinit var img: ImageView
    private lateinit var nameTv: TextView
    private lateinit var descTv: TextView
    private lateinit var nextBtn: Button
    private lateinit var favBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_random_cat, container, false).apply {
        img      = findViewById(R.id.randomCatImage)
        nameTv   = findViewById(R.id.randomCatName)
        descTv   = findViewById(R.id.randomCatDesc)
        nextBtn  = findViewById(R.id.btnNextRandom)
        favBtn   = findViewById(R.id.btnFavoriteRandom)

        nextBtn.setOnClickListener { showRandom() }
        favBtn.setOnClickListener {
            current?.let { cat ->
                repo.toggleFavorite(cat.id)
                updateFavoriteButton(cat.copy(isFavorite = !cat.isFavorite))
            }
        }

        showRandom()
    }

    private fun showRandom() {
        // беремо тільки ті, що не улюблені
        val candidates = repo.getCats().filter { !it.isFavorite }
        if (candidates.isEmpty()) {
            nameTv.text = "Немає нових порід"
            descTv.text = ""
            ImageUtils.loadImage(img, null)
            nextBtn.isEnabled = false
            favBtn.isEnabled = false
        } else {
            val cat = candidates[Random.nextInt(candidates.size)]
            current = cat
            nameTv.text = cat.name
            descTv.text = cat.description
            ImageUtils.loadImage(img, cat.imageUrl)
            updateFavoriteButton(cat)
        }
    }

    private fun updateFavoriteButton(cat: Cat) {
        current = cat
        favBtn.text = if (cat.isFavorite) "Вилучити з улюблених" else "У обране"
    }
}
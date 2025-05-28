package my.dir.catwikiprototype.data.local

import android.content.Context
import my.dir.catwikiprototype.data.Cat
import my.dir.catwikiprototype.data.CatRepository
import my.dir.catwikiprototype.utils.SortOption

/**
 * Реалізація CatRepository на основі Room.
 */
class RoomCatRepository(context: Context) : CatRepository {
    private val dao: CatDao = CatDatabase.getInstance(context).catDao()

    override fun getCats(sortOption: SortOption): List<Cat> =
        when (sortOption) {
            SortOption.ALPHABETICAL ->
                dao.getAllAlphabetical().map { it.toDomain() }

            SortOption.REVERSE_ALPHABETICAL ->
                dao.getAllReverse().map { it.toDomain() }

            SortOption.FAVORITES_FIRST -> {
                // Всі коти, потім улюблені зверху
                val all = dao.getAllAlphabetical().map { it.toDomain() }
                all.sortedWith(
                    compareByDescending<Cat> { it.isFavorite }
                        .thenBy { it.name.lowercase() }
                )
            }
        }

    override fun searchCats(query: String): List<Cat> =
        dao.search(query).map { it.toDomain() }

    override fun addCat(cat: Cat) {
        dao.insert(cat.toEntity())
    }

    override fun toggleFavorite(catId: Int) {
        // беремо ентіті за id, міняємо isFavorite і апдейтимо
        dao.getById(catId)?.let { entity ->
            val toggled = entity.copy(isFavorite = !entity.isFavorite)
            dao.update(toggled)
        }
    }

    override fun updateCat(cat: Cat) {
        dao.update(cat.toEntity())
    }

    override fun deleteCat(catId: Int) {
        dao.deleteById(catId)
    }

    /** Скинути всі дані (для Settings) */
    fun resetAll() {
        dao.clearAll()
    }
}

// Розширення для конвертації між Entity і Domain
private fun CatEntity.toDomain(): Cat =
    Cat(id, name, description, imageUrl, videoUrl, isFavorite)

private fun Cat.toEntity(): CatEntity =
    CatEntity(id, name, description, imageUrl, videoUrl, isFavorite)
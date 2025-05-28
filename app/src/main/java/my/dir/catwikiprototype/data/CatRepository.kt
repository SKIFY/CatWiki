package my.dir.catwikiprototype.data

import my.dir.catwikiprototype.utils.SortOption

/**
 * Interface defining operations for accessing and manipulating Cat data.
 */
interface CatRepository {
    /** Повертає список котів, з можливістю сортування */
    fun getCats(sortOption: SortOption = SortOption.ALPHABETICAL): List<Cat>

    /** Пошук котів за рядком у назві або описі */
    fun searchCats(query: String): List<Cat>

    /** Додає нову породу */
    fun addCat(cat: Cat)

    /** Перемикає статус «улюбленої» */
    fun toggleFavorite(catId: Int)

    /** Оновлює існуючу породу */
    fun updateCat(cat: Cat)

    /** Видаляє породу за її ID */
    fun deleteCat(catId: Int)
}
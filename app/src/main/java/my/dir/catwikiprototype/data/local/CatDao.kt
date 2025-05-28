package my.dir.catwikiprototype.data.local

import androidx.room.*

@Dao
interface CatDao {
    @Query("SELECT * FROM cats ORDER BY name COLLATE NOCASE ASC")
    fun getAllAlphabetical(): List<CatEntity>

    @Query("SELECT * FROM cats ORDER BY name COLLATE NOCASE DESC")
    fun getAllReverse(): List<CatEntity>

    @Query("SELECT * FROM cats")
    fun getAllByPopularity(): List<CatEntity>

    @Query("SELECT * FROM cats WHERE name LIKE '%' || :q || '%' OR description LIKE '%' || :q || '%'")
    fun search(q: String): List<CatEntity>

    @Query("SELECT * FROM cats WHERE id = :id")
    fun getById(id: Int): CatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cat: CatEntity)

    @Update
    fun update(cat: CatEntity)

    @Query("DELETE FROM cats WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM cats")
    fun clearAll()
}
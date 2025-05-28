package my.dir.catwikiprototype.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String?,
    val isFavorite: Boolean = false
)
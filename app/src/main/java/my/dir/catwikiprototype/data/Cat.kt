package my.dir.catwikiprototype.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cat(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String?,
    var isFavorite: Boolean = false
) : Parcelable
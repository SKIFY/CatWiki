package my.dir.catwikiprototype.utils

import my.dir.catwikiprototype.data.Cat
import my.dir.catwikiprototype.utils.SortOption

/**
 * MockData provides an initial list of cats for demonstration or testing.
 */
object MockData {
    fun getInitialCats(): List<Cat> = listOf(
        Cat(
            id = 1,
            name = "Maine Coon",
            description = "Large, sociable cat breed with tufted ears.",
            imageUrl = "https://example.com/images/maine_coon.jpg",
            videoUrl = "https://youtu.be/maine_coon_video",
            isFavorite = false
        ),
        Cat(
            id = 2,
            name = "Siamese",
            description = "Sleek, vocal breed with striking blue eyes.",
            imageUrl = "https://example.com/images/siamese.jpg",
            videoUrl = null,
            isFavorite = false
        ),
        Cat(
            id = 3,
            name = "Persian",
            description = "Long-haired, calm breed with a flat face.",
            imageUrl = "https://example.com/images/persian.jpg",
            videoUrl = "https://youtu.be/persian_video",
            isFavorite = false
        )
    )
}
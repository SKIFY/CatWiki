package my.dir.catwikiprototype.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import my.dir.catwikiprototype.R

object ImageUtils {

    fun loadImage(
        imageView: ImageView,
        path: String?,
        placeholderResId: Int = R.drawable.ic_cat_placeholder
    ) {
        if (path.isNullOrBlank()) {
            // якщо немає URL — ставимо просто заглушку
            imageView.setImageResource(placeholderResId)
            return
        }

        // конвертуємо у Uri лише якщо це потрібно
        val uri: Any = try {
            if (path.startsWith("content://") || path.startsWith("file://")) {
                Uri.parse(path)
            } else {
                path // вважаємо за URL
            }
        } catch (e: Exception) {
            path
        }

        Glide.with(imageView.context)
            .load(uri)
            .apply(RequestOptions()
                .placeholder(placeholderResId)
                .error(placeholderResId))
            .into(imageView)
    }
}
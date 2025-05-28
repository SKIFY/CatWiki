package my.dir.catwikiprototype.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import my.dir.catwikiprototype.data.Cat

object FavoriteUtils {
    private const val PREF = "favorites"
    private const val KEY = "cat_list"

    fun loadFavorites(ctx: Context): MutableList<Cat> {
        val json = ctx.getSharedPreferences(PREF, 0).getString(KEY, null)
        return if (json != null)
            Gson().fromJson(json, object : TypeToken<MutableList<Cat>>() {}.type)
        else mutableListOf()
    }

    fun saveFavorites(ctx: Context, list: List<Cat>) {
        ctx.getSharedPreferences(PREF, 0)
            .edit()
            .putString(KEY, Gson().toJson(list))
            .apply()
    }

    fun addToFavorites(ctx: Context, cat: Cat) {
        val fav = loadFavorites(ctx)
        if (fav.none { it.id == cat.id }) {
            fav.add(cat)
            saveFavorites(ctx, fav)
        }
    }

    fun removeFromFavorites(ctx: Context, cat: Cat) {
        val fav = loadFavorites(ctx)
        if (fav.removeIf { it.id == cat.id }) {
            saveFavorites(ctx, fav)
        }
    }
}
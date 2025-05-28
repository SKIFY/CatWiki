package my.dir.catwikiprototype.ui.theme.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import my.dir.catwikiprototype.data.Cat
import my.dir.catwikiprototype.R

class CatAdapter(
    private val cats: List<Cat>,
    private val onItemClick: (Cat) -> Unit,
    private val onDeleteClick: ((Cat) -> Unit)? = null
) : RecyclerView.Adapter<CatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cat, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(cats[position], onItemClick, onDeleteClick)
    }

    override fun getItemCount(): Int = cats.size
}
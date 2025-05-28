package my.dir.catwikiprototype.ui.theme.main

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import my.dir.catwikiprototype.R
import my.dir.catwikiprototype.data.Cat
import my.dir.catwikiprototype.utils.ImageUtils

class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val catImage: ImageView = itemView.findViewById(R.id.catImage)
    private val catName: TextView = itemView.findViewById(R.id.catName)
    private val catDesc: TextView = itemView.findViewById(R.id.catShortDesc)
    private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

    fun bind(
        cat: Cat,
        onItemClick: (Cat) -> Unit,
        onDeleteClick: ((Cat) -> Unit)?
    ) {
        catName.text = cat.name
        catDesc.text = cat.description
        ImageUtils.loadImage(catImage, cat.imageUrl)

        itemView.setOnClickListener { onItemClick(cat) }
        if (onDeleteClick != null) {
            btnDelete.visibility = View.VISIBLE
            btnDelete.setOnClickListener { onDeleteClick(cat) }
        } else {
            btnDelete.visibility = View.GONE
        }
    }
}
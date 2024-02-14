package ru.marat.roulette.fragments.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.marat.roulette.R

class IconsRVA(
    private val list: List<Int?>,
    val onClick: (Int?) -> Unit
) : RecyclerView.Adapter<IconsRVA.IconVH>() {


    inner class IconVH(private val view: ImageView) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            view.setImageResource(list[position] ?: R.drawable.baseline_close_24)
            view.setOnClickListener {
                onClick(list[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.icon, parent, false)
        return IconVH(view.findViewById(R.id.item_icon))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: IconVH, position: Int) {
        holder.bind(position)
    }
}
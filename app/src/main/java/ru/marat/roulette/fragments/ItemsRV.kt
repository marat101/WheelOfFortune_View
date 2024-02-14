package ru.marat.roulette.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.marat.roulette.Item
import ru.marat.roulette.R

@SuppressLint("NotifyDataSetChanged")
class ItemsRV(val onEditClick: () -> Unit) : RecyclerView.Adapter<ItemsRV.ItemVH>() {

    var list = listOf<Item>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
        return ItemVH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.bind(position)
    }

    inner class ItemVH(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            val name = view.findViewById<TextView>(R.id.item_name)
            val value = view.findViewById<TextView>(R.id.item_value)
            val color = view.findViewById<View>(R.id.item_color)
            val icon = view.findViewById<ImageView>(R.id.item_icon)
            val item = list[position]

            name.text = item.name
            value.text = item.value.toString()
            color.setBackgroundColor(item.color)
            item.icon?.let { icon.setImageResource(it) }
        }
    }
}


package com.example.aviasalesanim.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aviasalesanim.R
import com.example.aviasalesanim.domain.model.City

internal class CityAdapter(
    private val onItemClick: ((city: City) -> Unit)
) : ListAdapter<City, CityViewHolder>(CityDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.city_item, parent, false)

        val holder = CityViewHolder(itemView as TextView)
        holder.itemView.setOnClickListener { onItemClick(getItem(holder.adapterPosition)) }

        return holder
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object CityDiffCallback : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.fullname == newItem.fullname
        }
    }
}

class CityViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {

    fun bind(item: City) {
        textView.text = item.fullname
    }
}
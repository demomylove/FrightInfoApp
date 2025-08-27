package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.FavoriteRoute

class FavoriteRouteAdapter(
    private val onDeleteClick: (FavoriteRoute) -> Unit,
) : ListAdapter<FavoriteRoute, FavoriteRouteAdapter.ViewHolder>(FavoriteRouteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_route, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val route = getItem(position)
        holder.bind(route)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val routeTextView: TextView = itemView.findViewById(R.id.routeTextView)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

        fun bind(route: FavoriteRoute) {
            routeTextView.text = "${route.originAirport} -> ${route.destinationAirport}"
            deleteButton.setOnClickListener { onDeleteClick(route) }
        }
    }
}

class FavoriteRouteDiffCallback : DiffUtil.ItemCallback<FavoriteRoute>() {
    override fun areItemsTheSame(oldItem: FavoriteRoute, newItem: FavoriteRoute):
        Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavoriteRoute, newItem: FavoriteRoute):
        Boolean {
        return oldItem == newItem
    }
}

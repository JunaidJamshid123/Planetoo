package com.example.planetapp.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.planetapp.databinding.ViewBinding
import com.example.planetapp.domain.model.Planet
import com.example.planetapp.presentation.detail.PlanetDetailActivity
import com.example.planetapp.utils.Constants

class PlanetAdapter : ListAdapter<Planet, PlanetAdapter.PlanetViewHolder>(PlanetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
        val binding = ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlanetViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(planet: Planet) {
            binding.apply {
                title.text = planet.name
                Galaxy.text = planet.galaxy
                distance.text = "${planet.distanceFromSun} M km"
                gravity.text = "${planet.gravity} m/s²"
                planetImage.setImageResource(planet.image)
                
                root.setOnClickListener {
                    val intent = Intent(itemView.context, PlanetDetailActivity::class.java).apply {
                        putExtra(Constants.EXTRA_PLANET, planet)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    class PlanetDiffCallback : DiffUtil.ItemCallback<Planet>() {
        override fun areItemsTheSame(oldItem: Planet, newItem: Planet): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Planet, newItem: Planet): Boolean {
            return oldItem == newItem
        }
    }
}

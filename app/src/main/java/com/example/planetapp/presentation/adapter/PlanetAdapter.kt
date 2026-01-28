package com.example.planetapp.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.planetapp.databinding.ItemPlanetListBinding
import com.example.planetapp.domain.model.Planet
import com.example.planetapp.presentation.detail.PlanetDetailActivity
import com.example.planetapp.utils.Constants

class PlanetAdapter : ListAdapter<Planet, PlanetAdapter.PlanetViewHolder>(PlanetDiffCallback()) {

    private var lastAnimatedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
        val binding = ItemPlanetListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        holder.bind(getItem(position))
        
        // Animate items as they appear
        if (position > lastAnimatedPosition) {
            animateItem(holder, position)
            lastAnimatedPosition = position
        }
    }
    
    private fun animateItem(holder: PlanetViewHolder, position: Int) {
        holder.itemView.apply {
            alpha = 0f
            translationY = 30f
            translationX = 0f
            scaleX = 0.98f
            scaleY = 0.98f
            
            animate()
                .alpha(1f)
                .translationY(0f)
                .translationX(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(350)
                .setStartDelay((position * 60).toLong())
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Animate planet image with bounce
        holder.animatePlanetIcon(position)
    }
    
    fun resetAnimations() {
        lastAnimatedPosition = -1
    }

    class PlanetViewHolder(private val binding: ItemPlanetListBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(planet: Planet) {
            binding.apply {
                planetName.text = planet.name
                planetGalaxy.text = planet.galaxy
                planetDistance.text = "${planet.distanceFromSun} M km"
                planetGravity.text = "${planet.gravity} m/s²"
                planetImage.setImageResource(planet.image)
                
                root.setOnClickListener {
                    // Click animation
                    root.animate()
                        .scaleX(0.96f)
                        .scaleY(0.96f)
                        .setDuration(100)
                        .withEndAction {
                            root.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction {
                                    val intent = Intent(itemView.context, PlanetDetailActivity::class.java).apply {
                                        putExtra(Constants.EXTRA_PLANET, planet)
                                    }
                                    itemView.context.startActivity(intent)
                                }
                                .start()
                        }
                        .start()
                }
            }
        }
        
        fun animatePlanetIcon(position: Int) {
            binding.planetImage.apply {
                scaleX = 0.3f
                scaleY = 0.3f
                rotation = -20f
                
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .rotation(0f)
                    .setDuration(500)
                    .setStartDelay((position * 80 + 150).toLong())
                    .setInterpolator(OvershootInterpolator(1.5f))
                    .start()
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

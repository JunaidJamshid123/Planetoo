package com.example.planetapp.presentation.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.planetapp.R
import com.example.planetapp.data.remote.model.spacex.Rocket
import com.example.planetapp.databinding.ItemRocketBinding
import java.text.NumberFormat
import java.util.*

class RocketAdapter : ListAdapter<Rocket, RocketAdapter.RocketViewHolder>(RocketDiffCallback()) {
    
    private var lastAnimatedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketViewHolder {
        val binding = ItemRocketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RocketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RocketViewHolder, position: Int) {
        holder.bind(getItem(position))
        
        if (position > lastAnimatedPosition) {
            animateItem(holder, position)
            lastAnimatedPosition = position
        }
    }
    
    private fun animateItem(holder: RocketViewHolder, position: Int) {
        holder.itemView.apply {
            alpha = 0f
            translationY = 40f
            scaleX = 0.95f
            scaleY = 0.95f
            
            animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400)
                .setStartDelay((position * 80).toLong())
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    class RocketViewHolder(private val binding: ItemRocketBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(rocket: Rocket) {
            binding.apply {
                rocketName.text = rocket.name
                rocketCompany.text = rocket.company ?: "SpaceX"
                rocketCountry.text = rocket.country ?: "United States"
                
                // Height
                val heightMeters = rocket.height?.meters?.toInt() ?: 0
                rocketHeight.text = "${heightMeters}m"
                
                // Success rate
                val successRate = rocket.successRatePct ?: 0
                rocketSuccessRate.text = "${successRate}%"
                
                // Cost per launch
                val cost = rocket.costPerLaunch ?: 0
                val costFormatted = if (cost >= 1_000_000) {
                    "$${cost / 1_000_000}M"
                } else if (cost >= 1_000) {
                    "$${cost / 1_000}K"
                } else {
                    "$$cost"
                }
                rocketCost.text = costFormatted
                
                // Load rocket image
                val imageUrl = rocket.flickrImages?.firstOrNull()
                if (!imageUrl.isNullOrEmpty()) {
                    rocketImage.load(imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_satellite)
                        error(R.drawable.ic_satellite)
                    }
                } else {
                    rocketImage.setImageResource(R.drawable.ic_satellite)
                }
                
                // Status badge
                if (rocket.active) {
                    rocketStatus.text = "ACTIVE"
                    rocketStatus.setBackgroundResource(R.drawable.badge_size_large)
                } else {
                    rocketStatus.text = "RETIRED"
                    rocketStatus.setBackgroundResource(R.drawable.badge_size_small)
                }
                
                // Click to open Wikipedia
                root.setOnClickListener {
                    if (!rocket.wikipedia.isNullOrEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rocket.wikipedia))
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }

    class RocketDiffCallback : DiffUtil.ItemCallback<Rocket>() {
        override fun areItemsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
            return oldItem == newItem
        }
    }
}

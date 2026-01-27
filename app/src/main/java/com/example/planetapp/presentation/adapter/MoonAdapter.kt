package com.example.planetapp.presentation.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.planetapp.R
import com.example.planetapp.databinding.ItemMoonBinding
import com.example.planetapp.domain.model.Moon
import java.text.NumberFormat
import java.util.Locale

/**
 * RecyclerView Adapter for displaying moons
 */
class MoonAdapter(
    private val onMoonClick: (Moon) -> Unit
) : ListAdapter<Moon, MoonAdapter.MoonViewHolder>(MoonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoonViewHolder {
        val binding = ItemMoonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoonViewHolder, position: Int) {
        holder.bind(getItem(position))
        // Entry animation with stagger
        runEntryAnimation(holder.itemView, position)
    }

    private var lastAnimatedPosition = -1
    
    private fun runEntryAnimation(itemView: View, position: Int) {
        // Only animate items we haven't seen yet
        if (position <= lastAnimatedPosition) {
            itemView.alpha = 1f
            itemView.translationY = 0f
            itemView.scaleX = 1f
            itemView.scaleY = 1f
            return
        }
        lastAnimatedPosition = position
        
        itemView.alpha = 0f
        itemView.translationY = 40f
        itemView.scaleX = 0.95f
        itemView.scaleY = 0.95f
        
        itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(400)
            .setStartDelay((position * 80L).coerceAtMost(320L))
            .setInterpolator(OvershootInterpolator(0.5f))
            .start()
    }
    
    fun resetAnimations() {
        lastAnimatedPosition = -1
    }

    inner class MoonViewHolder(
        private val binding: ItemMoonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moon: Moon) {
            with(binding) {
                // Moon name
                moonName.text = moon.englishName
                
                // Parent planet
                parentPlanet.text = moon.getParentPlanetDisplayName()
                
                // Set parent planet icon
                val planetIconRes = getPlanetIconRes(moon.getParentPlanetDisplayName())
                parentPlanetIcon.setImageResource(planetIconRes)
                
                // Format radius
                val formattedRadius = if (moon.meanRadius >= 100) {
                    "${NumberFormat.getNumberInstance(Locale.US).format(moon.meanRadius.toInt())} km"
                } else if (moon.meanRadius > 0) {
                    "${String.format(Locale.US, "%.1f", moon.meanRadius)} km"
                } else {
                    "Unknown"
                }
                moonRadius.text = formattedRadius
                
                // Orbital period
                moonOrbit.text = moon.getOrbitalPeriodFormatted()
                
                // Animate glow effect
                moonGlow.alpha = 0.3f
                moonGlow.animate()
                    .alpha(0.6f)
                    .setDuration(800)
                    .setStartDelay((adapterPosition * 100L).coerceAtMost(400L))
                    .withEndAction {
                        moonGlow.animate()
                            .alpha(0.4f)
                            .setDuration(600)
                            .start()
                    }
                    .start()
                
                // Size badge text
                val sizeCategory = moon.getSizeCategory().uppercase().replace(" MOON", "")
                sizeBadge.text = sizeCategory
                
                // Set badge background based on size
                val badgeDrawable = when {
                    moon.meanRadius > 1000 -> R.drawable.badge_size_giant
                    moon.meanRadius > 500 -> R.drawable.badge_size_large
                    moon.meanRadius > 100 -> R.drawable.badge_size_medium
                    else -> R.drawable.badge_size_small
                }
                sizeBadge.setBackgroundResource(badgeDrawable)
                
                // Moon icon - use the moon drawable
                moonIcon.setImageResource(R.drawable.moon)
                
                // Click listener with animation
                root.setOnClickListener {
                    runClickAnimation(it) {
                        onMoonClick(moon)
                    }
                }
            }
        }
        
        private fun getPlanetIconRes(planetName: String): Int {
            return when (planetName.lowercase()) {
                "earth" -> R.drawable.planet_earth
                "mars" -> R.drawable.planet_mars
                "jupiter" -> R.drawable.planet_jupiter
                "saturn" -> R.drawable.planet_saturn
                "uranus" -> R.drawable.planet_uranus
                "neptune" -> R.drawable.planet_neptune
                "pluto" -> R.drawable.planet_pluto
                else -> R.drawable.moon
            }
        }
        
        private fun runClickAnimation(view: View, onComplete: () -> Unit) {
            val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f)
            val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f)
            val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f)
            val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f)
            
            scaleDownX.duration = 100
            scaleDownY.duration = 100
            scaleUpX.duration = 100
            scaleUpY.duration = 100
            
            val scaleDown = AnimatorSet().apply {
                playTogether(scaleDownX, scaleDownY)
            }
            
            val scaleUp = AnimatorSet().apply {
                playTogether(scaleUpX, scaleUpY)
            }
            
            AnimatorSet().apply {
                playSequentially(scaleDown, scaleUp)
                start()
            }
            
            view.postDelayed({ onComplete() }, 200)
        }
    }

    class MoonDiffCallback : DiffUtil.ItemCallback<Moon>() {
        override fun areItemsTheSame(oldItem: Moon, newItem: Moon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Moon, newItem: Moon): Boolean {
            return oldItem == newItem
        }
    }
}

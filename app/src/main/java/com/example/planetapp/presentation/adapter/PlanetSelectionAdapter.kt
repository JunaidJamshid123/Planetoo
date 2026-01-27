package com.example.planetapp.presentation.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.planetapp.databinding.ItemPlanetSelectionBinding
import com.example.planetapp.domain.model.PlanetWithMoons

/**
 * Adapter for displaying planets in a grid for moon selection
 * with beautiful animations and glow effects
 */
class PlanetSelectionAdapter(
    private val onPlanetClick: (PlanetWithMoons) -> Unit
) : ListAdapter<PlanetWithMoons, PlanetSelectionAdapter.PlanetViewHolder>(PlanetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
        val binding = ItemPlanetSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlanetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        holder.bind(getItem(position))
        // Staggered entry animation
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 80f
        holder.itemView.scaleX = 0.85f
        holder.itemView.scaleY = 0.85f
        
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay((position * 100).toLong())
            .setInterpolator(OvershootInterpolator(0.7f))
            .start()
    }

    inner class PlanetViewHolder(
        private val binding: ItemPlanetSelectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var floatAnimator: AnimatorSet? = null

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Pulse animation on click
                    animateClick(it) {
                        onPlanetClick(getItem(position))
                    }
                }
            }
            
            // Hover effect on touch
            binding.root.setOnTouchListener { v, event ->
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        animateGlowIn()
                    }
                    android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                        animateGlowOut()
                    }
                }
                false
            }
        }
        
        private fun animateClick(view: View, onComplete: () -> Unit) {
            val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.92f)
            val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.92f)
            val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.92f, 1.05f)
            val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.92f, 1.05f)
            val scaleNormalX = ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 1f)
            val scaleNormalY = ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 1f)
            
            scaleDownX.duration = 80
            scaleDownY.duration = 80
            scaleUpX.duration = 100
            scaleUpY.duration = 100
            scaleNormalX.duration = 80
            scaleNormalY.duration = 80
            
            val animatorSet = AnimatorSet()
            animatorSet.play(scaleDownX).with(scaleDownY)
            animatorSet.play(scaleUpX).with(scaleUpY).after(scaleDownX)
            animatorSet.play(scaleNormalX).with(scaleNormalY).after(scaleUpX)
            
            animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    onComplete()
                }
            })
            
            animatorSet.start()
        }
        
        private fun animateGlowIn() {
            binding.glowOverlay.animate()
                .alpha(0.6f)
                .setDuration(150)
                .start()
        }
        
        private fun animateGlowOut() {
            binding.glowOverlay.animate()
                .alpha(0f)
                .setDuration(200)
                .start()
        }

        fun bind(planet: PlanetWithMoons) {
            binding.apply {
                planetImage.setImageResource(planet.imageRes)
                planetName.text = planet.name
                planetDescription.text = planet.description
                
                // Moon count text with better formatting
                moonCount.text = when (planet.moonCount) {
                    0 -> "No Moons"
                    1 -> "1 Moon"
                    else -> "${planet.moonCount} Moons"
                }
                
                // Start floating animation
                startFloatingAnimation()
            }
        }
        
        private fun startFloatingAnimation() {
            floatAnimator?.cancel()
            
            val floatUp = ObjectAnimator.ofFloat(binding.planetImage, "translationY", 0f, -8f)
            floatUp.duration = 1800
            floatUp.interpolator = AccelerateDecelerateInterpolator()
            
            val floatDown = ObjectAnimator.ofFloat(binding.planetImage, "translationY", -8f, 0f)
            floatDown.duration = 1800
            floatDown.interpolator = AccelerateDecelerateInterpolator()
            
            // Subtle rotation
            val rotateLeft = ObjectAnimator.ofFloat(binding.planetImage, "rotation", 0f, 2f)
            rotateLeft.duration = 1800
            
            val rotateRight = ObjectAnimator.ofFloat(binding.planetImage, "rotation", 2f, 0f)
            rotateRight.duration = 1800
            
            floatAnimator = AnimatorSet().apply {
                play(floatUp).with(rotateLeft)
                play(floatDown).with(rotateRight).after(floatUp)
                addListener(object : android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        if (binding.planetImage.isAttachedToWindow) {
                            start()
                        }
                    }
                })
                start()
            }
        }
        
        fun stopAnimation() {
            floatAnimator?.cancel()
        }
    }

    override fun onViewRecycled(holder: PlanetViewHolder) {
        super.onViewRecycled(holder)
        holder.stopAnimation()
    }

    private class PlanetDiffCallback : DiffUtil.ItemCallback<PlanetWithMoons>() {
        override fun areItemsTheSame(oldItem: PlanetWithMoons, newItem: PlanetWithMoons): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlanetWithMoons, newItem: PlanetWithMoons): Boolean {
            return oldItem == newItem
        }
    }
}

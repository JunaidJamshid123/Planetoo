package com.example.planetapp.presentation.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.example.planetapp.databinding.ItemCategoryBinding
import com.example.planetapp.domain.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var lastAnimatedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position)
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, position: Int) {
            binding.apply {
                // Set content
                categoryTitle.text = category.title
                categorySubtitle.text = category.subtitle
                categoryItemCount.text = category.itemCount
                categoryImage.setImageResource(category.image)
                
                // Hide icon TextView since we're using images now
                categoryIcon.visibility = View.GONE
                
                // Set gradient colors for glow effect
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        Color.parseColor(category.gradientColors.first),
                        Color.parseColor(category.gradientColors.second)
                    )
                )
                glowEffect.background = gradientDrawable
                
                // Hide coming soon overlay
                comingSoonOverlay.visibility = View.GONE
                categoryCard.alpha = 1f
                
                // Beautiful entry animation (only animate new items)
                if (position > lastAnimatedPosition) {
                    lastAnimatedPosition = position
                    runEntryAnimation(position)
                }
                
                // Click listener with bounce animation
                categoryCard.setOnClickListener {
                    runClickAnimation {
                        onCategoryClick(category)
                    }
                }
                
                // Continuous slow rotation for the image
                startImageRotation()
            }
        }
        
        private fun startImageRotation() {
            binding.categoryImage.animate()
                .rotationBy(360f)
                .setDuration(25000)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction { startImageRotation() }
                .start()
        }
        
        private fun runEntryAnimation(position: Int) {
            val view = itemView
            val delay = position * 120L
            
            // Initial state
            view.alpha = 0f
            view.translationY = 80f
            view.scaleX = 0.8f
            view.scaleY = 0.8f
            
            // Fade in animation
            val fadeIn = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
                duration = 500
            }
            
            // Slide up animation
            val slideUp = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 80f, 0f).apply {
                duration = 600
                interpolator = OvershootInterpolator(1.2f)
            }
            
            // Scale X animation
            val scaleXAnim = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.8f, 1f).apply {
                duration = 500
                interpolator = OvershootInterpolator(2f)
            }
            
            // Scale Y animation
            val scaleYAnim = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.8f, 1f).apply {
                duration = 500
                interpolator = OvershootInterpolator(2f)
            }
            
            // Play all animations together with staggered delay
            AnimatorSet().apply {
                playTogether(fadeIn, slideUp, scaleXAnim, scaleYAnim)
                startDelay = delay
                start()
            }
            
            // Animate the image separately with a pulse effect
            binding.categoryImage.alpha = 0f
            binding.categoryImage.scaleX = 0.5f
            binding.categoryImage.scaleY = 0.5f
            binding.categoryImage.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(700)
                .setStartDelay(delay + 200)
                .setInterpolator(OvershootInterpolator(3f))
                .start()
            
            // Animate the glow effect
            binding.glowEffect.scaleX = 0f
            binding.glowEffect.scaleY = 0f
            binding.glowEffect.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .setStartDelay(delay + 100)
                .setInterpolator(OvershootInterpolator(1.5f))
                .start()
            
            // Animate the arrow icon
            binding.arrowIcon.translationX = -20f
            binding.arrowIcon.alpha = 0f
            binding.arrowIcon.animate()
                .translationX(0f)
                .alpha(1f)
                .setDuration(400)
                .setStartDelay(delay + 400)
                .setInterpolator(OvershootInterpolator(2f))
                .start()
        }
        
        private fun runClickAnimation(onComplete: () -> Unit) {
            val card = binding.categoryCard
            
            // Bounce down
            card.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .setDuration(80)
                .withEndAction {
                    // Bounce back with overshoot
                    card.animate()
                        .scaleX(1.02f)
                        .scaleY(1.02f)
                        .setDuration(100)
                        .setInterpolator(OvershootInterpolator(2f))
                        .withEndAction {
                            // Settle to normal
                            card.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(80)
                                .withEndAction { onComplete() }
                                .start()
                        }
                        .start()
                }
                .start()
        }
    }
}

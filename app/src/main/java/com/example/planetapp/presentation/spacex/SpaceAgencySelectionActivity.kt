package com.example.planetapp.presentation.spacex

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.planetapp.databinding.ActivitySpaceAgencySelectionBinding

class SpaceAgencySelectionActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySpaceAgencySelectionBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpaceAgencySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
        playEntryAnimations()
        startFloatingAnimation()
    }
    
    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        // SpaceX Card - Navigate to SpaceMissionsActivity
        binding.cardSpaceX.setOnClickListener {
            animateCardClick(it as androidx.cardview.widget.CardView) {
                startActivity(Intent(this, SpaceMissionsActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
        
        // NASA Card - Coming Soon
        binding.cardNasa.setOnClickListener {
            animateCardClick(it as androidx.cardview.widget.CardView) {
                Toast.makeText(this, "🚀 NASA missions coming soon!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun animateCardClick(card: androidx.cardview.widget.CardView, onComplete: () -> Unit) {
        card.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                card.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction { onComplete() }
                    .start()
            }
            .start()
    }
    
    private fun playEntryAnimations() {
        // Animate header
        binding.btnBack.alpha = 0f
        binding.btnBack.translationX = -50f
        binding.btnBack.animate()
            .alpha(1f)
            .translationX(0f)
            .setDuration(500)
            .setStartDelay(100)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        // Animate decorative Saturn
        binding.decorSaturn.alpha = 0f
        binding.decorSaturn.scaleX = 0f
        binding.decorSaturn.scaleY = 0f
        binding.decorSaturn.animate()
            .alpha(0.8f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setStartDelay(200)
            .setInterpolator(OvershootInterpolator(2f))
            .start()
        
        // Animate SpaceX card
        binding.cardSpaceX.alpha = 0f
        binding.cardSpaceX.translationY = 100f
        binding.cardSpaceX.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(300)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        // Animate NASA card
        binding.cardNasa.alpha = 0f
        binding.cardNasa.translationY = 100f
        binding.cardNasa.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(450)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
    
    private fun startFloatingAnimation() {
        // Floating animation for decorative Saturn
        val floatUp = ObjectAnimator.ofPropertyValuesHolder(
            binding.decorSaturn,
            PropertyValuesHolder.ofFloat("translationY", 0f, -15f, 0f),
            PropertyValuesHolder.ofFloat("rotation", -5f, 5f, -5f)
        ).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = DecelerateInterpolator()
        }
        floatUp.start()
    }
}

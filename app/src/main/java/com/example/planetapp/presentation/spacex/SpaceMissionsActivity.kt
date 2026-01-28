package com.example.planetapp.presentation.spacex

import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.planetapp.databinding.ActivitySpaceMissionsBinding

class SpaceMissionsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySpaceMissionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Make status bar transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        
        binding = ActivitySpaceMissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
        playEntryAnimations()
        startBackgroundAnimations()
    }
    
    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        
        binding.cardLatestLaunch.setOnClickListener {
            animateCardClick(it) {
                startActivity(Intent(this, LaunchListActivity::class.java).apply {
                    putExtra(LaunchListActivity.EXTRA_LAUNCH_TYPE, LaunchListActivity.TYPE_LATEST)
                })
            }
        }
        
        binding.cardUpcomingLaunches.setOnClickListener {
            animateCardClick(it) {
                startActivity(Intent(this, LaunchListActivity::class.java).apply {
                    putExtra(LaunchListActivity.EXTRA_LAUNCH_TYPE, LaunchListActivity.TYPE_UPCOMING)
                })
            }
        }
        
        binding.cardPastLaunches.setOnClickListener {
            animateCardClick(it) {
                startActivity(Intent(this, LaunchListActivity::class.java).apply {
                    putExtra(LaunchListActivity.EXTRA_LAUNCH_TYPE, LaunchListActivity.TYPE_PAST)
                })
            }
        }
        
        binding.cardCrew.setOnClickListener {
            animateCardClick(it) {
                startActivity(Intent(this, CrewListActivity::class.java))
            }
        }
        
        binding.cardRockets.setOnClickListener {
            animateCardClick(it) {
                startActivity(Intent(this, RocketListActivity::class.java))
            }
        }
    }
    
    private fun animateCardClick(view: android.view.View, action: () -> Unit) {
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction { action() }
                    .start()
            }
            .start()
    }
    
    private fun playEntryAnimations() {
        // Animate back button
        binding.backButton.apply {
            alpha = 0f
            translationX = -30f
            animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(400)
                .setStartDelay(100)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Animate title
        binding.titleText.apply {
            alpha = 0f
            translationY = 20f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(200)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Animate content card
        binding.contentCard.apply {
            alpha = 0f
            translationY = 100f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(300)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Animate decorative rocket
        binding.decorRocket.apply {
            alpha = 0f
            rotation = -15f
            animate()
                .alpha(0.85f)
                .rotation(15f)
                .setDuration(700)
                .setStartDelay(400)
                .setInterpolator(OvershootInterpolator(2f))
                .start()
        }
        
        // Animate cards sequentially
        val cards = listOf(
            binding.cardLatestLaunch,
            binding.cardUpcomingLaunches,
            binding.cardPastLaunches,
            binding.cardCrew,
            binding.cardRockets
        )
        
        cards.forEachIndexed { index, card ->
            card.alpha = 0f
            card.translationX = -50f
            card.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(400)
                .setStartDelay((400 + index * 80).toLong())
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }
    
    private fun startBackgroundAnimations() {
        // Animate orbit paths rotation
        binding.orbitPath1.animate()
            .rotation(360f)
            .setDuration(60000)
            .withEndAction { 
                binding.orbitPath1.rotation = 0f
                startBackgroundAnimations() 
            }
            .start()
        
        binding.orbitPath2.animate()
            .rotation(-360f)
            .setDuration(80000)
            .start()
        
        // Floating rocket animation
        animateDecorRocket()
    }
    
    private fun animateDecorRocket() {
        binding.decorRocket.animate()
            .translationY(-15f)
            .rotation(20f)
            .setDuration(2500)
            .withEndAction {
                binding.decorRocket.animate()
                    .translationY(15f)
                    .rotation(10f)
                    .setDuration(2500)
                    .withEndAction { animateDecorRocket() }
                    .start()
            }
            .start()
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}

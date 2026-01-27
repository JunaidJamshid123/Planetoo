package com.example.planetapp.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.planetapp.databinding.ActivitySplashBinding
import com.example.planetapp.presentation.home.HomeActivity
import com.example.planetapp.utils.Constants

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
        playAnimations()
        
        // Auto-navigate after delay
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToHome()
        }, Constants.SPLASH_DELAY)
    }
    
    private fun setupClickListeners() {
        binding.arrowButton.setOnClickListener {
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction { navigateToHome() }
                        .start()
                }
                .start()
        }
    }
    
    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
    
    private fun playAnimations() {
        // Initial state - hide elements
        listOf(
            binding.satellite,
            binding.moonLarge,
            binding.splashTitle,
            binding.splashSubtitle,
            binding.arrowButton,
            binding.astronaut
        ).forEach { 
            it.alpha = 0f 
        }
        
        // Orbit paths animation
        animateOrbitPaths()
        
        // Saturn floating in
        binding.satellite.apply {
            translationX = -80f
            animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(800)
                .setStartDelay(200)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction { startFloatingAnimation(this) }
                .start()
        }
        
        // Moon appearing
        binding.moonLarge.apply {
            scaleX = 0.5f
            scaleY = 0.5f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .setStartDelay(300)
                .setInterpolator(OvershootInterpolator(1.2f))
                .withEndAction { startFloatingAnimation(this, 2800) }
                .start()
        }
        
        // Title animation
        binding.splashTitle.apply {
            translationX = -150f
            animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(700)
                .setStartDelay(400)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Subtitle animation
        binding.splashSubtitle.apply {
            translationX = -100f
            animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(600)
                .setStartDelay(550)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Arrow button
        binding.arrowButton.apply {
            scaleX = 0f
            scaleY = 0f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400)
                .setStartDelay(700)
                .setInterpolator(OvershootInterpolator(2f))
                .withEndAction { pulseArrowButton() }
                .start()
        }
        
        // Earth - main showcase at bottom
        binding.astronaut.apply {
            translationY = 200f
            scaleX = 0.7f
            scaleY = 0.7f
            animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(900)
                .setStartDelay(500)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction { startFloatingAnimation(this, 3000) }
                .start()
        }
    }
    
    private fun animateOrbitPaths() {
        binding.orbitPath1.animate()
            .rotationBy(360f)
            .setDuration(50000)
            .setInterpolator(LinearInterpolator())
            .withEndAction { animateOrbitPaths() }
            .start()
        
        binding.orbitPath2.animate()
            .rotationBy(-360f)
            .setDuration(40000)
            .setInterpolator(LinearInterpolator())
            .start()
    }
    
    private fun startFloatingAnimation(view: View, duration: Long = 3000) {
        view.animate()
            .translationYBy(-8f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { floatDown(view, duration) }
            .start()
    }
    
    private fun floatDown(view: View, duration: Long) {
        view.animate()
            .translationYBy(8f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { startFloatingAnimation(view, duration) }
            .start()
    }
    
    private fun pulseArrowButton() {
        binding.arrowButton.animate()
            .scaleX(1.08f)
            .scaleY(1.08f)
            .setDuration(700)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                binding.arrowButton.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(700)
                    .withEndAction { pulseArrowButton() }
                    .start()
            }
            .start()
    }
}

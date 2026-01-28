package com.example.planetapp.presentation.spacex

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planetapp.data.remote.api.SpaceXRetrofitClient
import com.example.planetapp.data.remote.model.spacex.Rocket
import com.example.planetapp.databinding.ActivityLaunchListBinding
import com.example.planetapp.presentation.adapter.RocketAdapter
import kotlinx.coroutines.launch

class RocketListActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLaunchListBinding
    private lateinit var adapter: RocketAdapter
    private var loadingAnimatorSet: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        
        binding = ActivityLaunchListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupRecyclerView()
        loadData()
        playEntryAnimations()
    }
    
    private fun setupUI() {
        binding.backButton.setOnClickListener { onBackPressed() }
        binding.retryButton.setOnClickListener { loadData() }
        binding.titleText.text = "Rockets"
    }
    
    private fun setupRecyclerView() {
        adapter = RocketAdapter()
        binding.recyclerView.apply {
            this.adapter = this@RocketListActivity.adapter
            layoutManager = LinearLayoutManager(this@RocketListActivity)
            setHasFixedSize(false)
        }
    }
    
    private fun loadData() {
        showLoading()
        
        lifecycleScope.launch {
            try {
                val response = SpaceXRetrofitClient.spaceXApi.getRockets()
                
                if (response.isSuccessful && response.body() != null) {
                    val rockets = response.body()!!
                    if (rockets.isEmpty()) {
                        showEmpty()
                    } else {
                        showContent(rockets)
                    }
                } else {
                    showError("Failed to load rocket data")
                }
                
            } catch (e: Exception) {
                showError(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    private fun showLoading() {
        binding.loadingLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.subtitleText.text = "Loading..."
        startLoadingAnimation()
    }
    
    private fun startLoadingAnimation() {
        loadingAnimatorSet?.cancel()
        
        val orbitRotate = ObjectAnimator.ofFloat(binding.loadingOrbit, "rotation", 0f, 360f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        
        val rocketPulse = ObjectAnimator.ofPropertyValuesHolder(
            binding.loadingRocket,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.15f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.15f, 1f),
            PropertyValuesHolder.ofFloat("translationY", 0f, -10f, 0f)
        ).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            interpolator = DecelerateInterpolator()
        }
        
        loadingAnimatorSet = AnimatorSet().apply {
            playTogether(orbitRotate, rocketPulse)
            start()
        }
    }
    
    private fun stopLoadingAnimation() {
        loadingAnimatorSet?.cancel()
        loadingAnimatorSet = null
    }
    
    private fun showContent(rockets: List<Rocket>) {
        stopLoadingAnimation()
        binding.loadingLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.emptyLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        
        adapter.submitList(rockets)
        binding.subtitleText.text = "${rockets.size} rocket${if (rockets.size != 1) "s" else ""}"
        
        binding.recyclerView.alpha = 0f
        binding.recyclerView.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }
    
    private fun showEmpty() {
        stopLoadingAnimation()
        binding.loadingLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        binding.subtitleText.text = "No rockets found"
    }
    
    private fun showError(message: String) {
        stopLoadingAnimation()
        binding.loadingLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.errorMessage.text = message
        binding.subtitleText.text = "Error loading data"
    }
    
    private fun playEntryAnimations() {
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
        
        binding.contentCard.apply {
            alpha = 0f
            translationY = 80f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(300)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopLoadingAnimation()
    }
}

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
import com.example.planetapp.data.remote.model.spacex.Launch
import com.example.planetapp.databinding.ActivityLaunchListBinding
import com.example.planetapp.presentation.adapter.LaunchAdapter
import kotlinx.coroutines.launch

class LaunchListActivity : AppCompatActivity() {
    
    companion object {
        const val EXTRA_LAUNCH_TYPE = "launch_type"
        const val TYPE_LATEST = "latest"
        const val TYPE_UPCOMING = "upcoming"
        const val TYPE_PAST = "past"
    }
    
    private lateinit var binding: ActivityLaunchListBinding
    private lateinit var adapter: LaunchAdapter
    private var launchType: String = TYPE_UPCOMING
    private var loadingAnimatorSet: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        
        binding = ActivityLaunchListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        launchType = intent.getStringExtra(EXTRA_LAUNCH_TYPE) ?: TYPE_UPCOMING
        
        setupUI()
        setupRecyclerView()
        loadData()
        playEntryAnimations()
    }
    
    private fun setupUI() {
        binding.backButton.setOnClickListener { onBackPressed() }
        binding.retryButton.setOnClickListener { loadData() }
        
        // Set title based on type
        binding.titleText.text = when (launchType) {
            TYPE_LATEST -> "Latest Launch"
            TYPE_UPCOMING -> "Upcoming Launches"
            TYPE_PAST -> "Past Launches"
            else -> "Launches"
        }
    }
    
    private fun setupRecyclerView() {
        adapter = LaunchAdapter()
        binding.recyclerView.apply {
            this.adapter = this@LaunchListActivity.adapter
            layoutManager = LinearLayoutManager(this@LaunchListActivity)
            setHasFixedSize(false)
        }
    }
    
    private fun loadData() {
        showLoading()
        
        lifecycleScope.launch {
            try {
                val response = when (launchType) {
                    TYPE_LATEST -> {
                        val result = SpaceXRetrofitClient.spaceXApi.getLatestLaunch()
                        if (result.isSuccessful && result.body() != null) {
                            listOf(result.body()!!)
                        } else {
                            emptyList()
                        }
                    }
                    TYPE_UPCOMING -> {
                        val result = SpaceXRetrofitClient.spaceXApi.getUpcomingLaunches()
                        if (result.isSuccessful) {
                            result.body()?.take(20) ?: emptyList()
                        } else {
                            emptyList()
                        }
                    }
                    TYPE_PAST -> {
                        val result = SpaceXRetrofitClient.spaceXApi.getPastLaunches()
                        if (result.isSuccessful) {
                            // Get last 20, reversed so newest first
                            result.body()?.takeLast(20)?.reversed() ?: emptyList()
                        } else {
                            emptyList()
                        }
                    }
                    else -> emptyList()
                }
                
                handleResponse(response)
                
            } catch (e: Exception) {
                showError(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    private fun handleResponse(launches: List<Launch>) {
        if (launches.isEmpty()) {
            showEmpty()
        } else {
            showContent(launches)
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
        // Cancel any existing animation
        loadingAnimatorSet?.cancel()
        
        // Orbit rotation animation
        val orbitRotate = ObjectAnimator.ofFloat(binding.loadingOrbit, "rotation", 0f, 360f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        
        // Rocket pulse and float animation
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
    
    private fun showContent(launches: List<Launch>) {
        stopLoadingAnimation()
        binding.loadingLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.emptyLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        
        adapter.submitList(launches)
        binding.subtitleText.text = "${launches.size} mission${if (launches.size != 1) "s" else ""}"
        
        // Animate recycler view
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
        binding.subtitleText.text = "No missions found"
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

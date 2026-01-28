package com.example.planetapp.presentation.planets

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planetapp.R
import com.example.planetapp.data.repository.PlanetRepositoryImpl
import com.example.planetapp.databinding.ActivityPlanetListBinding
import com.example.planetapp.domain.model.Planet
import com.example.planetapp.presentation.adapter.PlanetAdapter

class PlanetListActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPlanetListBinding
    private lateinit var adapter: PlanetAdapter
    private val repository = PlanetRepositoryImpl()
    private var allPlanets: List<Planet> = emptyList()
    private var currentSortBy: SortOption = SortOption.DISTANCE_ASC
    
    enum class SortOption {
        DISTANCE_ASC, DISTANCE_DESC, NAME_ASC, SIZE_DESC
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Make status bar transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        
        binding = ActivityPlanetListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupSort()
        setupSwipeRefresh()
        
        loadPlanets()
        playEntryAnimations()
        startBackgroundAnimations()
    }
    
    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun setupRecyclerView() {
        adapter = PlanetAdapter()
        binding.planetsRecyclerView.apply {
            this.adapter = this@PlanetListActivity.adapter
            layoutManager = LinearLayoutManager(this@PlanetListActivity)
            setHasFixedSize(false)
        }
    }
    
    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilters()
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun setupSort() {
        binding.sortText.setOnClickListener {
            currentSortBy = when (currentSortBy) {
                SortOption.DISTANCE_ASC -> SortOption.DISTANCE_DESC
                SortOption.DISTANCE_DESC -> SortOption.NAME_ASC
                SortOption.NAME_ASC -> SortOption.SIZE_DESC
                SortOption.SIZE_DESC -> SortOption.DISTANCE_ASC
            }
            
            binding.sortText.text = when (currentSortBy) {
                SortOption.DISTANCE_ASC -> "Sort: Distance ▲"
                SortOption.DISTANCE_DESC -> "Sort: Distance ▼"
                SortOption.NAME_ASC -> "Sort: A-Z"
                SortOption.SIZE_DESC -> "Sort: Size ▼"
            }
            
            // Animate sort text
            binding.sortText.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(100)
                .withEndAction {
                    binding.sortText.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
            
            applyFilters()
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.purple_200, theme)
        )
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(
            resources.getColor(R.color.secondry, theme)
        )
        binding.swipeRefresh.setOnRefreshListener {
            loadPlanets()
        }
    }
    
    private fun loadPlanets() {
        showLoading(true)
        adapter.resetAnimations()
        
        // Simulate network delay for animation effect
        Handler(Looper.getMainLooper()).postDelayed({
            allPlanets = repository.getAllPlanets()
            applyFilters()
            showLoading(false)
            binding.swipeRefresh.isRefreshing = false
            
            // Update subtitle
            val countText = "${allPlanets.size} Celestial Bodies"
            binding.subtitleText.text = countText
            binding.subtitleText.alpha = 0f
            binding.subtitleText.animate().alpha(1f).setDuration(300).start()
            
            // Animate cards appearing
            animateCardsIn()
        }, 800)
    }
    
    private fun applyFilters() {
        val searchQuery = binding.searchEditText.text.toString().trim()
        
        var filtered = allPlanets
        
        // Apply search
        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.type.contains(searchQuery, ignoreCase = true)
            }
        }
        
        // Apply sorting
        filtered = when (currentSortBy) {
            SortOption.DISTANCE_ASC -> filtered.sortedBy { parseDistance(it.distanceFromSun) }
            SortOption.DISTANCE_DESC -> filtered.sortedByDescending { parseDistance(it.distanceFromSun) }
            SortOption.NAME_ASC -> filtered.sortedBy { it.name }
            SortOption.SIZE_DESC -> filtered.sortedByDescending { parseDiameter(it.diameter) }
        }
        
        adapter.submitList(filtered)
        
        // Update count text
        binding.planetCountText.text = if (searchQuery.isEmpty()) {
            "${filtered.size} Celestial Bodies"
        } else {
            "${filtered.size} Results"
        }
        
        // Show/hide empty state
        binding.emptyLayout.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        binding.planetsRecyclerView.visibility = if (filtered.isEmpty()) View.GONE else View.VISIBLE
    }
    
    private fun parseDistance(distance: String): Double {
        return try {
            distance.replace(",", "").replace(" ", "")
                .filter { it.isDigit() || it == '.' }
                .toDoubleOrNull() ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }
    
    private fun parseDiameter(diameter: String): Double {
        return try {
            diameter.replace(",", "").replace(" ", "")
                .filter { it.isDigit() || it == '.' }
                .toDoubleOrNull() ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.loadingLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.planetsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
        
        if (show) {
            startLoadingAnimation()
        }
    }
    
    private fun startLoadingAnimation() {
        // Rotate orbit ring
        val orbitRingRotation = ObjectAnimator.ofFloat(
            binding.loadingOrbitRing, "rotation", 0f, 360f
        ).apply {
            duration = 8000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        
        // Rotate orbiting planet 1
        val planet1Rotation = ObjectAnimator.ofFloat(
            binding.orbitingPlanet1Container, "rotation", 0f, 360f
        ).apply {
            duration = 2500
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        
        // Rotate orbiting planet 2
        val planet2Rotation = ObjectAnimator.ofFloat(
            binding.orbitingPlanet2Container, "rotation", 180f, 540f
        ).apply {
            duration = 3500
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        
        // Pulse center planet
        val centerPulseX = ObjectAnimator.ofFloat(
            binding.loadingCenterPlanet, "scaleX", 1f, 1.15f, 1f
        ).apply {
            duration = 1200
            repeatCount = ObjectAnimator.INFINITE
        }
        
        val centerPulseY = ObjectAnimator.ofFloat(
            binding.loadingCenterPlanet, "scaleY", 1f, 1.15f, 1f
        ).apply {
            duration = 1200
            repeatCount = ObjectAnimator.INFINITE
        }
        
        AnimatorSet().apply {
            playTogether(orbitRingRotation, planet1Rotation, planet2Rotation, centerPulseX, centerPulseY)
            start()
        }
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
        
        // Animate subtitle
        binding.subtitleText.apply {
            alpha = 0f
            translationY = 15f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(450)
                .setStartDelay(300)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Animate header planet icon
        binding.headerPlanetIcon.apply {
            alpha = 0f
            scaleX = 0.5f
            scaleY = 0.5f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setStartDelay(250)
                .setInterpolator(OvershootInterpolator(2f))
                .start()
        }
        
        // Animate content card sliding up
        binding.contentCard.apply {
            alpha = 0f
            translationY = 100f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(350)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Animate decorative planet
        binding.decorPlanet.apply {
            alpha = 0f
            rotation = -30f
            animate()
                .alpha(0.9f)
                .rotation(0f)
                .setDuration(700)
                .setStartDelay(500)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }
    
    private fun animateCardsIn() {
        binding.planetsRecyclerView.apply {
            alpha = 0f
            translationY = 40f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    // Replay layout animation
                    scheduleLayoutAnimation()
                }
                .start()
        }
    }
    
    private fun startBackgroundAnimations() {
        // Animate orbit paths rotation
        binding.orbitPath1.animate()
            .rotation(360f)
            .setDuration(60000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { 
                binding.orbitPath1.rotation = 0f
                startBackgroundAnimations() 
            }
            .start()
        
        binding.orbitPath2.animate()
            .rotation(-360f)
            .setDuration(80000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        
        // Twinkle stars animation
        animateStars()
        
        // Floating decorative planet
        animateDecorPlanet()
    }
    
    private fun animateStars() {
        // Star 1 twinkle
        binding.star1.animate()
            .alpha(0.3f)
            .setDuration(1500)
            .withEndAction {
                binding.star1.animate()
                    .alpha(0.7f)
                    .setDuration(1500)
                    .withEndAction { animateStars() }
                    .start()
            }
            .start()
        
        // Star 2 twinkle with offset
        Handler(Looper.getMainLooper()).postDelayed({
            binding.star2.animate()
                .alpha(0.2f)
                .setDuration(2000)
                .withEndAction {
                    binding.star2.animate()
                        .alpha(0.4f)
                        .setDuration(2000)
                        .start()
                }
                .start()
        }, 500)
    }
    
    private fun animateDecorPlanet() {
        binding.decorPlanet.animate()
            .translationY(10f)
            .rotation(5f)
            .setDuration(3000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                binding.decorPlanet.animate()
                    .translationY(-10f)
                    .rotation(-5f)
                    .setDuration(3000)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction { animateDecorPlanet() }
                    .start()
            }
            .start()
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}

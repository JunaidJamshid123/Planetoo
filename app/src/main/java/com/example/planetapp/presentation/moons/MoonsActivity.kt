package com.example.planetapp.presentation.moons

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planetapp.R
import com.example.planetapp.data.repository.MoonRepository
import com.example.planetapp.databinding.ActivityMoonsBinding
import com.example.planetapp.domain.model.Moon
import com.example.planetapp.presentation.adapter.MoonAdapter
import kotlinx.coroutines.launch

class MoonsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoonsBinding
    private lateinit var adapter: MoonAdapter
    private val repository = MoonRepository()
    
    private var allMoons: List<Moon> = emptyList()
    private var currentSortBy: SortOption = SortOption.SIZE_DESC
    
    // Planet filter from intent
    private var selectedPlanetId: String? = null
    private var selectedPlanetName: String? = null

    companion object {
        const val EXTRA_PLANET_ID = "extra_planet_id"
        const val EXTRA_PLANET_NAME = "extra_planet_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoonsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get planet filter from intent
        selectedPlanetId = intent.getStringExtra(EXTRA_PLANET_ID)
        selectedPlanetName = intent.getStringExtra(EXTRA_PLANET_NAME)
        
        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupSort()
        setupSwipeRefresh()
        
        loadMoons()
        playEntryAnimations()
        startBackgroundAnimations()
    }

    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        
        // Update title and icon based on selected planet
        if (selectedPlanetName != null) {
            binding.titleText.text = "$selectedPlanetName's Moons"
            binding.subtitleText.text = "Natural Satellites"
            
            // Set the correct planet icon
            val planetIconRes = getPlanetIconRes(selectedPlanetName!!)
            binding.headerPlanetIcon.setImageResource(planetIconRes)
        } else {
            binding.titleText.text = "All Moons"
            binding.subtitleText.text = "Natural Satellites"
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

    private fun setupRecyclerView() {
        adapter = MoonAdapter { moon ->
            openMoonDetail(moon)
        }
        
        binding.moonsRecyclerView.apply {
            this.adapter = this@MoonsActivity.adapter
            layoutManager = LinearLayoutManager(this@MoonsActivity)
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
            // Toggle sort option
            currentSortBy = when (currentSortBy) {
                SortOption.SIZE_DESC -> SortOption.SIZE_ASC
                SortOption.SIZE_ASC -> SortOption.NAME_ASC
                SortOption.NAME_ASC -> SortOption.ORBIT_ASC
                SortOption.ORBIT_ASC -> SortOption.SIZE_DESC
            }
            
            binding.sortText.text = when (currentSortBy) {
                SortOption.SIZE_DESC -> "Sort: Size ▼"
                SortOption.SIZE_ASC -> "Sort: Size ▲"
                SortOption.NAME_ASC -> "Sort: A-Z"
                SortOption.ORBIT_ASC -> "Sort: Orbit"
            }
            
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
            adapter.resetAnimations()
            loadMoons()
        }
    }

    private fun loadMoons() {
        showLoading(true)
        adapter.resetAnimations()
        
        lifecycleScope.launch {
            // Fetch only moons for the selected planet if specified
            val result = if (selectedPlanetName != null) {
                repository.getMoonsForPlanet(selectedPlanetName!!)
            } else {
                repository.getAllMoons()
            }
            
            result.onSuccess { moons ->
                allMoons = moons
                applyFilters()
                showLoading(false)
                binding.swipeRefresh.isRefreshing = false
                
                // Update subtitle with count
                val countText = if (moons.size == 1) "1 Natural Satellite" else "${moons.size} Natural Satellites"
                binding.subtitleText.text = countText
                
                // Animate subtitle update
                binding.subtitleText.alpha = 0f
                binding.subtitleText.animate().alpha(1f).setDuration(300).start()
            }
            .onFailure { error ->
                showError(error.message ?: "Failed to load moons")
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun applyFilters() {
        val searchQuery = binding.searchEditText.text.toString().trim()
        
        var filtered = allMoons
        
        // Apply search query
        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.englishName.contains(searchQuery, ignoreCase = true) ||
                it.getParentPlanetDisplayName().contains(searchQuery, ignoreCase = true)
            }
        }
        
        // Apply sorting
        filtered = when (currentSortBy) {
            SortOption.SIZE_DESC -> filtered.sortedByDescending { it.meanRadius }
            SortOption.SIZE_ASC -> filtered.sortedBy { it.meanRadius }
            SortOption.NAME_ASC -> filtered.sortedBy { it.englishName }
            SortOption.ORBIT_ASC -> filtered.sortedBy { it.orbitalPeriod }
        }
        
        adapter.submitList(filtered)
        updateMoonCount(filtered.size)
        
        // Show empty state if no results
        binding.emptyLayout.visibility = if (filtered.isEmpty() && allMoons.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun updateMoonCount(count: Int) {
        val planetText = if (selectedPlanetName != null) " · $selectedPlanetName" else ""
        binding.moonCountText.text = "$count moons$planetText"
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.loadingLayout.visibility = View.VISIBLE
            binding.swipeRefresh.visibility = View.GONE
            binding.errorLayout.visibility = View.GONE
            binding.emptyLayout.visibility = View.GONE
            startLoadingAnimation()
        } else {
            stopLoadingAnimation()
            binding.loadingLayout.visibility = View.GONE
            binding.swipeRefresh.visibility = View.VISIBLE
            // Animate RecyclerView appearing
            binding.moonsRecyclerView.alpha = 0f
            binding.moonsRecyclerView.animate()
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }
    
    private var loadingAnimator1: ObjectAnimator? = null
    private var loadingAnimator2: ObjectAnimator? = null
    private var pulseAnimator: AnimatorSet? = null
    
    private fun startLoadingAnimation() {
        // Find views directly from loadingLayout
        val orbitContainer1 = binding.loadingLayout.findViewById<View>(R.id.orbitingMoon1Container)
        val orbitContainer2 = binding.loadingLayout.findViewById<View>(R.id.orbitingMoon2Container)
        val centerMoon = binding.loadingLayout.findViewById<View>(R.id.loadingCenterMoon)
        val orbitRing = binding.loadingLayout.findViewById<View>(R.id.loadingOrbitRing)
        val loadingText = binding.loadingLayout.findViewById<android.widget.TextView>(R.id.loadingText)
        val loadingSubtext = binding.loadingLayout.findViewById<android.widget.TextView>(R.id.loadingSubtext)
        
        // Animate orbiting moon 1
        orbitContainer1?.let { container ->
            loadingAnimator1 = ObjectAnimator.ofFloat(container, "rotation", 0f, 360f).apply {
                duration = 2000
                repeatCount = ObjectAnimator.INFINITE
                interpolator = android.view.animation.LinearInterpolator()
                start()
            }
        }
        
        // Animate orbiting moon 2 (opposite direction)
        orbitContainer2?.let { container ->
            loadingAnimator2 = ObjectAnimator.ofFloat(container, "rotation", 180f, 540f).apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                interpolator = android.view.animation.LinearInterpolator()
                start()
            }
        }
        
        // Pulse animation for center moon
        centerMoon?.let { moon ->
            val scaleUpX = ObjectAnimator.ofFloat(moon, "scaleX", 1f, 1.15f)
            val scaleUpY = ObjectAnimator.ofFloat(moon, "scaleY", 1f, 1.15f)
            val scaleDownX = ObjectAnimator.ofFloat(moon, "scaleX", 1.15f, 1f)
            val scaleDownY = ObjectAnimator.ofFloat(moon, "scaleY", 1.15f, 1f)
            
            scaleUpX.duration = 800
            scaleUpY.duration = 800
            scaleDownX.duration = 800
            scaleDownY.duration = 800
            
            val scaleUp = AnimatorSet().apply { playTogether(scaleUpX, scaleUpY) }
            val scaleDown = AnimatorSet().apply { playTogether(scaleDownX, scaleDownY) }
            
            pulseAnimator = AnimatorSet().apply {
                playSequentially(scaleUp, scaleDown)
                addListener(object : android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        if (binding.loadingLayout.visibility == View.VISIBLE) {
                            start()
                        }
                    }
                })
                start()
            }
        }
        
        // Animate orbit ring
        orbitRing?.let { ring ->
            ring.animate()
                .alpha(0.5f)
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(1000)
                .withEndAction {
                    ring.animate()
                        .alpha(0.3f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(1000)
                        .start()
                }
                .start()
        }
        
        // Animate loading text with typing effect simulation
        loadingText?.let { text ->
            text.alpha = 0f
            text.translationY = 10f
            text.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay(200)
                .start()
        }
        
        loadingSubtext?.let { subtext ->
            subtext.alpha = 0f
            subtext.animate()
                .alpha(1f)
                .setDuration(400)
                .setStartDelay(400)
                .start()
        }
    }
    
    private fun stopLoadingAnimation() {
        loadingAnimator1?.cancel()
        loadingAnimator2?.cancel()
        pulseAnimator?.cancel()
        loadingAnimator1 = null
        loadingAnimator2 = null
        pulseAnimator = null
    }

    private fun showError(message: String) {
        stopLoadingAnimation()
        binding.loadingLayout.visibility = View.GONE
        binding.swipeRefresh.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
        
        // Animate error layout appearing
        binding.errorLayout.alpha = 0f
        binding.errorLayout.translationY = 30f
        binding.errorLayout.visibility = View.VISIBLE
        binding.errorLayout.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(400)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        binding.errorMessage.text = message
        
        binding.retryButton.setOnClickListener {
            loadMoons()
        }
    }

    private fun openMoonDetail(moon: Moon) {
        val intent = Intent(this, MoonDetailActivity::class.java).apply {
            putExtra(MoonDetailActivity.EXTRA_MOON, moon)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun playEntryAnimations() {
        // Back button animation
        binding.backButton.alpha = 0f
        binding.backButton.translationX = -50f
        binding.backButton.animate()
            .alpha(1f)
            .translationX(0f)
            .setDuration(500)
            .setStartDelay(100)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        // Title animation
        binding.titleText.alpha = 0f
        binding.titleText.translationY = 30f
        binding.titleText.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(150)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        // Subtitle animation
        binding.subtitleText.alpha = 0f
        binding.subtitleText.translationY = 20f
        binding.subtitleText.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(200)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        // Planet icon animation
        binding.headerPlanetIcon.alpha = 0f
        binding.headerPlanetIcon.scaleX = 0f
        binding.headerPlanetIcon.scaleY = 0f
        binding.headerPlanetIcon.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(700)
            .setStartDelay(200)
            .setInterpolator(OvershootInterpolator(1.5f))
            .withEndAction { startPlanetPulse() }
            .start()
        
        // Content card animation
        binding.contentCard.alpha = 0f
        binding.contentCard.translationY = 100f
        binding.contentCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(700)
            .setStartDelay(300)
            .setInterpolator(OvershootInterpolator(0.5f))
            .start()
        
        // Decorative elements
        binding.decorPlanet.alpha = 0f
        binding.decorPlanet.scaleX = 0f
        binding.decorPlanet.scaleY = 0f
        binding.decorPlanet.animate()
            .alpha(0.7f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setStartDelay(400)
            .setInterpolator(OvershootInterpolator(1f))
            .start()
        
        // Stars
        animateStarEntry(binding.star1, 300, 0.5f)
        animateStarEntry(binding.star2, 400, 0.4f)
        
        // Orbit paths
        binding.orbitPath1.alpha = 0f
        binding.orbitPath1.animate().alpha(0.15f).setDuration(1000).setStartDelay(200).start()
        binding.orbitPath2.alpha = 0f
        binding.orbitPath2.animate().alpha(0.1f).setDuration(1000).setStartDelay(300).start()
    }
    
    private fun animateStarEntry(view: View, delay: Long, targetAlpha: Float) {
        view.alpha = 0f
        view.scaleX = 0f
        view.scaleY = 0f
        view.animate()
            .alpha(targetAlpha)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay(delay)
            .setInterpolator(OvershootInterpolator(2f))
            .start()
    }
    
    private fun startBackgroundAnimations() {
        // Floating animation for planet icon
        animatePlanetFloat()
        
        // Floating animation for decorative planet
        animateDecorPlanetFloat()
        
        // Stars twinkling
        animateStarsTwinkle()
        
        // Orbit rotation
        animateOrbitRings()
    }
    
    private fun startPlanetPulse() {
        binding.headerPlanetIcon.animate()
            .scaleX(1.08f)
            .scaleY(1.08f)
            .setDuration(1200)
            .withEndAction {
                binding.headerPlanetIcon.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(1200)
                    .withEndAction { startPlanetPulse() }
                    .start()
            }
            .start()
    }
    
    private fun animatePlanetFloat() {
        val floatUp = ObjectAnimator.ofFloat(binding.headerPlanetIcon, "translationY", 0f, -8f)
        floatUp.duration = 2000
        floatUp.interpolator = AccelerateDecelerateInterpolator()
        
        val floatDown = ObjectAnimator.ofFloat(binding.headerPlanetIcon, "translationY", -8f, 0f)
        floatDown.duration = 2000
        floatDown.interpolator = AccelerateDecelerateInterpolator()
        
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(floatUp, floatDown)
        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                if (!isFinishing) {
                    animatorSet.start()
                }
            }
        })
        animatorSet.startDelay = 1000
        animatorSet.start()
    }
    
    private fun animateDecorPlanetFloat() {
        val floatUp = ObjectAnimator.ofFloat(binding.decorPlanet, "translationY", 0f, -10f)
        floatUp.duration = 2500
        floatUp.interpolator = AccelerateDecelerateInterpolator()
        
        val floatDown = ObjectAnimator.ofFloat(binding.decorPlanet, "translationY", -10f, 0f)
        floatDown.duration = 2500
        floatDown.interpolator = AccelerateDecelerateInterpolator()
        
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(floatUp, floatDown)
        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                if (!isFinishing) {
                    animatorSet.start()
                }
            }
        })
        animatorSet.startDelay = 500
        animatorSet.start()
    }
    
    private fun animateStarsTwinkle() {
        listOf(binding.star1, binding.star2).forEachIndexed { index, star ->
            val twinkle = ObjectAnimator.ofFloat(star, "alpha", star.alpha, star.alpha * 0.3f, star.alpha)
            twinkle.duration = 1500 + (index * 200L)
            twinkle.repeatCount = ObjectAnimator.INFINITE
            twinkle.startDelay = index * 300L
            twinkle.start()
        }
    }
    
    private fun animateOrbitRings() {
        ObjectAnimator.ofFloat(binding.orbitPath1, "rotation", 0f, 360f).apply {
            duration = 60000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = android.view.animation.LinearInterpolator()
            start()
        }
        
        ObjectAnimator.ofFloat(binding.orbitPath2, "rotation", 0f, -360f).apply {
            duration = 80000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = android.view.animation.LinearInterpolator()
            start()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    enum class SortOption {
        SIZE_DESC, SIZE_ASC, NAME_ASC, ORBIT_ASC
    }
}

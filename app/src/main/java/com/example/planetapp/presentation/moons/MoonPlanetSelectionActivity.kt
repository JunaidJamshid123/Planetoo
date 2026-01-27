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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.planetapp.R
import com.example.planetapp.data.local.PlanetMoonDataSource
import com.example.planetapp.databinding.ActivityMoonPlanetSelectionBinding
import com.example.planetapp.domain.model.PlanetType
import com.example.planetapp.domain.model.PlanetWithMoons
import com.example.planetapp.presentation.adapter.PlanetSelectionAdapter

/**
 * Activity for selecting a planet to view its moons
 * This is the first step in the Moons flow with beautiful animations
 */
class MoonPlanetSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoonPlanetSelectionBinding
    private lateinit var adapter: PlanetSelectionAdapter
    
    private var allPlanets: List<PlanetWithMoons> = emptyList()
    private var currentFilter: FilterType = FilterType.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoonPlanetSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupFilters()
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
        adapter = PlanetSelectionAdapter { planet ->
            navigateToMoons(planet)
        }
        
        binding.planetsRecyclerView.apply {
            this.adapter = this@MoonPlanetSelectionActivity.adapter
            layoutManager = GridLayoutManager(this@MoonPlanetSelectionActivity, 2)
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

    private fun setupFilters() {
        binding.filterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            currentFilter = when {
                checkedIds.contains(R.id.chipInner) -> FilterType.INNER
                checkedIds.contains(R.id.chipOuter) -> FilterType.OUTER
                checkedIds.contains(R.id.chipDwarf) -> FilterType.DWARF
                else -> FilterType.ALL
            }
            applyFilters()
        }
    }

    private fun loadPlanets() {
        allPlanets = PlanetMoonDataSource.getAllPlanetsWithMoons()
        adapter.submitList(allPlanets)
    }

    private fun applyFilters() {
        val searchQuery = binding.searchEditText.text?.toString() ?: ""
        
        var filtered = allPlanets
        
        // Apply type filter
        filtered = when (currentFilter) {
            FilterType.ALL -> filtered
            FilterType.INNER -> filtered.filter { it.type == PlanetType.INNER }
            FilterType.OUTER -> filtered.filter { it.type == PlanetType.OUTER }
            FilterType.DWARF -> filtered.filter { it.type == PlanetType.DWARF }
        }
        
        // Apply search filter
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { 
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
            }
        }
        
        adapter.submitList(filtered)
    }

    private fun navigateToMoons(planet: PlanetWithMoons) {
        val intent = Intent(this, MoonsActivity::class.java).apply {
            putExtra(MoonsActivity.EXTRA_PLANET_ID, planet.id)
            putExtra(MoonsActivity.EXTRA_PLANET_NAME, planet.name)
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
        
        // Title label animation
        binding.titleLabel.alpha = 0f
        binding.titleLabel.translationY = 30f
        binding.titleLabel.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(150)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        // Main title animation
        binding.mainTitle.alpha = 0f
        binding.mainTitle.translationY = 40f
        binding.mainTitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(200)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        // Subtitle animation
        binding.subtitle.alpha = 0f
        binding.subtitle.translationY = 30f
        binding.subtitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(250)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        // Content card animation - slide up with overshoot
        binding.contentCard.alpha = 0f
        binding.contentCard.translationY = 100f
        binding.contentCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(700)
            .setStartDelay(300)
            .setInterpolator(OvershootInterpolator(0.5f))
            .start()
        
        // Decorative Saturn animation
        binding.decorSaturn.alpha = 0f
        binding.decorSaturn.scaleX = 0f
        binding.decorSaturn.scaleY = 0f
        binding.decorSaturn.rotation = -30f
        binding.decorSaturn.animate()
            .alpha(0.85f)
            .scaleX(1f)
            .scaleY(1f)
            .rotation(0f)
            .setDuration(800)
            .setStartDelay(400)
            .setInterpolator(OvershootInterpolator(1f))
            .start()
        
        // Decorative Moon animation
        binding.decorMoon.alpha = 0f
        binding.decorMoon.scaleX = 0f
        binding.decorMoon.scaleY = 0f
        binding.decorMoon.animate()
            .alpha(0.5f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(700)
            .setStartDelay(500)
            .setInterpolator(OvershootInterpolator(1.2f))
            .start()
        
        // Stars twinkling animation
        animateStarEntry(binding.star1, 300, 0.6f)
        animateStarEntry(binding.star2, 400, 0.4f)
        animateStarEntry(binding.star3, 500, 0.5f)
        animateStarEntry(binding.star4, 600, 0.35f)
        
        // Dots fade in
        binding.dot1.alpha = 0f
        binding.dot1.animate().alpha(0.4f).setDuration(800).setStartDelay(700).start()
        binding.dot2.alpha = 0f
        binding.dot2.animate().alpha(0.3f).setDuration(800).setStartDelay(800).start()
        binding.dot3.alpha = 0f
        binding.dot3.animate().alpha(0.25f).setDuration(800).setStartDelay(850).start()
        
        // New decorative planets
        binding.decorEarth.alpha = 0f
        binding.decorEarth.scaleX = 0f
        binding.decorEarth.scaleY = 0f
        binding.decorEarth.animate()
            .alpha(0.4f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setStartDelay(600)
            .setInterpolator(OvershootInterpolator(1f))
            .start()
        
        binding.decorJupiter.alpha = 0f
        binding.decorJupiter.scaleX = 0f
        binding.decorJupiter.scaleY = 0f
        binding.decorJupiter.animate()
            .alpha(0.35f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(650)
            .setStartDelay(550)
            .setInterpolator(OvershootInterpolator(1f))
            .start()
        
        // Star 5
        animateStarEntry(binding.star5, 450, 0.5f)
        
        // Orbit paths fade in
        binding.orbitPath1.alpha = 0f
        binding.orbitPath1.animate()
            .alpha(0.2f)
            .setDuration(1000)
            .setStartDelay(200)
            .start()
        
        binding.orbitPath2.alpha = 0f
        binding.orbitPath2.animate()
            .alpha(0.12f)
            .setDuration(1000)
            .setStartDelay(300)
            .start()
        
        binding.orbitPath3.alpha = 0f
        binding.orbitPath3.animate()
            .alpha(0.08f)
            .setDuration(1000)
            .setStartDelay(400)
            .start()
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
        // Floating animation for decorative Saturn
        animateSaturnFloat()
        
        // Floating animation for Moon
        animateMoonFloat()
        
        // Floating animation for new planets
        animateDecorativePlanetsFloat()
        
        // Stars twinkling
        animateStarsTwinkle()
        
        // Subtle rotation for orbit rings
        animateOrbitRings()
    }
    
    private fun animateDecorativePlanetsFloat() {
        // Earth floating
        val earthFloatUp = ObjectAnimator.ofFloat(binding.decorEarth, "translationY", 0f, -6f)
        earthFloatUp.duration = 2200
        earthFloatUp.interpolator = AccelerateDecelerateInterpolator()
        
        val earthFloatDown = ObjectAnimator.ofFloat(binding.decorEarth, "translationY", -6f, 0f)
        earthFloatDown.duration = 2200
        earthFloatDown.interpolator = AccelerateDecelerateInterpolator()
        
        val earthAnimator = AnimatorSet()
        earthAnimator.playSequentially(earthFloatUp, earthFloatDown)
        earthAnimator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                if (!isFinishing) {
                    earthAnimator.start()
                }
            }
        })
        earthAnimator.startDelay = 800
        earthAnimator.start()
        
        // Jupiter floating
        val jupiterFloatUp = ObjectAnimator.ofFloat(binding.decorJupiter, "translationY", 0f, -10f)
        jupiterFloatUp.duration = 2600
        jupiterFloatUp.interpolator = AccelerateDecelerateInterpolator()
        
        val jupiterFloatDown = ObjectAnimator.ofFloat(binding.decorJupiter, "translationY", -10f, 0f)
        jupiterFloatDown.duration = 2600
        jupiterFloatDown.interpolator = AccelerateDecelerateInterpolator()
        
        val jupiterAnimator = AnimatorSet()
        jupiterAnimator.playSequentially(jupiterFloatUp, jupiterFloatDown)
        jupiterAnimator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                if (!isFinishing) {
                    jupiterAnimator.start()
                }
            }
        })
        jupiterAnimator.startDelay = 400
        jupiterAnimator.start()
    }
    
    private fun animateMoonFloat() {
        val floatUp = ObjectAnimator.ofFloat(binding.decorMoon, "translationY", 0f, -8f)
        floatUp.duration = 2000
        floatUp.interpolator = AccelerateDecelerateInterpolator()
        
        val floatDown = ObjectAnimator.ofFloat(binding.decorMoon, "translationY", -8f, 0f)
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
        animatorSet.startDelay = 500
        animatorSet.start()
    }
    
    private fun animateStarsTwinkle() {
        // Twinkle animation for stars
        listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5).forEachIndexed { index, star ->
            val twinkle = ObjectAnimator.ofFloat(star, "alpha", star.alpha, star.alpha * 0.3f, star.alpha)
            twinkle.duration = 1500 + (index * 200L)
            twinkle.repeatCount = ObjectAnimator.INFINITE
            twinkle.startDelay = index * 300L
            twinkle.start()
        }
    }
    
    private fun animateSaturnFloat() {
        val floatUp = ObjectAnimator.ofFloat(binding.decorSaturn, "translationY", 0f, -12f)
        floatUp.duration = 2500
        floatUp.interpolator = AccelerateDecelerateInterpolator()
        
        val floatDown = ObjectAnimator.ofFloat(binding.decorSaturn, "translationY", -12f, 0f)
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
        animatorSet.startDelay = 1000
        animatorSet.start()
    }
    
    private fun animateOrbitRings() {
        // Slow rotation for orbit ring 1
        ObjectAnimator.ofFloat(binding.orbitPath1, "rotation", 0f, 360f).apply {
            duration = 60000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = android.view.animation.LinearInterpolator()
            start()
        }
        
        // Counter rotation for orbit ring 2
        ObjectAnimator.ofFloat(binding.orbitPath2, "rotation", 0f, -360f).apply {
            duration = 80000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = android.view.animation.LinearInterpolator()
            start()
        }
        
        // Orbit ring 3 rotation
        ObjectAnimator.ofFloat(binding.orbitPath3, "rotation", 0f, 360f).apply {
            duration = 100000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = android.view.animation.LinearInterpolator()
            start()
        }
    }

    enum class FilterType {
        ALL, INNER, OUTER, DWARF
    }
}

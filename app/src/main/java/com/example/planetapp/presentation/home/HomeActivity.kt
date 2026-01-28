package com.example.planetapp.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planetapp.data.local.CategoryDataSource
import com.example.planetapp.databinding.ActivityHomeBinding
import com.example.planetapp.domain.model.Category
import com.example.planetapp.presentation.adapter.CategoryAdapter
import com.example.planetapp.presentation.category.CategoryDetailActivity
import com.example.planetapp.presentation.moons.MoonPlanetSelectionActivity
import com.example.planetapp.presentation.planets.PlanetListActivity
import com.example.planetapp.presentation.spacex.SpaceAgencySelectionActivity

class HomeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupCategoriesRecyclerView()
        playEntryAnimations()
    }
    
    private fun setupCategoriesRecyclerView() {
        val categories = CategoryDataSource.getAllCategories()
        
        val adapter = CategoryAdapter(categories) { category ->
            navigateToCategory(category)
        }
        
        binding.categoriesRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
        }
    }
    
    private fun navigateToCategory(category: Category) {
        when (category.id) {
            1 -> {
                // Planets
                startActivity(Intent(this, PlanetListActivity::class.java))
            }
            2 -> {
                // Moons - Navigate to Planet Selection first
                startActivity(Intent(this, MoonPlanetSelectionActivity::class.java))
            }
            3 -> {
                // Space Missions - Agency Selection (SpaceX / NASA)
                startActivity(Intent(this, SpaceAgencySelectionActivity::class.java))
            }
            else -> {
                val intent = Intent(this, CategoryDetailActivity::class.java).apply {
                    putExtra(CategoryDetailActivity.EXTRA_CATEGORY_ID, category.id)
                    putExtra(CategoryDetailActivity.EXTRA_CATEGORY_TITLE, category.title)
                    putExtra(CategoryDetailActivity.EXTRA_CATEGORY_IMAGE, category.image)
                }
                startActivity(intent)
            }
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    
    private fun playEntryAnimations() {
        // Animate header planet icon with bounce
        binding.headerPlanetIcon.scaleX = 0f
        binding.headerPlanetIcon.scaleY = 0f
        binding.headerPlanetIcon.alpha = 0f
        
        binding.headerPlanetIcon.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .rotation(360f)
            .setDuration(1000)
            .setStartDelay(500)
            .setInterpolator(OvershootInterpolator(2f))
            .withEndAction { startContinuousRotation() }
            .start()
        
        // Animate RecyclerView container
        binding.categoriesRecyclerView.alpha = 0f
        binding.categoriesRecyclerView.translationY = 100f
        
        binding.categoriesRecyclerView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(300)
            .setInterpolator(OvershootInterpolator(0.8f))
            .start()
    }
    
    private fun startContinuousRotation() {
        binding.headerPlanetIcon.animate()
            .rotationBy(360f)
            .setDuration(20000)
            .withEndAction { startContinuousRotation() }
            .start()
    }
}

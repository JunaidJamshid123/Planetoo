package com.example.planetapp.presentation.planets

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planetapp.data.repository.PlanetRepositoryImpl
import com.example.planetapp.databinding.ActivityPlanetListBinding
import com.example.planetapp.domain.model.Planet
import com.example.planetapp.presentation.adapter.PlanetAdapter

class PlanetListActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPlanetListBinding
    private lateinit var adapter: PlanetAdapter
    private val repository = PlanetRepositoryImpl()
    private var allPlanets: List<Planet> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanetListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        setupSearch()
        loadPlanets()
        playAnimations()
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
        }
    }
    
    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterPlanets(s.toString())
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun loadPlanets() {
        allPlanets = repository.getAllPlanets()
        adapter.submitList(allPlanets)
        binding.planetCountText.text = "${allPlanets.size} Celestial Bodies"
    }
    
    private fun filterPlanets(query: String) {
        if (query.isEmpty()) {
            adapter.submitList(allPlanets)
            binding.planetCountText.text = "${allPlanets.size} Celestial Bodies"
        } else {
            val filtered = allPlanets.filter { 
                it.name.contains(query, ignoreCase = true) ||
                it.type.contains(query, ignoreCase = true)
            }
            adapter.submitList(filtered)
            binding.planetCountText.text = "${filtered.size} Results"
        }
    }
    
    private fun playAnimations() {
        binding.planetsRecyclerView.alpha = 0f
        binding.planetsRecyclerView.translationY = 30f
        binding.planetsRecyclerView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(200)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}

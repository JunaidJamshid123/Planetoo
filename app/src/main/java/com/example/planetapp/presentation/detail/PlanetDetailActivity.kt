package com.example.planetapp.presentation.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.planetapp.databinding.ActivityPlanetDetailsBinding
import com.example.planetapp.domain.model.Planet
import com.example.planetapp.utils.Constants

class PlanetDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPlanetDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanetDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val planet = intent.getParcelableExtra<Planet>(Constants.EXTRA_PLANET)
        planet?.let { displayPlanetDetails(it) }
    }
    
    private fun displayPlanetDetails(planet: Planet) {
        binding.apply {
            titleInfo.text = planet.name
            distanceInfo.text = "${planet.distanceFromSun} M km"
            gravityInfo.text = "${planet.gravity} m/s²"
            GalaxyInfo.text = planet.galaxy
            imageDetailInfo.setImageResource(planet.image)
            
            // Display additional info in overview
            val detailedOverview = buildString {
                appendLine("📏 Diameter: ${planet.diameter} km")
                appendLine("🌡️ Temperature: ${planet.temperature}")
                appendLine("🌙 Moons: ${planet.moons}")
                appendLine("⏱️ Day Length: ${planet.dayLength}")
                appendLine("📅 Year Length: ${planet.yearLength}")
                appendLine("💨 Atmosphere: ${planet.atmosphere}")
                appendLine()
                appendLine("━━━━━━━━━━━━━━━━━━━━")
                appendLine()
                appendLine(planet.overview)
                appendLine()
                appendLine("━━━━━━━━━━━━━━━━━━━━")
                appendLine()
                appendLine("🌟 FUN FACTS:")
                planet.funFacts.forEachIndexed { index, fact ->
                    appendLine("${index + 1}. $fact")
                }
            }
            Plantetinfodetails.text = detailedOverview
        }
    }
}

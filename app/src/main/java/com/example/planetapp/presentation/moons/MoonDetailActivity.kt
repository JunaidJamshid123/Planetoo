package com.example.planetapp.presentation.moons

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.planetapp.databinding.ActivityMoonDetailBinding
import com.example.planetapp.databinding.ItemPropertyRowBinding
import com.example.planetapp.domain.model.Moon
import java.text.NumberFormat
import java.util.Locale

class MoonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoonDetailBinding
    private var moon: Moon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Make status bar transparent for immersive header
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        
        binding = ActivityMoonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        moon = intent.getParcelableExtra(EXTRA_MOON)
        
        setupToolbar()
        moon?.let { 
            displayMoonData(it) 
            playEntryAnimations()
        } ?: finish()
    }

    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun displayMoonData(moon: Moon) {
        with(binding) {
            // Header info
            moonName.text = moon.englishName
            parentPlanet.text = "Orbits ${moon.getParentPlanetDisplayName()}"
            planetEmoji.text = moon.getPlanetEmoji()
            
            // Quick stats cards
            val formattedRadius = if (moon.meanRadius >= 100) {
                "${NumberFormat.getNumberInstance(Locale.US).format(moon.meanRadius.toInt())} km"
            } else {
                "${String.format(Locale.US, "%.1f", moon.meanRadius)} km"
            }
            radiusValue.text = formattedRadius
            
            gravityValue.text = "${String.format(Locale.US, "%.2f", moon.gravity)} m/s²"
            orbitValue.text = moon.getOrbitalPeriodFormatted()
            
            // Discovery info
            discoveredBy.text = if (moon.discoveredBy.isNotBlank() && moon.discoveredBy != "Unknown") {
                moon.discoveredBy
            } else {
                "Ancient Knowledge"
            }
            
            discoveryDate.text = if (moon.discoveryDate.isNotBlank() && moon.discoveryDate != "Ancient") {
                formatDiscoveryDate(moon.discoveryDate)
            } else {
                "Prehistoric"
            }
            
            // Physical properties
            setProperty(propertyMass, "Mass", moon.mass)
            setProperty(propertyDensity, "Density", "${String.format(Locale.US, "%.2f", moon.density)} g/cm³")
            setProperty(propertyEscape, "Escape Velocity", formatVelocity(moon.escapeVelocity))
            setProperty(propertyTemp, "Avg Temperature", formatTemperature(moon.avgTemp))
            
            // Orbital properties
            setProperty(propertyDistance, "Distance", formatDistance(moon.distanceFromPlanet))
            setProperty(propertyEccentricity, "Eccentricity", String.format(Locale.US, "%.4f", moon.eccentricity))
            setProperty(propertyInclination, "Inclination", "${String.format(Locale.US, "%.2f", moon.inclination)}°")
            setProperty(propertyRotation, "Rotation", formatRotation(moon.rotationPeriod))
            
            // Fun fact
            funFactText.text = generateFunFact(moon)
        }
    }

    private fun setProperty(binding: ItemPropertyRowBinding, label: String, value: String) {
        binding.propertyLabel.text = label
        binding.propertyValue.text = value
    }

    private fun formatDiscoveryDate(date: String): String {
        // Convert DD/MM/YYYY to readable format
        return try {
            val parts = date.split("/")
            if (parts.size == 3) {
                val months = arrayOf("", "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December")
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2]
                "${months[month]} $day, $year"
            } else {
                date
            }
        } catch (e: Exception) {
            date
        }
    }

    private fun formatVelocity(velocity: Double): String {
        return if (velocity > 1000) {
            "${String.format(Locale.US, "%.1f", velocity / 1000)} km/s"
        } else {
            "${String.format(Locale.US, "%.0f", velocity)} m/s"
        }
    }

    private fun formatTemperature(tempKelvin: Int): String {
        return if (tempKelvin > 0) {
            val celsius = tempKelvin - 273
            "$tempKelvin K (${celsius}°C)"
        } else {
            "Unknown"
        }
    }

    private fun formatDistance(distanceKm: Long): String {
        return if (distanceKm >= 1_000_000) {
            "${String.format(Locale.US, "%.2f", distanceKm / 1_000_000.0)} million km"
        } else {
            "${NumberFormat.getNumberInstance(Locale.US).format(distanceKm)} km"
        }
    }

    private fun formatRotation(hours: Double): String {
        return when {
            hours < 24 -> "${String.format(Locale.US, "%.1f", hours)} hours"
            hours < 720 -> "${String.format(Locale.US, "%.1f", hours / 24)} days"
            else -> "${String.format(Locale.US, "%.1f", hours / 720)} months"
        }
    }

    private fun generateFunFact(moon: Moon): String {
        // Generate interesting facts based on moon properties
        val facts = mutableListOf<String>()
        
        when (moon.englishName.lowercase()) {
            "moon" -> facts.add("Earth's Moon is the only celestial body beyond Earth that humans have visited! It's slowly drifting away from us at about 3.8 cm per year.")
            "io" -> facts.add("Io is the most volcanically active body in our solar system! It has over 400 active volcanoes and its surface is constantly being reshaped.")
            "europa" -> facts.add("Europa is thought to have a subsurface ocean that may contain more water than all of Earth's oceans combined! Scientists believe it could potentially harbor life.")
            "ganymede" -> facts.add("Ganymede is the largest moon in the solar system - even bigger than Mercury! It's also the only moon known to have its own magnetic field.")
            "callisto" -> facts.add("Callisto is the most heavily cratered object in our solar system. Its surface is ancient and hasn't changed much in billions of years!")
            "titan" -> facts.add("Titan is the only moon with a thick atmosphere and the only place besides Earth with stable liquid on its surface - though they're lakes of methane and ethane!")
            "enceladus" -> facts.add("Enceladus has geysers that shoot water ice into space! This material forms Saturn's E ring and suggests a liquid ocean beneath its icy crust.")
            "triton" -> facts.add("Triton orbits Neptune backwards (retrograde)! Scientists believe it was captured from the Kuiper Belt. It has active geysers of nitrogen gas.")
            "phobos" -> facts.add("Phobos is slowly spiraling toward Mars and will either crash into the planet or break apart into a ring in about 50 million years!")
            "deimos" -> facts.add("Deimos is so small that if you could jump from its surface, you might escape its gravity entirely!")
            "charon" -> facts.add("Charon is so large compared to Pluto that they actually orbit each other! They're sometimes called a 'double dwarf planet' system.")
            else -> {
                // Generate generic facts based on properties
                if (moon.meanRadius > 1000) {
                    facts.add("${moon.englishName} is one of the largest moons in our solar system, with a radius larger than many dwarf planets!")
                }
                if (moon.orbitalPeriod < 1) {
                    facts.add("${moon.englishName} completes an orbit in less than a day! That's faster than most moons in the solar system.")
                }
                if (moon.gravity < 0.1 && moon.gravity > 0) {
                    facts.add("${moon.englishName} has such weak gravity that an average person could easily jump several meters high!")
                }
                if (moon.discoveredBy.isNotBlank() && moon.discoveredBy != "Unknown") {
                    facts.add("${moon.englishName} was discovered by ${moon.discoveredBy} - adding another member to ${moon.getParentPlanetDisplayName()}'s family of moons!")
                }
            }
        }
        
        return if (facts.isNotEmpty()) {
            facts.random()
        } else {
            "${moon.englishName} is one of ${moon.getParentPlanetDisplayName()}'s moons, silently orbiting through the cosmic void. Each moon has its own unique story to tell!"
        }
    }

    private fun playEntryAnimations() {
        // Animate moon name with fade and slide up
        binding.moonName.apply {
            alpha = 0f
            translationY = 30f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(200)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
        
        // Animate the badge
        binding.parentPlanet.apply {
            alpha = 0f
            scaleX = 0.8f
            scaleY = 0.8f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400)
                .setStartDelay(350)
                .setInterpolator(OvershootInterpolator(1.5f))
                .start()
        }
        
        // Animate fun fact card
        binding.funFactCard.apply {
            alpha = 0f
            translationY = 50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(500)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        const val EXTRA_MOON = "extra_moon"
    }
}

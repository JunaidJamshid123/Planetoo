package com.example.planetapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model for Moon entity
 */
@Parcelize
data class Moon(
    val id: String,
    val name: String,
    val englishName: String,
    val parentPlanet: String,
    val meanRadius: Double,          // km
    val mass: String,                // Formatted mass string
    val density: Double,             // g/cm³
    val gravity: Double,             // m/s²
    val escapeVelocity: Double,      // m/s
    val orbitalPeriod: Double,       // days
    val rotationPeriod: Double,      // hours
    val distanceFromPlanet: Long,    // km
    val eccentricity: Double,
    val inclination: Double,         // degrees
    val discoveredBy: String,
    val discoveryDate: String,
    val avgTemp: Int,                // Kelvin
    val dimension: String,           // For irregular moons
    val axialTilt: Double
) : Parcelable {
    
    /**
     * Get a nice display name for the parent planet
     */
    fun getParentPlanetDisplayName(): String {
        return when (parentPlanet.lowercase()) {
            "terre" -> "Earth"
            "mars" -> "Mars"
            "jupiter" -> "Jupiter"
            "saturne" -> "Saturn"
            "uranus" -> "Uranus"
            "neptune" -> "Neptune"
            "pluton" -> "Pluto"
            else -> parentPlanet.replaceFirstChar { it.uppercase() }
        }
    }
    
    /**
     * Get moon size category
     */
    fun getSizeCategory(): String {
        return when {
            meanRadius > 1000 -> "Giant Moon"
            meanRadius > 500 -> "Large Moon"
            meanRadius > 100 -> "Medium Moon"
            meanRadius > 10 -> "Small Moon"
            else -> "Tiny Moon"
        }
    }
    
    /**
     * Get emoji for parent planet
     */
    fun getPlanetEmoji(): String {
        return when (parentPlanet.lowercase()) {
            "terre" -> "🌍"
            "mars" -> "🔴"
            "jupiter" -> "🟠"
            "saturne" -> "🪐"
            "uranus" -> "🔵"
            "neptune" -> "💙"
            "pluton" -> "⚫"
            else -> "🌑"
        }
    }
    
    /**
     * Format orbital period nicely
     */
    fun getOrbitalPeriodFormatted(): String {
        return when {
            orbitalPeriod < 1 -> "${String.format("%.1f", orbitalPeriod * 24)} hours"
            orbitalPeriod < 30 -> "${String.format("%.2f", orbitalPeriod)} days"
            else -> "${String.format("%.1f", orbitalPeriod / 30)} months"
        }
    }
    
    /**
     * Get color theme based on parent planet
     */
    fun getThemeColor(): Int {
        return when (parentPlanet.lowercase()) {
            "terre" -> 0xFF4FC3F7.toInt()  // Light blue
            "mars" -> 0xFFFF7043.toInt()   // Orange-red
            "jupiter" -> 0xFFFFB74D.toInt() // Amber
            "saturne" -> 0xFFFFD54F.toInt() // Yellow
            "uranus" -> 0xFF81D4FA.toInt()  // Cyan
            "neptune" -> 0xFF7986CB.toInt() // Indigo
            "pluton" -> 0xFF90A4AE.toInt()  // Blue-grey
            else -> 0xFFB0BEC5.toInt()      // Grey
        }
    }
}

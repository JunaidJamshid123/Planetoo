package com.example.planetapp.domain.model

/**
 * Data class representing a planet with its moon count for moon selection screen
 */
data class PlanetWithMoons(
    val id: String,
    val name: String,
    val imageRes: Int,
    val moonCount: Int,
    val description: String,
    val type: PlanetType
)

enum class PlanetType {
    INNER,  // Mercury, Venus, Earth, Mars
    OUTER,  // Jupiter, Saturn, Uranus, Neptune
    DWARF   // Pluto
}

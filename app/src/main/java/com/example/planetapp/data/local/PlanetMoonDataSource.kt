package com.example.planetapp.data.local

import com.example.planetapp.R
import com.example.planetapp.domain.model.PlanetType
import com.example.planetapp.domain.model.PlanetWithMoons

/**
 * Data source for planets with moon information
 * Using high-quality planet images from drawables
 */
object PlanetMoonDataSource {
    
    fun getAllPlanetsWithMoons(): List<PlanetWithMoons> {
        return listOf(
            PlanetWithMoons(
                id = "earth",
                name = "Earth",
                imageRes = R.drawable.planet_earth,
                moonCount = 1,
                description = "Our home planet with one natural satellite - The Moon",
                type = PlanetType.INNER
            ),
            PlanetWithMoons(
                id = "mars",
                name = "Mars",
                imageRes = R.drawable.planet_mars,
                moonCount = 2,
                description = "The Red Planet with two small moons: Phobos and Deimos",
                type = PlanetType.INNER
            ),
            PlanetWithMoons(
                id = "jupiter",
                name = "Jupiter",
                imageRes = R.drawable.planet_jupiter,
                moonCount = 95,
                description = "Gas giant with the most known moons including Io, Europa, Ganymede",
                type = PlanetType.OUTER
            ),
            PlanetWithMoons(
                id = "saturn",
                name = "Saturn",
                imageRes = R.drawable.planet_saturn,
                moonCount = 146,
                description = "Ringed planet with iconic moons like Titan and Enceladus",
                type = PlanetType.OUTER
            ),
            PlanetWithMoons(
                id = "uranus",
                name = "Uranus",
                imageRes = R.drawable.planet_uranus,
                moonCount = 28,
                description = "Ice giant with moons named after Shakespeare characters",
                type = PlanetType.OUTER
            ),
            PlanetWithMoons(
                id = "neptune",
                name = "Neptune",
                imageRes = R.drawable.planet_neptune,
                moonCount = 16,
                description = "Distant ice giant with Triton, its largest moon",
                type = PlanetType.OUTER
            ),
            PlanetWithMoons(
                id = "pluto",
                name = "Pluto",
                imageRes = R.drawable.planet_pluto,
                moonCount = 5,
                description = "Dwarf planet with five known moons including Charon",
                type = PlanetType.DWARF
            )
        )
    }
    
    fun getPlanetsByType(type: PlanetType): List<PlanetWithMoons> {
        return getAllPlanetsWithMoons().filter { it.type == type }
    }
    
    fun searchPlanets(query: String): List<PlanetWithMoons> {
        return getAllPlanetsWithMoons().filter { 
            it.name.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }
}

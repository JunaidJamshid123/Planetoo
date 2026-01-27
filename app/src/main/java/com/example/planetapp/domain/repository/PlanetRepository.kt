package com.example.planetapp.domain.repository

import com.example.planetapp.domain.model.Planet

interface PlanetRepository {
    fun getAllPlanets(): List<Planet>
    fun searchPlanets(query: String): List<Planet>
    fun getPlanetById(id: Int): Planet?
}

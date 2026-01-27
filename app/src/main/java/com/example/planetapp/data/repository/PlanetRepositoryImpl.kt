package com.example.planetapp.data.repository

import com.example.planetapp.data.local.PlanetDataSource
import com.example.planetapp.domain.model.Planet
import com.example.planetapp.domain.repository.PlanetRepository

class PlanetRepositoryImpl : PlanetRepository {
    
    override fun getAllPlanets(): List<Planet> {
        return PlanetDataSource.getAllPlanets()
    }
    
    override fun searchPlanets(query: String): List<Planet> {
        return PlanetDataSource.searchPlanets(query)
    }
    
    override fun getPlanetById(id: Int): Planet? {
        return PlanetDataSource.getAllPlanets().find { it.id == id }
    }
}

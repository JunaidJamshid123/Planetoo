package com.example.planetapp.data.repository

import com.example.planetapp.data.remote.api.RetrofitClient
import com.example.planetapp.data.remote.mapper.MoonMapper
import com.example.planetapp.domain.model.Moon

/**
 * Repository for fetching moon data from API
 */
class MoonRepository {
    
    private val api = RetrofitClient.solarSystemApi
    
    /**
     * Fetch all moons from the Solar System API
     */
    suspend fun getAllMoons(): Result<List<Moon>> {
        return try {
            val response = api.getMoons()
            if (response.isSuccessful && response.body() != null) {
                val moons = MoonMapper.mapToDomainList(response.body()!!.bodies)
                Result.success(moons)
            } else {
                Result.failure(Exception("Failed to fetch moons: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get moons grouped by parent planet
     */
    suspend fun getMoonsGroupedByPlanet(): Result<Map<String, List<Moon>>> {
        return getAllMoons().map { moons ->
            moons.groupBy { it.getParentPlanetDisplayName() }
                .toSortedMap(compareBy { getPlanetOrder(it) })
        }
    }
    
    /**
     * Get moons for a specific planet
     */
    suspend fun getMoonsForPlanet(planet: String): Result<List<Moon>> {
        return getAllMoons().map { moons ->
            moons.filter { it.getParentPlanetDisplayName().equals(planet, ignoreCase = true) }
        }
    }
    
    private fun getPlanetOrder(planet: String): Int {
        return when (planet.lowercase()) {
            "earth" -> 1
            "mars" -> 2
            "jupiter" -> 3
            "saturn" -> 4
            "uranus" -> 5
            "neptune" -> 6
            "pluto" -> 7
            else -> 8
        }
    }
}

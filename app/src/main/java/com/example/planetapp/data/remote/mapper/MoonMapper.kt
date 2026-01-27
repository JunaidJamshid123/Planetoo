package com.example.planetapp.data.remote.mapper

import com.example.planetapp.data.remote.model.MoonResponse
import com.example.planetapp.domain.model.Moon

/**
 * Mapper to convert API response to domain model
 */
object MoonMapper {
    
    fun mapToDomain(response: MoonResponse): Moon {
        return Moon(
            id = response.id,
            name = response.name,
            englishName = response.englishName,
            parentPlanet = response.aroundPlanet?.planet ?: "Unknown",
            meanRadius = response.meanRadius,
            mass = formatMass(response.mass?.massValue, response.mass?.massExponent),
            density = response.density,
            gravity = response.gravity,
            escapeVelocity = response.escapeVelocity,
            orbitalPeriod = response.orbitalPeriod,
            rotationPeriod = response.rotationPeriod,
            distanceFromPlanet = response.semimajorAxis,
            eccentricity = response.eccentricity,
            inclination = response.inclination,
            discoveredBy = response.discoveredBy ?: "Unknown",
            discoveryDate = response.discoveryDate ?: "Ancient",
            avgTemp = response.avgTemp,
            dimension = response.dimension ?: "",
            axialTilt = response.axialTilt
        )
    }
    
    fun mapToDomainList(responses: List<MoonResponse>): List<Moon> {
        return responses.map { mapToDomain(it) }
    }
    
    private fun formatMass(value: Double?, exponent: Int?): String {
        if (value == null || exponent == null) return "Unknown"
        return "${String.format("%.2f", value)} × 10^$exponent kg"
    }
}

package com.example.planetapp.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * API Response wrapper for moons list
 */
data class MoonListResponse(
    @SerializedName("bodies")
    val bodies: List<MoonResponse>
)

/**
 * Moon data from Solar System API
 */
data class MoonResponse(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("englishName")
    val englishName: String,
    
    @SerializedName("isPlanet")
    val isPlanet: Boolean,
    
    @SerializedName("semimajorAxis")
    val semimajorAxis: Long,
    
    @SerializedName("perihelion")
    val perihelion: Long,
    
    @SerializedName("aphelion")
    val aphelion: Long,
    
    @SerializedName("eccentricity")
    val eccentricity: Double,
    
    @SerializedName("inclination")
    val inclination: Double,
    
    @SerializedName("mass")
    val mass: MassResponse?,
    
    @SerializedName("vol")
    val volume: VolumeResponse?,
    
    @SerializedName("density")
    val density: Double,
    
    @SerializedName("gravity")
    val gravity: Double,
    
    @SerializedName("escape")
    val escapeVelocity: Double,
    
    @SerializedName("meanRadius")
    val meanRadius: Double,
    
    @SerializedName("equaRadius")
    val equatorialRadius: Double,
    
    @SerializedName("polarRadius")
    val polarRadius: Double,
    
    @SerializedName("dimension")
    val dimension: String?,
    
    @SerializedName("sideralOrbit")
    val orbitalPeriod: Double,
    
    @SerializedName("sideralRotation")
    val rotationPeriod: Double,
    
    @SerializedName("aroundPlanet")
    val aroundPlanet: AroundPlanetResponse?,
    
    @SerializedName("discoveredBy")
    val discoveredBy: String?,
    
    @SerializedName("discoveryDate")
    val discoveryDate: String?,
    
    @SerializedName("axialTilt")
    val axialTilt: Double,
    
    @SerializedName("avgTemp")
    val avgTemp: Int,
    
    @SerializedName("bodyType")
    val bodyType: String
)

data class MassResponse(
    @SerializedName("massValue")
    val massValue: Double,
    
    @SerializedName("massExponent")
    val massExponent: Int
)

data class VolumeResponse(
    @SerializedName("volValue")
    val volValue: Double,
    
    @SerializedName("volExponent")
    val volExponent: Int
)

data class AroundPlanetResponse(
    @SerializedName("planet")
    val planet: String,
    
    @SerializedName("rel")
    val rel: String
)

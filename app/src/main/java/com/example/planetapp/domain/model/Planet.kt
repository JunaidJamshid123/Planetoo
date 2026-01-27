package com.example.planetapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Planet(
    val id: Int,
    val name: String,
    val type: String,           // Terrestrial, Gas Giant, Ice Giant, Star, Dwarf Planet
    val image: Int,
    val galaxy: String,
    val distanceFromSun: String, // in million km
    val gravity: String,         // m/s²
    val overview: String,
    
    // New fields
    val diameter: String,        // in km
    val orbitalPeriod: String,   // in Earth days/years
    val moons: Int,              // number of moons
    val temperature: String,     // average temperature
    val dayLength: String,       // rotation period
    val yearLength: String,      // orbital period in Earth years
    val atmosphere: String,      // main atmospheric components
    val funFacts: List<String>   // interesting facts
) : Parcelable

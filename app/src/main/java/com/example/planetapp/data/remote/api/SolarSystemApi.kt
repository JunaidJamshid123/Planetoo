package com.example.planetapp.data.remote.api

import com.example.planetapp.data.remote.model.MoonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Solar System OpenData API Interface
 * Base URL: https://api.le-systeme-solaire.net
 * 
 * This is a FREE PUBLIC API - NO authentication required!
 */
interface SolarSystemApi {
    
    /**
     * Get all moons from the Solar System
     * Using encoded=true to prevent double encoding of filter[]
     */
    @GET("rest/bodies?filter[]=bodyType,eq,Moon")
    suspend fun getMoons(): Response<MoonListResponse>
    
    /**
     * Get all planets
     */
    @GET("rest/bodies")
    suspend fun getPlanets(
        @Query("filter[]") filter: String = "isPlanet,eq,true"
    ): Response<MoonListResponse>
    
    /**
     * Get all asteroids
     */
    @GET("rest/bodies")
    suspend fun getAsteroids(
        @Query("filter[]") filter: String = "bodyType,eq,Asteroid"
    ): Response<MoonListResponse>
    
    /**
     * Get all comets
     */
    @GET("rest/bodies")
    suspend fun getComets(
        @Query("filter[]") filter: String = "bodyType,eq,Comet"
    ): Response<MoonListResponse>
    
    companion object {
        const val BASE_URL = "https://api.le-systeme-solaire.net/"
    }
}

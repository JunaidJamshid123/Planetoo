package com.example.planetapp.data.remote.api

import com.example.planetapp.data.remote.model.spacex.CrewMember
import com.example.planetapp.data.remote.model.spacex.Launch
import com.example.planetapp.data.remote.model.spacex.Launchpad
import com.example.planetapp.data.remote.model.spacex.Rocket
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * SpaceX API Interface
 * Base URL: https://api.spacexdata.com
 * 
 * This is a FREE PUBLIC API - NO authentication required!
 */
interface SpaceXApi {
    
    /**
     * Get the latest launch
     */
    @GET("v5/launches/latest")
    suspend fun getLatestLaunch(): Response<Launch>
    
    /**
     * Get upcoming launches
     */
    @GET("v5/launches/upcoming")
    suspend fun getUpcomingLaunches(): Response<List<Launch>>
    
    /**
     * Get past launches (limited to recent ones)
     */
    @GET("v5/launches/past")
    suspend fun getPastLaunches(): Response<List<Launch>>
    
    /**
     * Get a single launch by ID
     */
    @GET("v5/launches/{id}")
    suspend fun getLaunchById(@Path("id") launchId: String): Response<Launch>
    
    /**
     * Get all crew members
     */
    @GET("v4/crew")
    suspend fun getCrew(): Response<List<CrewMember>>
    
    /**
     * Get a single crew member by ID
     */
    @GET("v4/crew/{id}")
    suspend fun getCrewById(@Path("id") crewId: String): Response<CrewMember>
    
    /**
     * Get all rockets
     */
    @GET("v4/rockets")
    suspend fun getRockets(): Response<List<Rocket>>
    
    /**
     * Get a single rocket by ID
     */
    @GET("v4/rockets/{id}")
    suspend fun getRocketById(@Path("id") rocketId: String): Response<Rocket>
    
    /**
     * Get all launchpads
     */
    @GET("v4/launchpads")
    suspend fun getLaunchpads(): Response<List<Launchpad>>
    
    /**
     * Get a single launchpad by ID
     */
    @GET("v4/launchpads/{id}")
    suspend fun getLaunchpadById(@Path("id") launchpadId: String): Response<Launchpad>
    
    companion object {
        const val BASE_URL = "https://api.spacexdata.com/"
    }
}

package com.example.planetapp.data.remote.model.spacex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Launchpad(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String?,
    
    @SerializedName("full_name")
    val fullName: String?,
    
    @SerializedName("status")
    val status: String?,
    
    @SerializedName("locality")
    val locality: String?,
    
    @SerializedName("region")
    val region: String?,
    
    @SerializedName("timezone")
    val timezone: String?,
    
    @SerializedName("latitude")
    val latitude: Double?,
    
    @SerializedName("longitude")
    val longitude: Double?,
    
    @SerializedName("launch_attempts")
    val launchAttempts: Int?,
    
    @SerializedName("launch_successes")
    val launchSuccesses: Int?,
    
    @SerializedName("rockets")
    val rockets: List<String>?,
    
    @SerializedName("launches")
    val launches: List<String>?,
    
    @SerializedName("details")
    val details: String?,
    
    @SerializedName("images")
    val images: LaunchpadImages?
) : Parcelable

@Parcelize
data class LaunchpadImages(
    @SerializedName("large")
    val large: List<String>?
) : Parcelable

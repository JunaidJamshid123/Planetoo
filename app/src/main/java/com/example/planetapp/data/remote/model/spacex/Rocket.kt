package com.example.planetapp.data.remote.model.spacex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rocket(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String?,
    
    @SerializedName("active")
    val active: Boolean,
    
    @SerializedName("stages")
    val stages: Int?,
    
    @SerializedName("boosters")
    val boosters: Int?,
    
    @SerializedName("cost_per_launch")
    val costPerLaunch: Long?,
    
    @SerializedName("success_rate_pct")
    val successRatePct: Int?,
    
    @SerializedName("first_flight")
    val firstFlight: String?,
    
    @SerializedName("country")
    val country: String?,
    
    @SerializedName("company")
    val company: String?,
    
    @SerializedName("height")
    val height: Dimension?,
    
    @SerializedName("diameter")
    val diameter: Dimension?,
    
    @SerializedName("mass")
    val mass: Mass?,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("wikipedia")
    val wikipedia: String?,
    
    @SerializedName("flickr_images")
    val flickrImages: List<String>?
) : Parcelable

@Parcelize
data class Dimension(
    @SerializedName("meters")
    val meters: Double?,
    
    @SerializedName("feet")
    val feet: Double?
) : Parcelable

@Parcelize
data class Mass(
    @SerializedName("kg")
    val kg: Long?,
    
    @SerializedName("lb")
    val lb: Long?
) : Parcelable

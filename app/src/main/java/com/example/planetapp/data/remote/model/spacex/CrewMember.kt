package com.example.planetapp.data.remote.model.spacex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrewMember(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("agency")
    val agency: String?,
    
    @SerializedName("image")
    val image: String?,
    
    @SerializedName("wikipedia")
    val wikipedia: String?,
    
    @SerializedName("launches")
    val launches: List<String>?,
    
    @SerializedName("status")
    val status: String?
) : Parcelable

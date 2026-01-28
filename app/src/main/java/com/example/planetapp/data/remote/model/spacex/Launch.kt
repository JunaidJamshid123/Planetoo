package com.example.planetapp.data.remote.model.spacex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Launch(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("flight_number")
    val flightNumber: Int,
    
    @SerializedName("date_utc")
    val dateUtc: String,
    
    @SerializedName("date_local")
    val dateLocal: String?,
    
    @SerializedName("date_precision")
    val datePrecision: String?,
    
    @SerializedName("details")
    val details: String?,
    
    @SerializedName("success")
    val success: Boolean?,
    
    @SerializedName("upcoming")
    val upcoming: Boolean,
    
    @SerializedName("rocket")
    val rocketId: String?,
    
    @SerializedName("launchpad")
    val launchpadId: String?,
    
    @SerializedName("crew")
    val crew: List<LaunchCrew>?,
    
    @SerializedName("payloads")
    val payloadIds: List<String>?,
    
    @SerializedName("capsules")
    val capsuleIds: List<String>?,
    
    @SerializedName("cores")
    val cores: List<LaunchCore>?,
    
    @SerializedName("links")
    val links: LaunchLinks?,
    
    @SerializedName("failures")
    val failures: List<Failure>?,
    
    @SerializedName("static_fire_date_utc")
    val staticFireDateUtc: String?,
    
    @SerializedName("window")
    val launchWindow: Int?,
    
    @SerializedName("auto_update")
    val autoUpdate: Boolean?
) : Parcelable

@Parcelize
data class LaunchCrew(
    @SerializedName("crew")
    val crewId: String?,
    
    @SerializedName("role")
    val role: String?
) : Parcelable

@Parcelize
data class LaunchCore(
    @SerializedName("core")
    val coreId: String?,
    
    @SerializedName("flight")
    val flight: Int?,
    
    @SerializedName("gridfins")
    val gridfins: Boolean?,
    
    @SerializedName("legs")
    val legs: Boolean?,
    
    @SerializedName("reused")
    val reused: Boolean?,
    
    @SerializedName("landing_attempt")
    val landingAttempt: Boolean?,
    
    @SerializedName("landing_success")
    val landingSuccess: Boolean?,
    
    @SerializedName("landing_type")
    val landingType: String?,
    
    @SerializedName("landpad")
    val landpadId: String?
) : Parcelable

@Parcelize
data class LaunchLinks(
    @SerializedName("patch")
    val patch: Patch?,
    
    @SerializedName("reddit")
    val reddit: RedditLinks?,
    
    @SerializedName("flickr")
    val flickr: FlickrLinks?,
    
    @SerializedName("presskit")
    val presskit: String?,
    
    @SerializedName("webcast")
    val webcast: String?,
    
    @SerializedName("youtube_id")
    val youtubeId: String?,
    
    @SerializedName("article")
    val article: String?,
    
    @SerializedName("wikipedia")
    val wikipedia: String?
) : Parcelable

@Parcelize
data class RedditLinks(
    @SerializedName("campaign")
    val campaign: String?,
    
    @SerializedName("launch")
    val launch: String?,
    
    @SerializedName("media")
    val media: String?,
    
    @SerializedName("recovery")
    val recovery: String?
) : Parcelable

@Parcelize
data class FlickrLinks(
    @SerializedName("small")
    val small: List<String>?,
    
    @SerializedName("original")
    val original: List<String>?
) : Parcelable

@Parcelize
data class Patch(
    @SerializedName("small")
    val small: String?,
    
    @SerializedName("large")
    val large: String?
) : Parcelable

@Parcelize
data class Failure(
    @SerializedName("time")
    val time: Int?,
    
    @SerializedName("altitude")
    val altitude: Int?,
    
    @SerializedName("reason")
    val reason: String?
) : Parcelable

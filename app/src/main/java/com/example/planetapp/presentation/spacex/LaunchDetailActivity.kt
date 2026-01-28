package com.example.planetapp.presentation.spacex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.planetapp.R
import com.example.planetapp.data.remote.api.SpaceXRetrofitClient
import com.example.planetapp.data.remote.model.spacex.Launch
import com.example.planetapp.data.remote.model.spacex.Launchpad
import com.example.planetapp.data.remote.model.spacex.Rocket
import com.example.planetapp.databinding.ActivityLaunchDetailBinding
import com.example.planetapp.presentation.adapter.GalleryAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LaunchDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLaunchDetailBinding
    private val api = SpaceXRetrofitClient.spaceXApi
    
    private var rocket: Rocket? = null
    private var launchpad: Launchpad? = null
    
    companion object {
        const val EXTRA_LAUNCH = "extra_launch"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val launch = intent.getParcelableExtra<Launch>(EXTRA_LAUNCH)
        
        if (launch == null) {
            finish()
            return
        }
        
        setupToolbar(launch)
        displayLaunchInfo(launch)
        loadAdditionalData(launch)
        playEntryAnimations()
    }
    
    private fun setupToolbar(launch: Launch) {
        binding.toolbar.setNavigationOnClickListener { 
            onBackPressedDispatcher.onBackPressed()
        }
        binding.collapsingToolbar.title = launch.name
    }
    
    private fun displayLaunchInfo(launch: Launch) {
        binding.apply {
            // Mission name and flight number
            missionName.text = launch.name
            flightNumber.text = "Flight #${launch.flightNumber}"
            
            // Status badge
            when {
                launch.upcoming -> {
                    statusBadge.text = "UPCOMING"
                    statusBadge.setBackgroundResource(R.drawable.badge_size_medium)
                }
                launch.success == true -> {
                    statusBadge.text = "SUCCESS"
                    statusBadge.setBackgroundResource(R.drawable.badge_size_large)
                }
                launch.success == false -> {
                    statusBadge.text = "FAILED"
                    statusBadge.setBackgroundResource(R.drawable.badge_size_small)
                }
                else -> {
                    statusBadge.text = "TBD"
                    statusBadge.setBackgroundResource(R.drawable.badge_size_medium)
                }
            }
            
            // Mission patch
            val patchUrl = launch.links?.patch?.large ?: launch.links?.patch?.small
            if (!patchUrl.isNullOrEmpty()) {
                missionPatch.load(patchUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_astronaut)
                    error(R.drawable.ic_astronaut)
                }
            }
            
            // Date & Time
            launchDate.text = formatDateFull(launch.dateUtc)
            launch.dateLocal?.let {
                launchDateLocal.text = "Local: ${formatDateLocal(it)}"
                launchDateLocal.visibility = View.VISIBLE
            } ?: run {
                launchDateLocal.visibility = View.GONE
            }
            
            // Mission details
            if (!launch.details.isNullOrEmpty()) {
                missionDetails.text = launch.details
                detailsCard.visibility = View.VISIBLE
            } else {
                detailsCard.visibility = View.GONE
            }
            
            // Failure details
            if (launch.success == false && !launch.failures.isNullOrEmpty()) {
                val failure = launch.failures.first()
                failureReason.text = failure.reason ?: "Unknown failure"
                
                val failureInfo = buildString {
                    failure.time?.let { append("Time: T+${it}s") }
                    failure.altitude?.let { 
                        if (isNotEmpty()) append(" • ")
                        append("Altitude: ${it}km") 
                    }
                }
                if (failureInfo.isNotEmpty()) {
                    failureTime.text = failureInfo
                    failureTime.visibility = View.VISIBLE
                } else {
                    failureTime.visibility = View.GONE
                }
                
                failureCard.visibility = View.VISIBLE
            } else {
                failureCard.visibility = View.GONE
            }
            
            // Core/Booster info
            launch.cores?.firstOrNull()?.let { core ->
                coreCard.visibility = View.VISIBLE
                
                // Landing attempt
                if (core.landingAttempt == true) {
                    landingAttempt.text = "Yes"
                    landingAttemptIcon.text = "🎯"
                } else {
                    landingAttempt.text = "No"
                    landingAttemptIcon.text = "❌"
                }
                
                // Landing success
                when (core.landingSuccess) {
                    true -> {
                        landingSuccess.text = "Success"
                        landingSuccessIcon.text = "✅"
                    }
                    false -> {
                        landingSuccess.text = "Failed"
                        landingSuccessIcon.text = "❌"
                    }
                    null -> {
                        landingSuccess.text = "N/A"
                        landingSuccessIcon.text = "❓"
                    }
                }
                
                // Landing type
                landingType.text = core.landingType ?: "N/A"
                
                // Core flight number
                coreFlight.text = core.flight?.let { "#$it" } ?: "New"
                
            } ?: run {
                coreCard.visibility = View.GONE
            }
            
            // Gallery
            val galleryImages = launch.links?.flickr?.original ?: launch.links?.flickr?.small ?: emptyList()
            if (galleryImages.isNotEmpty()) {
                galleryCard.visibility = View.VISIBLE
                galleryRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@LaunchDetailActivity, LinearLayoutManager.HORIZONTAL, false)
                    adapter = GalleryAdapter(galleryImages)
                }
            } else {
                galleryCard.visibility = View.GONE
            }
            
            // Links
            setupLinks(launch)
        }
    }
    
    private fun setupLinks(launch: Launch) {
        binding.apply {
            // Video link
            val videoUrl = launch.links?.webcast ?: launch.links?.youtubeId?.let { "https://www.youtube.com/watch?v=$it" }
            if (!videoUrl.isNullOrEmpty()) {
                btnWatchVideo.visibility = View.VISIBLE
                btnWatchVideo.setOnClickListener {
                    openUrl(videoUrl)
                }
            } else {
                btnWatchVideo.visibility = View.GONE
            }
            
            // Wikipedia link
            if (!launch.links?.wikipedia.isNullOrEmpty()) {
                btnWikipedia.visibility = View.VISIBLE
                btnWikipedia.setOnClickListener {
                    openUrl(launch.links?.wikipedia!!)
                }
            } else {
                btnWikipedia.visibility = View.GONE
            }
            
            // Article link
            if (!launch.links?.article.isNullOrEmpty()) {
                btnArticle.visibility = View.VISIBLE
                btnArticle.setOnClickListener {
                    openUrl(launch.links?.article!!)
                }
            } else {
                btnArticle.visibility = View.GONE
            }
            
            // Reddit links
            val reddit = launch.links?.reddit
            var hasRedditLinks = false
            
            if (!reddit?.campaign.isNullOrEmpty()) {
                btnRedditCampaign.visibility = View.VISIBLE
                btnRedditCampaign.setOnClickListener { openUrl(reddit?.campaign!!) }
                hasRedditLinks = true
            } else {
                btnRedditCampaign.visibility = View.GONE
            }
            
            if (!reddit?.launch.isNullOrEmpty()) {
                btnRedditLaunch.visibility = View.VISIBLE
                btnRedditLaunch.setOnClickListener { openUrl(reddit?.launch!!) }
                hasRedditLinks = true
            } else {
                btnRedditLaunch.visibility = View.GONE
            }
            
            if (!reddit?.media.isNullOrEmpty()) {
                btnRedditMedia.visibility = View.VISIBLE
                btnRedditMedia.setOnClickListener { openUrl(reddit?.media!!) }
                hasRedditLinks = true
            } else {
                btnRedditMedia.visibility = View.GONE
            }
            
            redditLinksLayout.visibility = if (hasRedditLinks) View.VISIBLE else View.GONE
        }
    }
    
    private fun loadAdditionalData(launch: Launch) {
        binding.loadingOverlay.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                // Load rocket info
                launch.rocketId?.let { rocketId ->
                    try {
                        val response = api.getRocketById(rocketId)
                        if (response.isSuccessful) {
                            rocket = response.body()
                            displayRocketInfo(rocket)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                
                // Load launchpad info
                launch.launchpadId?.let { launchpadId ->
                    try {
                        val response = api.getLaunchpadById(launchpadId)
                        if (response.isSuccessful) {
                            launchpad = response.body()
                            displayLaunchpadInfo(launchpad)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } finally {
                binding.loadingOverlay.visibility = View.GONE
            }
        }
    }
    
    private fun displayRocketInfo(rocket: Rocket?) {
        rocket ?: return
        
        binding.apply {
            rocketName.text = rocket.name
            
            val stats = buildString {
                rocket.height?.meters?.let { append("Height: ${it.toInt()}m") }
                rocket.successRatePct?.let {
                    if (isNotEmpty()) append(" • ")
                    append("Success: $it%")
                }
            }
            rocketStats.text = stats
            
            // Load rocket image
            rocket.flickrImages?.firstOrNull()?.let { imageUrl ->
                rocketImage.load(imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_satellite)
                    error(R.drawable.ic_satellite)
                }
            }
            
            rocketCard.visibility = View.VISIBLE
        }
    }
    
    private fun displayLaunchpadInfo(launchpad: Launchpad?) {
        launchpad ?: return
        
        binding.apply {
            launchpadName.text = launchpad.fullName ?: launchpad.name ?: "Unknown"
            
            val location = buildString {
                launchpad.locality?.let { append(it) }
                launchpad.region?.let {
                    if (isNotEmpty()) append(", ")
                    append(it)
                }
            }
            launchpadLocation.text = location.ifEmpty { "Unknown location" }
            
            val stats = buildString {
                launchpad.launchAttempts?.let { append("$it launches") }
                launchpad.launchSuccesses?.let {
                    if (isNotEmpty()) append(" • ")
                    append("$it successes")
                }
            }
            launchpadStats.text = stats
            
            launchpadCard.visibility = View.VISIBLE
        }
    }
    
    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun formatDateFull(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)
            
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy 'at' h:mm a 'UTC'", Locale.US)
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
            outputFormat.format(date!!)
        } catch (e: Exception) {
            try {
                // Try without milliseconds
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(dateString)
                
                val outputFormat = SimpleDateFormat("MMMM dd, yyyy 'at' h:mm a 'UTC'", Locale.US)
                outputFormat.timeZone = TimeZone.getTimeZone("UTC")
                outputFormat.format(date!!)
            } catch (e2: Exception) {
                dateString.take(10)
            }
        }
    }
    
    private fun formatDateLocal(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
            val date = inputFormat.parse(dateString)
            
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy 'at' h:mm a", Locale.US)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }
    
    private fun playEntryAnimations() {
        // Animate content
        binding.apply {
            val views = listOf(missionName, flightNumber, statusBadge)
            views.forEachIndexed { index, view ->
                view.alpha = 0f
                view.translationY = 30f
                view.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .setStartDelay((index * 100 + 300).toLong())
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            }
        }
    }
}

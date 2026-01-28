package com.example.planetapp.presentation.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.planetapp.R
import com.example.planetapp.data.remote.model.spacex.Launch
import com.example.planetapp.databinding.ItemLaunchBinding
import com.example.planetapp.presentation.spacex.LaunchDetailActivity
import java.text.SimpleDateFormat
import java.util.*

class LaunchAdapter : ListAdapter<Launch, LaunchAdapter.LaunchViewHolder>(LaunchDiffCallback()) {
    
    private var lastAnimatedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        val binding = ItemLaunchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaunchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bind(getItem(position))
        
        if (position > lastAnimatedPosition) {
            animateItem(holder, position)
            lastAnimatedPosition = position
        }
    }
    
    private fun animateItem(holder: LaunchViewHolder, position: Int) {
        holder.itemView.apply {
            alpha = 0f
            translationY = 30f
            
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(350)
                .setStartDelay((position * 50).toLong())
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    class LaunchViewHolder(private val binding: ItemLaunchBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(launch: Launch) {
            binding.apply {
                missionName.text = launch.name
                flightNumber.text = "Flight #${launch.flightNumber}"
                
                // Format date
                launchDate.text = formatDate(launch.dateUtc)
                
                // Load mission patch
                val patchUrl = launch.links?.patch?.small
                if (!patchUrl.isNullOrEmpty()) {
                    missionPatch.load(patchUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_astronaut)
                        error(R.drawable.ic_astronaut)
                    }
                } else {
                    missionPatch.setImageResource(R.drawable.ic_astronaut)
                }
                
                // Set status badge
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
                        statusBadge.text = "UNKNOWN"
                        statusBadge.setBackgroundResource(R.drawable.badge_size_medium)
                    }
                }
                
                // Click to open detail
                root.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, LaunchDetailActivity::class.java).apply {
                        putExtra(LaunchDetailActivity.EXTRA_LAUNCH, launch)
                    }
                    context.startActivity(intent)
                }
            }
        }
        
        private fun formatDate(dateString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(dateString)
                
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                dateString.take(10)
            }
        }
    }

    class LaunchDiffCallback : DiffUtil.ItemCallback<Launch>() {
        override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
            return oldItem == newItem
        }
    }
}

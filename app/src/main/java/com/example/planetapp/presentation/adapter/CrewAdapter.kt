package com.example.planetapp.presentation.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.planetapp.R
import com.example.planetapp.data.remote.model.spacex.CrewMember
import com.example.planetapp.databinding.ItemCrewBinding

class CrewAdapter : ListAdapter<CrewMember, CrewAdapter.CrewViewHolder>(CrewDiffCallback()) {
    
    private var lastAnimatedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val binding = ItemCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CrewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        holder.bind(getItem(position))
        
        if (position > lastAnimatedPosition) {
            animateItem(holder, position)
            lastAnimatedPosition = position
        }
    }
    
    private fun animateItem(holder: CrewViewHolder, position: Int) {
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

    class CrewViewHolder(private val binding: ItemCrewBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(crew: CrewMember) {
            binding.apply {
                crewName.text = crew.name
                crewAgency.text = crew.agency ?: "Unknown Agency"
                
                // Load crew image
                if (!crew.image.isNullOrEmpty()) {
                    crewImage.load(crew.image) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.ic_astronaut)
                        error(R.drawable.ic_astronaut)
                    }
                } else {
                    crewImage.setImageResource(R.drawable.ic_astronaut)
                }
                
                // Set status badge
                when (crew.status?.lowercase()) {
                    "active" -> {
                        crewStatus.text = "ACTIVE"
                        crewStatus.setBackgroundResource(R.drawable.badge_size_large)
                    }
                    "inactive" -> {
                        crewStatus.text = "INACTIVE"
                        crewStatus.setBackgroundResource(R.drawable.badge_size_medium)
                    }
                    "retired" -> {
                        crewStatus.text = "RETIRED"
                        crewStatus.setBackgroundResource(R.drawable.badge_size_small)
                    }
                    else -> {
                        crewStatus.text = crew.status?.uppercase() ?: "UNKNOWN"
                        crewStatus.setBackgroundResource(R.drawable.badge_size_medium)
                    }
                }
                
                // Click to open Wikipedia
                root.setOnClickListener {
                    if (!crew.wikipedia.isNullOrEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(crew.wikipedia))
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }

    class CrewDiffCallback : DiffUtil.ItemCallback<CrewMember>() {
        override fun areItemsTheSame(oldItem: CrewMember, newItem: CrewMember): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CrewMember, newItem: CrewMember): Boolean {
            return oldItem == newItem
        }
    }
}

package com.example.planetapp.presentation.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.planetapp.R
import com.example.planetapp.databinding.ItemGalleryImageBinding

class GalleryAdapter(
    private val images: List<String>
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class GalleryViewHolder(private val binding: ItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(imageUrl: String) {
            binding.galleryImage.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_satellite)
                error(R.drawable.ic_satellite)
            }
            
            binding.root.setOnClickListener {
                // Open full image in browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl))
                itemView.context.startActivity(intent)
            }
        }
    }
}

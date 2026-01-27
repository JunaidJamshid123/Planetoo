package com.example.planetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.planetapp.databinding.ActivityPlanetDetailsBinding

class PlanetDetails : AppCompatActivity() {
    private lateinit var binding: ActivityPlanetDetailsBinding
    private lateinit var obj:PlanetData
    private var planetimage:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanetDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obj = intent.getParcelableExtra<PlanetData>("planet")!!
        planetimage = intent.getIntExtra("planetimage", -1)

        planetimage.let { image ->
            if (image != null) {
                setData(obj, image)
            }
        }
    }
    private fun setData(obj:PlanetData,planetImage:Int){
        if (obj != null) {
            binding.titleInfo.text = obj.title
            binding.distanceInfo.text = obj.distance + "Km"
            binding.gravityInfo.text = obj.gravity + "m/sec^2"
            binding.GalaxyInfo.text = obj.galaxy

            // Check if planetImage is not the default value (-1)
            if (planetImage != -1) {
                binding.imageDetailInfo.setImageResource(planetImage)
            } else {
                // Set a default image or handle the case where planetImage is -1
            }

            binding.Plantetinfodetails.text = obj.Overview
        } else {
            // Handle the case when obj is null
            // For example, show an error message or provide default data
        }
    }

}
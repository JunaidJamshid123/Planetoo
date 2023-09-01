package com.example.planetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_planet_details.*

class PlanetDetails : AppCompatActivity() {
    private lateinit var obj:PlanetData
    private var planetimage:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet_details)
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
            title_info.text = obj.title
            distance_info.text = obj.distance + "Km"
            gravity_info.text = obj.gravity + "m/sec^2"
            Galaxy_info.text = obj.galaxy

            // Check if planetImage is not the default value (-1)
            if (planetImage != -1) {
                image_detail_info.setImageResource(planetImage)
            } else {
                // Set a default image or handle the case where planetImage is -1
            }

            Plantetinfodetails.text = obj.Overview
        } else {
            // Handle the case when obj is null
            // For example, show an error message or provide default data
        }
    }

}
package com.example.planetapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planetapp.databinding.ViewBinding

class Adapter(val planet:List<PlanetData>):RecyclerView.Adapter<Adapter.myviewHolder>() {
    class myviewHolder(val binding: ViewBinding):RecyclerView.ViewHolder(binding.root){

        val title = binding.title
        val img = binding.planetImage
        val galaxy = binding.Galaxy
        val distance = binding.distance
        val gravity = binding.gravity



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewHolder {
       val binding = ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return myviewHolder(binding)
    }

    override fun onBindViewHolder(holder: myviewHolder, position: Int) {
        var dummyimage:Int? = null
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,PlanetDetails::class.java)
            intent.putExtra("planet",planet[position])
            intent.putExtra("planetimage",dummyimage)
            holder.itemView.context.startActivity(intent)
        }
        holder.title.text = planet[position].title
        holder.galaxy.text = planet[position].galaxy
        holder.distance.text = planet[position].distance+"Km"
        holder.gravity.text=planet[position].gravity+"m/sec^2"
        holder.img.setImageResource(planet[position].img)
    }

    override fun getItemCount(): Int {
      return planet.size
    }
}
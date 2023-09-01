package com.example.planetapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view.view.*

class Adapter(val planet:List<PlanetData>):RecyclerView.Adapter<Adapter.myviewHolder>() {
    class myviewHolder(view: View):RecyclerView.ViewHolder(view){

        val title = view.title
        val img = view.planetImage
        val galaxy = view.Galaxy
        val distance = view.distance
        val gravity = view.gravity



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewHolder {
       var itemView = LayoutInflater.from(parent.context)
           .inflate(R.layout.view,parent,false)
        return myviewHolder(itemView)
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
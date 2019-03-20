package com.ar.ciu.ciuar

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

class MonumentsViewAdapter(val monumentsList : ArrayList<Monument>, val context : Context) : RecyclerView.Adapter<MonumentsViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_monuments, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return monumentsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monument : Monument = monumentsList[position]
        holder.titleImg.text = monument.title
        Glide.with(this.context)
                .load(monument.img)
                .into(holder.urlImg)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val titleImg = itemView.findViewById(R.id.titleImg) as TextView
        val urlImg = itemView.findViewById(R.id.urlImg) as ImageView
    }
}
package com.ar.ciu.ciuar

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MonumentsViewAdapter(val monumentsList : ArrayList<Monument>) : RecyclerView.Adapter<MonumentsViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_monuments, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return monumentsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monument : Monument = monumentsList[position]
        holder.textViewName.text = monument.title
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.titleImg) as TextView
    }
}
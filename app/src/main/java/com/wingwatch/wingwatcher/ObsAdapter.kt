package com.wingwatch.wingwatcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ObsAdapter(
    private val obsList: List<Observation>,
    private val onItemClick: (Observation) -> Unit
) : RecyclerView.Adapter<ObsAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_observation, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val obs = obsList[position]
        val helperClass = HelperClass
        holder.tvSpecies.text = obs.species
        holder.tvNumber.text = obs.count
        holder.tvDate.text = obs.date
        holder.tvLat.text = obs.lat.toString()
        holder.tvLon.text = obs.lon.toString()
        holder.tvDate.text = helperClass.getPrettyDate(obs.date!!)
        holder.bind(obs)
    }

    override fun getItemCount(): Int {
        return obsList.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSpecies: TextView = itemView.findViewById(R.id.tvObsSpecies)
        val tvNumber: TextView = itemView.findViewById(R.id.tvObsNumber)
        val tvLat: TextView = itemView.findViewById(R.id.tvObsLat)
        val tvLon: TextView = itemView.findViewById(R.id.tvObsLon)
        val tvDate: TextView = itemView.findViewById(R.id.tvObsDate)

        private val lytItem: ConstraintLayout = itemView.findViewById(R.id.lytItemObs)
        fun bind(observation: Observation) {
            lytItem.setOnClickListener {
                onItemClick(observation)
            }
        }

    }
}
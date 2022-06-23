package com.example.finalexam.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalexam.R
import com.example.finalexam.room.Info


class CustomAdapter(
    private val onStatusChange: (Info, Boolean) -> Unit,
    private val onDelete: (Info) -> Unit,
) : ListAdapter<Info, CustomAdapter.MyViewHolder>(DIFF) {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var alarmTime: TextView = view.findViewById(R.id.timeEditText)
        var switch: SwitchCompat = view.findViewById(R.id.switch1)
        var btnDelete: TextView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.alarmTime.text = item.time
        holder.switch.isChecked = item.isActive
        holder.switch.setOnClickListener {
            onStatusChange.invoke(item, !item.isActive)
        }
        holder.btnDelete.setOnClickListener { onDelete.invoke(item) }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Info>() {
            override fun areItemsTheSame(oldItem: Info, newItem: Info): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Info, newItem: Info): Boolean {
                return oldItem.time == newItem.time
            }
        }
    }
}
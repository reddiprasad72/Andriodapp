package com.bluboy.android.presentation.game.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R

class TournamentHistoryAdapter(val callback: (String) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_tournament_history, container, false)
        viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
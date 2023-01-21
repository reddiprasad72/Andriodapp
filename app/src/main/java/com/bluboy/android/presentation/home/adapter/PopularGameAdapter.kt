package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.TrendingGame
import com.bluboy.android.databinding.ItemPopularGameBinding
import com.bluboy.android.presentation.utility.loadCornerImageGame

open class PopularGameAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<TrendingGame>,
    val callback: (temp: TrendingGame, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemPopularGameBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        rowBinding =
            ItemPopularGameBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return homeArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        loadCornerImageGame(rowBinding.imageViewGame, homeArrayList[position].gameImage)
        rowBinding.textViewGameName.text = homeArrayList[position].gameName

        holder.itemView.setOnClickListener {
            callback.invoke(homeArrayList[position], position)
        }
    }

    inner class ViewHolder(binding: ItemPopularGameBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
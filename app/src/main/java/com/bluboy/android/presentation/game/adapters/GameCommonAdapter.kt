package com.bluboy.android.presentation.game.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.DummyGames
import com.bluboy.android.databinding.ItemOtherGamesPopularBinding
import com.bluboy.android.presentation.utility.loadCornerImageGame

open class GameCommonAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<DummyGames>,
    val callback: (gameData: DummyGames) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemOtherGamesPopularBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        rowBinding =
            ItemOtherGamesPopularBinding.inflate(
                LayoutInflater.from(container.context), container, false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return homeArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        loadCornerImageGame(rowBinding.imageViewGames, homeArrayList[position].image)
        rowBinding.textViewGameName.text = homeArrayList[position].title
    }

    inner class ViewHolder(binding: ItemOtherGamesPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
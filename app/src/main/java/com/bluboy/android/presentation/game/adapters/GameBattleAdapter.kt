package com.bluboy.android.presentation.game.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.databinding.ItemOtherGamesPopularBinding
import com.bluboy.android.presentation.utility.loadCornerImageGame

open class GameBattleAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<GamesData>,
    val callback: (gameData: GamesData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemOtherGamesPopularBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        rowBinding =
            ItemOtherGamesPopularBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return homeArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        loadCornerImageGame(rowBinding.imageViewGames, homeArrayList[position].gameImage)
        rowBinding.textViewGameName.text = homeArrayList[position].gameName
        rowBinding.textViewOnline.text = homeArrayList[position].totalOnlineUsers + " Online"
        holder.itemView.setOnClickListener {
            callback.invoke(homeArrayList[position])
        }
    }

    inner class ViewHolder(binding: ItemOtherGamesPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
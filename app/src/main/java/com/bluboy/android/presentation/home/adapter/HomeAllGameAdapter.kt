package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.databinding.ItemAllGamesHomeBinding
import com.bluboy.android.presentation.utility.*

open class HomeAllGameAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<GamesData>,
    val callback: (temp: GamesData, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemAllGamesHomeBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemAllGamesHomeBinding.inflate(
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
        try {
            holder.setIsRecyclable(false)
            loadCornerImageGame(rowBinding.imageViewGames, homeArrayList[position]?.gameImage)
            rowBinding.textViewGameName.text = homeArrayList[position]?.gameName

            if (homeArrayList[position]?.gameCommingSoon == "Y") {
                rowBinding.textviewCommingSoon.visible()
                rowBinding.textViewOnlineUser.invisible()
            } else {
                rowBinding.textviewCommingSoon.invisible()
                rowBinding.textViewOnlineUser.visible()
            }

            if (homeArrayList[position]?.gameTag != "") {
                rowBinding.textViewGameTag.visible()
                rowBinding.textViewGameTag.text = homeArrayList[position]?.gameTag
                if (position % 2 == 0) {
                    rowBinding.textViewGameTag.background =
                        ContextCompat.getDrawable(contexts, R.drawable.tag_one)
                } else {
                    rowBinding.textViewGameTag.background =
                        ContextCompat.getDrawable(contexts, R.drawable.tag_two)
                }
            } else {
                rowBinding.textViewGameTag.gone()
            }

            rowBinding.textViewOnlineUser.text =
                homeArrayList[position]?.totalOnlineUsers + " Users Online"
        } catch (e: Exception) {
        }

        holder.itemView.setSafeOnClickListener {
            try {
                callback.invoke(homeArrayList[position], position)
            } catch (e: Exception) {
            }
        }
    }

    inner class ViewHolder(binding: ItemAllGamesHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
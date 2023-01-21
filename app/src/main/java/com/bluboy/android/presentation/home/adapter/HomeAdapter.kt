package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.databinding.ItemGamesHomeBinding
import com.bluboy.android.presentation.utility.*

open class HomeAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<GamesData>,
    val callback: (temp: GamesData, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemGamesHomeBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemGamesHomeBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return homeArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        loadCornerImageGame(rowBinding.imageViewGames, homeArrayList[position].gameImage)
        rowBinding.textViewGameName.text = homeArrayList[position].gameName

        if (homeArrayList[position].gameCommingSoon == "Y") {
            rowBinding.textviewCommingSoon.visible()
            rowBinding.textViewOnlineUser.invisible()
        } else {
            rowBinding.textviewCommingSoon.invisible()
            rowBinding.textViewOnlineUser.visible()
        }
        if (homeArrayList[position].gameTag != "") {
            rowBinding.textViewGameTag.visible()
            /*when {
                homeArrayList[position]?.gameTag?.contains("Hot",false) == true -> loadImage(rowBinding.textViewGameTag,R.drawable.tag_hot)
                homeArrayList[position]?.gameTag?.contains("New",false) == true -> loadImage(rowBinding.textViewGameTag,R.drawable.tag_new)
                homeArrayList[position]?.gameTag?.contains("Recommended",false) == true -> loadImage(rowBinding.textViewGameTag,R.drawable.tag_recommended)
            }*/
            rowBinding.textViewGameTag.text = homeArrayList[position].gameTag
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
            homeArrayList[position].totalOnlineUsers + " Users Online"
        holder.itemView.setSafeOnClickListener {
            callback.invoke(homeArrayList[position], position)
        }
    }

    inner class ViewHolder(binding: ItemGamesHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
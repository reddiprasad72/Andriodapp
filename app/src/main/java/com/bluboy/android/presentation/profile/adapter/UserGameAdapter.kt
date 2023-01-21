package com.bluboy.android.presentation.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.UserGame
import com.bluboy.android.databinding.ItemGamesProfileBinding
import com.bluboy.android.presentation.utility.loadImage
import com.bluboy.android.presentation.utility.setSafeOnClickListener

open class UserGameAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<UserGame>,
    val callback: (userGame: UserGame) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemGamesProfileBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemGamesProfileBinding.inflate(
                LayoutInflater.from(container.context), container, false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return homeArrayList.size//homeArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        loadImage(rowBinding.imageViewGames, homeArrayList[position].gameImage)

        rowBinding.textViewGameName.text = homeArrayList[position].gameName

        rowBinding.row.setSafeOnClickListener {
            callback.invoke(homeArrayList[position])
        }
    }

    inner class ViewHolder(binding: ItemGamesProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
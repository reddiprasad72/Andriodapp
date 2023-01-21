package com.bluboy.android.presentation.game.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.LeaderBoard
import com.bluboy.android.databinding.ItemLeaderboardBinding
import com.bluboy.android.presentation.utility.loadCircleImage

class LeaderBoardGamesAdapter(
    val context: Context,
    var arrayListLeaderboardFinal: ArrayList<LeaderBoard>,
    val callback: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemLeaderboardBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemLeaderboardBinding.inflate(LayoutInflater.from(container.context), container, false)

        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayListLeaderboardFinal.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*if (position % 2 == 0) {
            rowBinding.constaintMain.background =
                ContextCompat.getDrawable(context, R.drawable.rectangle_leaderboard)
        } else {
            rowBinding.constaintMain.background =
                ContextCompat.getDrawable(context, R.drawable.bg_transparent)
        }*/
        holder.setIsRecyclable(false)
        loadCircleImage(
            rowBinding.imageViewUser,
            arrayListLeaderboardFinal[position].profilePic
        )
        rowBinding.textViewUserName.text = arrayListLeaderboardFinal[position].userName
        rowBinding.textViewPoint.text = arrayListLeaderboardFinal[position].totalWinningAmount
        rowBinding.textViewRank.text = arrayListLeaderboardFinal[position].rank.toString()
    }

    inner class ViewHolder(binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
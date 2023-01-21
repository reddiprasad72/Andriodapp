package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.LeaderBoard
import com.bluboy.android.databinding.ItemLeaderboardBinding
import com.bluboy.android.presentation.utility.loadCornerImage

class LeaderBoardListAdapter(
    val context: Context,
    var arrayListLeaderboard: ArrayList<LeaderBoard>,
    val callback: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemLeaderboardBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        rowBinding =
            ItemLeaderboardBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayListLeaderboard.size
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
        loadCornerImage(rowBinding.imageViewUser, arrayListLeaderboard[position].profilePic)
        rowBinding.textViewUserName.text = arrayListLeaderboard[position].userName
        rowBinding.textViewPoint.text = arrayListLeaderboard[position].totalWinningAmount
        rowBinding.textViewRank.text = arrayListLeaderboard[position].rank.toString()
    }

    inner class ViewHolder(binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
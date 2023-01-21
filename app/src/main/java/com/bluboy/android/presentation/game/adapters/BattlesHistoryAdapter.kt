package com.bluboy.android.presentation.game.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.BattleHistory
import com.bluboy.android.databinding.ItemBattlesHistoryBinding
import com.bluboy.android.presentation.utility.*
import kotlin.math.roundToInt

class BattlesHistoryAdapter(
    var context: Context,
    var arrayListBattleHistory: ArrayList<BattleHistory>,
    val callback: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemBattlesHistoryBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemBattlesHistoryBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayListBattleHistory.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        loadCornerImage(rowBinding.imageViewGame, arrayListBattleHistory[position].gameImage)
        rowBinding.textViewGameName.text = arrayListBattleHistory[position].tournamentName
        // rowBinding.textViewRoomCode.text = arrayListBattleHistory[position].room
        var dateTimeFirst =
            DateTimeHelper.convertLongToTime(arrayListBattleHistory[position].gameStart.toLong())
        var date = DateTimeHelper.convertDate(
            dateTimeFirst,
            input = "yyyy-MM-dd'T'hh:mm:ss",
            output = "dd-MM-yyyy hh:mm a"
        )
        rowBinding.textViewDateTime.text = date.uppercase()
        rowBinding.textViewfee.text = context.getString(
            R.string.x_price_rupees,
            arrayListBattleHistory[position].tournamentEntryFees
        )

        if (arrayListBattleHistory[position].players.isNotEmpty() && arrayListBattleHistory[position].players.size == 2) {
            rowBinding.battleView.visible()
            var mLayoutParams =
                RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            mLayoutParams.setMargins(
                0,
                (10 * context.resources.displayMetrics.density).roundToInt(),
                0,
                0
            )

            rowBinding.battleView.layoutParams = mLayoutParams
            loadCircleImage(
                rowBinding.imageViewOne,
                arrayListBattleHistory[position].players[0].profilePic
            )
            rowBinding.textViewUserNameOne.text =
                arrayListBattleHistory[position].players[0].userName
            rowBinding.textViewPointOne.text =
                arrayListBattleHistory[position].players[0].score
            loadCircleImage(
                rowBinding.imageViewTwo,
                arrayListBattleHistory[position].players[1].profilePic
            )
            rowBinding.textViewUserNameTwo.text =
                arrayListBattleHistory[position].players[1].userName
            rowBinding.textViewPointTwo.text =
                arrayListBattleHistory[position].players[1].score
//            rowBinding.buttonAmount.text = context.getString(R.string.x_price_rupees, arrayListBattleHistory[position].winAmount)

            if (arrayListBattleHistory[position].winStatus == "Cancelled") {
                rowBinding.imageViewCrownOne.gone()
                rowBinding.imageViewCrownTwo.gone()
//                rowBinding.buttonAmount.setBackgroundDrawable(context.getDrawable(R.drawable.bg_button_red))
                rowBinding.imageViewStatus.visible()
                loadImage(rowBinding.imageViewStatus, R.drawable.ic_lost)
                rowBinding.textViewLost.gone()
                rowBinding.textViewCancelled.visible()
                rowBinding.textViewCancelled.text = "Cancelled"

            } else if (arrayListBattleHistory[position].winStatus == "Pending") {
                rowBinding.imageViewCrownOne.gone()
                rowBinding.imageViewCrownTwo.gone()
//                rowBinding.buttonAmount.setBackgroundDrawable(context.getDrawable(R.drawable.bg_button_red))
                rowBinding.imageViewStatus.visible()
                loadImage(rowBinding.imageViewStatus, R.drawable.ic_lost)
                rowBinding.textViewLost.gone()
                rowBinding.textViewCancelled.visible()
                rowBinding.textViewCancelled.text = "Pending"

            } else if (arrayListBattleHistory[position].players[0].score.toInt()
                == arrayListBattleHistory[position].players[1].score.toInt()) {
                rowBinding.imageViewCrownOne.gone()
                rowBinding.imageViewCrownTwo.gone()
//                rowBinding.buttonAmount.setBackgroundDrawable(context.getDrawable(R.drawable.bg_button_yellow))
                rowBinding.imageViewStatus.visible()
                loadImage(rowBinding.imageViewStatus, R.drawable.ic_draw)
                rowBinding.textViewLost.gone()
                rowBinding.textViewCancelled.visible()
                rowBinding.textViewCancelled.text = "Draw"

            } else if (arrayListBattleHistory[position].players[0].score.toInt() >
                arrayListBattleHistory[position].players[1].score.toInt()) {
                rowBinding.imageViewCrownOne.visible()
                rowBinding.imageViewCrownTwo.gone()
//                rowBinding.buttonAmount.setBackgroundDrawable(context.getDrawable(R.drawable.bg_button_green))
                rowBinding.imageViewStatus.visible()
                loadImage(rowBinding.imageViewStatus, R.drawable.ic_won_transaction)
                rowBinding.textViewLost.gone()
                rowBinding.textViewCancelled.gone()

            } else if (arrayListBattleHistory[position].players[0].score.toInt() <
                arrayListBattleHistory[position].players[1].score.toInt()) {
                rowBinding.imageViewCrownOne.gone()
                rowBinding.imageViewCrownTwo.visible()
//                rowBinding.buttonAmount.setBackgroundDrawable(context.getDrawable(R.drawable.bg_button_red))
                rowBinding.imageViewStatus.visible()
                loadImage(rowBinding.imageViewStatus, R.drawable.ic_lost)
                rowBinding.textViewLost.gone()
                rowBinding.textViewCancelled.visible()
                rowBinding.textViewCancelled.text = "Lost"
            }
        } else {
            rowBinding.battleView.gone()
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
    }

    inner class ViewHolder(binding: ItemBattlesHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
package com.bluboy.android.presentation.game.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.Battle
import com.bluboy.android.databinding.ItemBattlesBinding
import com.bluboy.android.presentation.utility.gone
import com.bluboy.android.presentation.utility.visible

class BattlesGameAdapter(
    var context: Context,
    var arrayBattles: ArrayList<Battle>,
    val callback: (Battle) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var rowBinding: ItemBattlesBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemBattlesBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayBattles.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        holder.itemView.setOnClickListener {
            callback.invoke(arrayBattles[position])
        }

        rowBinding.buttonFee.setOnClickListener {
            callback.invoke(arrayBattles[position])
        }
        /*BounceView.addAnimTo(rowBinding.buttonFee)*/
        rowBinding.textViewBattleName.text = arrayBattles[position].tournamentName
        rowBinding.textViewOnlinePlayer.text =
            arrayBattles[position].totalOnlineUsers + " Online"
        if (arrayBattles[position].tournamentEntryFees == "0") {
            rowBinding.buttonFee.text = "FREE"
        } else {
            rowBinding.buttonFee.text = context.getString(
                R.string.join_battle_rupees,
                arrayBattles[position].tournamentEntryFees.toString()
            )
        }
        if (arrayBattles[position].tournamentBonusLimit == "0") {
            rowBinding.groupBonus.gone()
        } else {
            rowBinding.groupBonus.visible()
            rowBinding.textViewBonus.text =
                "Use Bonus ${arrayBattles[position].tournamentBonusLimit} %"
        }
        /*if (arrayBattles[position].tournamentFeatured == "Y") {
            rowBinding.imageViewFeaturedBattle.visible()
        } else {
            rowBinding.imageViewFeaturedBattle.gone()
        }*/
    }

    inner class ViewHolder(binding: ItemBattlesBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
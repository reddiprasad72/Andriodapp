package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.presentation.utility.invisible
import com.bluboy.android.presentation.utility.setSafeOnClickListener
import com.bluboy.android.presentation.utility.visible
import com.bumptech.glide.Glide

class HomeGameCarouselAdapter (
    val contexts: Context,
    private var list : ArrayList<GamesData>,
    val callback: (temp: GamesData, position: Int) -> Unit
): RecyclerView.Adapter<HomeGameCarouselAdapter.ViewHolder>() {

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val gameImage : ImageView = itemView.findViewById(R.id.sivGameIcon)
         val tvGameName : AppCompatTextView = itemView.findViewById(R.id.tvGameName)
         val tvComingSoon : AppCompatTextView = itemView.findViewById(R.id.tvComingSoon)
         val btnPlayNow : AppCompatButton = itemView.findViewById(R.id.buttonPlayNow)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_games_carousel, parent,false)
         return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        Glide.with(holder.gameImage).load(list[position]?.gameImage).into(holder.gameImage)
        holder.tvGameName.text= list[position]?.gameName

        if (list[position]?.gameCommingSoon == "Y") {
            holder.tvComingSoon.visible()
            holder.btnPlayNow.invisible()
//            rowBinding.textViewOnlineUser.invisible()
        } else {
            holder.tvComingSoon.invisible()
            holder.btnPlayNow.visible()
//            rowBinding.textViewOnlineUser.visible()
        }

        holder.btnPlayNow.setSafeOnClickListener {
            try {
                callback.invoke(list[position], position)
            } catch (e: Exception) {
            }
        }
        holder.itemView.setSafeOnClickListener {
            try {
                callback.invoke(list[position], position)
            } catch (e: Exception) {
            }
        }
    }

    fun updateData(list: ArrayList<GamesData>) {
        this.list = list
        notifyDataSetChanged()
    }

    //Use the method for item changed
    /*fun itemChanged() {
        // remove last item for test purposes
        this.list[0] = (DataModel(R.drawable.londonlove, "Thi is cool"))
        notifyItemChanged(0)

    }*/

    //Use the method for checking the itemRemoved
    /*fun removeData() {
        // remove last item for test purposes
        val orgListSize = list.size
        this.list = this.list.subList(0, orgListSize - 1).toList() as ArrayList<GamesData>
        notifyItemRemoved(orgListSize - 1)
    }*/
}
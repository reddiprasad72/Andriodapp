package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.Refer
import com.bluboy.android.databinding.ItemMyReferBinding
import com.bluboy.android.presentation.utility.loadImage
import com.bluboy.android.presentation.utility.toTwoDigit

open class MyReferAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<Refer>,
    val callback: (temp: Refer) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemMyReferBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemMyReferBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return homeArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        rowBinding.textViewRank.text = (position + 1).toTwoDigit()
        rowBinding.textViewUserName.text = homeArrayList[position].userName
        rowBinding.textViewPoint.text = "+${homeArrayList[position].amount} "
        loadImage(rowBinding.imageViewUser, homeArrayList[position].profilePic)
    }

    inner class ViewHolder(binding: ItemMyReferBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}
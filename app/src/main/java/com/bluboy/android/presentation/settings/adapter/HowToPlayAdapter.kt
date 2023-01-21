package com.bluboy.android.presentation.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.HowToPlayContent
import com.bluboy.android.databinding.ItemHowToPlayBinding
import com.bluboy.android.presentation.utility.loadImage
import com.bluboy.android.presentation.utility.setSafeOnClickListener

class HowToPlayAdapter(
    var arrayListHowToPlay: ArrayList<HowToPlayContent>,
    val callback: (HowToPlayContent) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemHowToPlayBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemHowToPlayBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return arrayListHowToPlay.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        rowBinding.textViewHowToPlay.text = arrayListHowToPlay[position].content_title
        rowBinding.textViewGameName.text = arrayListHowToPlay[position].game_name

        loadImage(rowBinding.imageViewGame, arrayListHowToPlay[position].game_image)

        holder.itemView.setSafeOnClickListener {
            callback.invoke(arrayListHowToPlay[position])
        }

        rowBinding.btViewPlay.setSafeOnClickListener {
            callback.invoke(arrayListHowToPlay[position])
        }
    }

    inner class ViewHolder(binding: ItemHowToPlayBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
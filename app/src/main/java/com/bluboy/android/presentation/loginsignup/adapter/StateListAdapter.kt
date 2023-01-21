package com.bluboy.android.presentation.loginsignup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.StateList
import com.bluboy.android.databinding.ItemStateListBinding

open class StateListAdapter(
    private val stateArrayList: ArrayList<StateList>,
    private val callback: (temp: StateList) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemStateListBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        rowBinding =
            ItemStateListBinding.inflate(LayoutInflater.from(container.context), container, false)
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return stateArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        rowBinding.textViewStateName.text = stateArrayList[position].stateName
        holder.itemView.setOnClickListener {
            callback.invoke(stateArrayList[position])
        }
    }

    inner class ViewHolder(binding: ItemStateListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
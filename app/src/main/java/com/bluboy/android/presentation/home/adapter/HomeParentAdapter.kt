package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.data.models.CategoryData
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.databinding.ItemGamesHomeParentBinding
import com.bluboy.android.presentation.utility.*

open class HomeParentAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<CategoryData>,
    val callback: (temp: GamesData, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var homeAdapter: HomeAdapter
    lateinit var rowBinding: ItemGamesHomeParentBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemGamesHomeParentBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return homeArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if (homeArrayList[position].games.isNotEmpty()) {
            rowBinding.textViewCategoryName.visible()
            rowBinding.recyclerViewParentHome.visible()
            rowBinding.textViewCategoryName.text = homeArrayList[position].categoryName
            var linearLayoutManager =
                LinearLayoutManager(contexts, LinearLayoutManager.HORIZONTAL, false)
            rowBinding.recyclerViewParentHome.layoutManager = linearLayoutManager
            homeAdapter = HomeAdapter(contexts,
                homeArrayList[position].games as ArrayList<GamesData>, { games, pos ->
                    if (games.gameCommingSoon == "Y") {
                    } else {
                        callback.invoke(games, pos)
                    }
                })
            rowBinding.recyclerViewParentHome.adapter = homeAdapter

        } else {
            rowBinding.textViewCategoryName.gone()
            rowBinding.recyclerViewParentHome.gone()
        }

    }

    inner class ViewHolder(binding: ItemGamesHomeParentBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
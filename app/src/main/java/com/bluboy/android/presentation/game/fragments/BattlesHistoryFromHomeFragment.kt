package com.bluboy.android.presentation.game.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.data.models.BattaleHistoryRs
import com.bluboy.android.data.models.BattleHistory
import com.bluboy.android.databinding.FragmentBattleHistoryBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.game.adapters.BattlesHistoryAdapter
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.gone
import com.bluboy.android.presentation.utility.visible
import org.koin.android.viewmodel.ext.android.sharedViewModel


class BattlesHistoryFromHomeFragment : BaseFragment() {

    private var leaderType = "Battles"
    private var mAdapter: BattlesHistoryAdapter? = null
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var page = 1
    private var isInProgress = false
    private var battle: BattaleHistoryRs? = null
    private var arrayListBattleHistory = ArrayList<BattleHistory>()
    private lateinit var binding: FragmentBattleHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBattleHistoryBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAdapter()
//        showProgress()
        arrayListBattleHistory.clear()
        homeViewModel.getAllGameHistory(1)
    }

    private fun observer() {
        homeViewModel.battleHistoryObserver.observe(this, Observer {
            isInProgress = false
            binding.mShimmerBattleHistory.gone()
            binding.progressBottom.gone()

            if (it.status == 1) {
                battle = it
                binding.nestedScrollViewBattlesHistory.visible()
                binding.llEmpty.gone()

                it.data?.battleHistory?.let { it1 -> arrayListBattleHistory.addAll(it1) }
                if (arrayListBattleHistory.size==0){
                    binding.nestedScrollViewBattlesHistory.gone()
                    binding.llEmpty.visible()
                }
                mAdapter?.notifyDataSetChanged()
            } else {
                binding.nestedScrollViewBattlesHistory.gone()
                binding.llEmpty.visible()
            }
        })
    }

    private fun setAdapter() {
        mAdapter = BattlesHistoryAdapter(requireContext(), arrayListBattleHistory) {

        }

        var mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewBattle.layoutManager = mLayoutManager
        binding.recyclerViewBattle.adapter = mAdapter

        binding.nestedScrollViewBattlesHistory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // end of the scroll view
                if (battle?.data?.isHaveMoreRecords.equals("Yes") && !isInProgress) {
                    page++
                    binding.progressBottom.visible()
                    getBattleHistory(true)
                }
            }
        })
    }

    fun getBattleHistory(isScroll: Boolean? = false) {
        if (isInProgress) {
            return
        }
        isInProgress = true
        if (isScroll == false) {
            page = 1
            arrayListBattleHistory.clear()
            mAdapter?.notifyDataSetChanged()
        }
//        showProgress()
        if (page == 1) {
            binding.mShimmerBattleHistory.visible()
            binding.nestedScrollViewBattlesHistory.gone()
            binding.llEmpty.gone()
        }
        homeViewModel.getAllGameHistory(page)
    }
}
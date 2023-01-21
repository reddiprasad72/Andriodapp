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

class BattlesHistoryFragment : BaseFragment() {

    private var leaderType = "Battles"
    private var mAdapter: BattlesHistoryAdapter? = null
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var page = 1
    private var isInProgress = false
    private var gameId: String? = null
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
        if (gameId != null) {
            arrayListBattleHistory.clear()
        }
    }

    private fun observer() {
        homeViewModel.battleHistoryObserver.observe(this, Observer {
            isInProgress = false
//            Handler(Looper.getMainLooper()).postDelayed({
//            hideProgress()
//            },150)
            binding.mShimmerBattleHistory.gone()
            binding.progressBottom.gone()

            if (it.status == 1) {
                binding.nestedScrollViewBattlesHistory.visible()
                binding.llEmpty.gone()
                var temp = arrayListBattleHistory.size
                battle = it
                it.data?.battleHistory?.let { it1 -> arrayListBattleHistory.addAll(it1) }
                mAdapter?.notifyDataSetChanged()
            } else {
                binding.nestedScrollViewBattlesHistory.gone()
                binding.llEmpty.visible()
            }
        })
    }

    private fun setAdapter() {
        mAdapter = BattlesHistoryAdapter(requireContext(), arrayListBattleHistory, {
        })
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
                    getBattleHistory(gameId!!, true)
                }
            }
        })
    }

    fun getBattleHistory(gameId: String, isScroll: Boolean? = false) {
        if (isInProgress) {
            return
        }
        isInProgress = true
        this.gameId = gameId
        if (isScroll == false) {
            page = 1
            arrayListBattleHistory.clear()
        }
        if (page == 1) {
            binding.mShimmerBattleHistory.visible()
            binding.nestedScrollViewBattlesHistory.gone()
            binding.llEmpty.gone()
        }
        homeViewModel.getBattleHistory(this.gameId!!, page)
    }
}
package com.bluboy.android.presentation.home.fragments.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bluboy.android.R
import com.bluboy.android.data.models.LeaderBoard
import com.bluboy.android.data.models.LeaderboardRs
import com.bluboy.android.databinding.FragmentLeaderboardBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.home.adapter.LeaderBoardListAdapter
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LeaderboardWeeklyFragment : BaseFragment() {

    private var mAdapter: LeaderBoardListAdapter? = null
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var leaderboardRs: LeaderboardRs? = null
    private var page = 1
    private var isInProgress = false
    private var arrayListLeaderboard = ArrayList<LeaderBoard>()
    private var arrayListLeaderboardFinal = ArrayList<LeaderBoard>()
    private lateinit var binding: FragmentLeaderboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).asGif()
            .load(R.raw.loader)
            .into(binding.progress)

        setAdapter()
        arrayListLeaderboard.clear()
        arrayListLeaderboardFinal.clear()
        mAdapter?.notifyDataSetChanged()
//        showProgress()
        if (page == 1) {
            binding.mShimmerLeaderboard.visible()
            binding.nestedScrollViewLeaderBoard.gone()
            binding.llEmpty.gone()
        }
//        Handler(Looper.getMainLooper()).postDelayed({
        homeViewModel.getLeaderboardWeekly(page)
    }

    private fun observer() {
        homeViewModel.LeaderboardObserverWeekly.observe(this, Observer {
            isInProgress = false
//            hideProgress()
            binding.progressBottom.gone()
            binding.mShimmerLeaderboard.gone()
            if (page == 1) {
                arrayListLeaderboard.clear()
                arrayListLeaderboardFinal.clear()
            }

            if (it.status == 1) {
                leaderboardRs = it
                it.data?.leaderBoard?.let { it1 -> arrayListLeaderboard.addAll(it1) }

                if (it.data != null && arrayListLeaderboard.isNotEmpty()) {
                    binding.nestedScrollViewLeaderBoard.visible()
                    binding.llEmpty.gone()

                    if (arrayListLeaderboard.size > 0) {
                        binding.topThree.imageViewProfileone.visible()
                        binding.topThree.imageViewProfilebg.visible()
                        binding.topThree.ivRankOneGold.visible()

                        binding.topThree.imageCrown.visible()
                        binding.topThree.textViewUserOneName.visible()
                        binding.topThree.imageViewBackgroundUserOne.visible()
                        binding.topThree.textViewUserOnePoint.visible()
                        binding.topThree.textViewRankOne.gone()

                        twoHide()
                        threeHide()
                        loadCornerImage(
                            binding.topThree.imageViewProfileone,
                            arrayListLeaderboard[0].profilePic
                        )
                        binding.topThree.textViewUserOneName.text = arrayListLeaderboard[0].userName
                        binding.topThree.textViewUserOnePoint.text =
                            arrayListLeaderboard[0].totalWinningAmount
                    }
                    if (arrayListLeaderboard.size > 1) {
                        twoShow()
                        threeHide()
                        if (arrayListLeaderboard[1].rank == 1)
                            binding.topThree.textViewRankTwo.text = getString(R.string.rank_one)
                        else
                            binding.topThree.textViewRankTwo.text = getString(R.string.rank_two)
                        loadCornerImage(
                            binding.topThree.imageViewProfileTwo,
                            arrayListLeaderboard[1].profilePic
                        )
                        binding.topThree.textViewUserTwoName.text = arrayListLeaderboard[1].userName
                        binding.topThree.textViewUserTwoPoint.text =
                            arrayListLeaderboard[1].totalWinningAmount
                    }
                    if (arrayListLeaderboard.size > 2) {
                        twoShow()
                        threeShow()
                        if (arrayListLeaderboard[2].rank == 1)
                            binding.topThree.textViewRankThree.text = getString(R.string.rank_one)
                        else if (arrayListLeaderboard[2].rank == 2)
                            binding.topThree.textViewRankThree.text = getString(R.string.rank_two)
                        else
                            binding.topThree.textViewRankThree.text = getString(R.string.rank_three)
                        loadCornerImage(
                            binding.topThree.imageViewProfileThree,
                            arrayListLeaderboard[2].profilePic
                        )
                        binding.topThree.textViewUserThreeName.text =
                            arrayListLeaderboard[2].userName
                        binding.topThree.textViewUserThreePoint.text =
                            arrayListLeaderboard[2].totalWinningAmount
                    }

                    if (it.data?.ownRank != null && it.data?.ownRank?.userName != null) {
                        binding.ownRankLayout.root.visible()
                        binding.ownRankLayout.textViewRank.text = it.data?.ownRank?.rank.toString()
                        loadCornerImage(
                            binding.ownRankLayout.imageViewUser,
                            it.data?.ownRank?.profilePic
                        )
                        binding.ownRankLayout.textViewUserName.text =
                            it.data?.ownRank?.userName.toString()
                        binding.ownRankLayout.textViewPoint.text =
                            it.data?.ownRank?.totalWinningAmount.toString()
                    } else {
                        binding.ownRankLayout.root.gone()
                    }

                    arrayListLeaderboardFinal.clear()
                    if (arrayListLeaderboard.size > 3) {
                        binding.constraintRank.visible()
                        for (i in 3 until arrayListLeaderboard.size) {
                            arrayListLeaderboardFinal.add(arrayListLeaderboard[i])
                        }
                    } else {
                        binding.constraintRank.gone()
                    }
                    mAdapter?.notifyDataSetChanged()
                }
            } else {
                binding.nestedScrollViewLeaderBoard.gone()
                binding.llEmpty.visible()
            }
        })
    }

    private fun twoHide() {
        binding.topThree.apply {
            imageViewProfileTwo.invisible()
            imageViewProfileTwobg.invisible()
            ivRankTwoSilver.invisible()
            textViewUserTwoName.invisible()
            imageViewBackgroundUserTwo.invisible()
            textViewUserTwoPoint.invisible()
            textViewRankTwo.invisible()
        }
    }

    private fun threeHide() {
        binding.topThree.apply {
            imageViewProfileThree.invisible()
            imageViewProfileThreebg.invisible()
            ivRankThreeBronze.invisible()
            textViewUserThreeName.invisible()
            imageViewBackgroundUserThree.invisible()
            textViewUserThreePoint.invisible()
            textViewRankThree.invisible()
        }
    }

    private fun twoShow() {
        binding.topThree.apply {
            imageViewProfileTwo.visible()
            imageViewProfileTwobg.visible()
            ivRankTwoSilver.visible()
            textViewUserTwoName.visible()
            imageViewBackgroundUserTwo.visible()
            textViewUserTwoPoint.visible()
            textViewRankTwo.gone()
        }
    }

    private fun threeShow() {
        binding.topThree.apply {
            imageViewProfileThree.visible()
            imageViewProfileThreebg.visible()
            ivRankThreeBronze.visible()
            textViewUserThreeName.visible()
            imageViewBackgroundUserThree.visible()
            textViewUserThreePoint.visible()
            textViewRankThree.gone()
        }
    }

    private fun setAdapter() {
        mAdapter = LeaderBoardListAdapter(requireContext(), arrayListLeaderboardFinal) {
        }

        var mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvLeaderboard.layoutManager = mLayoutManager
        binding.rcvLeaderboard.adapter = mAdapter

        binding.nestedScrollViewLeaderBoard.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // end of the scroll view
                if (leaderboardRs?.data?.isHaveMoreRecords.equals("Yes") && !isInProgress) {
                    page++
                    binding.progressBottom.visible()
                    getLeaderBoards(true)
                }
            }
        })
    }

    fun getLeaderBoards(isScroll: Boolean? = false) {
        if (isInProgress) {
            return
        }
        isInProgress = true

        if (isScroll == false) {
            page = 1
            arrayListLeaderboard.clear()
            arrayListLeaderboardFinal.clear()
            mAdapter?.notifyDataSetChanged()
        }
//       showProgress()
        if (page == 1) {
            binding.mShimmerLeaderboard.visible()
            binding.nestedScrollViewLeaderBoard.gone()
            binding.llEmpty.gone()
        }
//        Handler(Looper.getMainLooper()).postDelayed({
        homeViewModel.getLeaderboardWeekly(page)
//        },400)
    }
}
package com.bluboy.android.presentation.game.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.data.models.Battle
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.databinding.FragmentBattleGameBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.game.activity.GameActivity
import com.bluboy.android.presentation.game.adapters.BattlesGameAdapter
import com.bluboy.android.presentation.game.adapters.GameBattleAdapter
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.utility.bouncyEffect.OverScrollDecoratorHelper
import org.koin.android.viewmodel.ext.android.sharedViewModel

class BattlesGameFragment : BaseFragment() {

    private var leaderType = "Battles"
    private var mAdapter: BattlesGameAdapter? = null
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var arrayGames = ArrayList<GamesData>()
    private var arrayBattle = ArrayList<Battle>()
    private var games: GamesData? = null
    private var page = 1
    private lateinit var homeAdapter: GameBattleAdapter
    private lateinit var binding: FragmentBattleGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBattleGameBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAdapter()
        arrayBattle.clear()
        showProgress()
        if (games != null && games?.gameId != null)
            homeViewModel.getBattles(games?.gameId!!.toInt(), page)
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerViewHomeGame.invisible()
        homeViewModel.getGameList(page)
    }

    fun getGameData(games: GamesData) {
        this.games = games
    }

    private fun observer() {
        homeViewModel.BattlesObserver.observe(this, Observer {
            hideProgress()
            // binding.mShimmerViewBattleList.gone()
            if (it.status == 1) {
                binding.recyclerViewBattle.visible()
                binding.llEmpty.root.gone()
                it.data?.battle?.let { it1 -> arrayBattle.addAll(it1) }
                mAdapter?.notifyDataSetChanged()
            } else {
                binding.recyclerViewBattle.gone()
                binding.llEmpty.root.visible()
            }
        })

        homeViewModel.gamesListObserver.observe(this, Observer {
            binding.mShimmerViewOtherPopularGame.gone()
            binding.recyclerViewHomeGame.visible()
            if (it.status == 1) {
                arrayGames.clear()
                it.data.games.forEach {
                    if (it.gameId != games?.gameId && it.gameCommingSoon == "N") {
                        arrayGames.add(it)
                    }
                    if (it.gameId == games?.gameId) {
                        (activity as GameActivity).binding.textViewOnlinePlayer.text =
                            it.totalOnlineUsers + " Online"
                        this.games = it
                    }
                }
                if (arrayGames.size == 0) {
                    binding.textViewBestGamesPlay.gone()
                }
                homeAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun setAdapter() {
        mAdapter = BattlesGameAdapter(requireContext(), arrayBattle) {
            startActivityCustom(
                IntentHelper.getGameDetailsScreenIntent(
                    requireContext(),
                    games!!,
                    it
                )
            )
        }

        var mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewBattle.layoutManager = mLayoutManager
        binding.recyclerViewBattle.adapter = mAdapter
        binding.recyclerViewBattle.isNestedScrollingEnabled = false

        var linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewHomeGame.layoutManager = linearLayoutManager
        homeAdapter = GameBattleAdapter(requireContext(), arrayGames) {
            startActivityCustom(IntentHelper.getGameScreenIntent(requireContext(), it))
            (activity as GameActivity).finish()
        }
        binding.recyclerViewHomeGame.adapter = homeAdapter
        OverScrollDecoratorHelper.setUpOverScroll(
            binding.recyclerViewHomeGame,
            OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
        )
        binding.recyclerViewHomeGame.isNestedScrollingEnabled = false
    }

//    fun getBattlesCall(wallype: String,isScroll:Boolean?=false) {
//        this.leaderType = wallype
//        if (isScroll==false){
//            page = 1
//        }
////       showProgress()
//        homeViewModel.getBattles(games?.gameId!!.toInt(),page)
//    }
}
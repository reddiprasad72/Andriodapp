package com.bluboy.android.presentation.game.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.data.models.*
import com.bluboy.android.databinding.FragmentTournamentGameBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.game.adapters.GameCommonAdapter
import com.bluboy.android.presentation.game.adapters.TournamentGameAdapter
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.bouncyEffect.OverScrollDecoratorHelper
import org.koin.android.viewmodel.ext.android.sharedViewModel

class TournamentGameFragment : BaseFragment() {

    private var leaderType = "Tournaments"
    private var mAdapter: TournamentGameAdapter? = null
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var arrayGames = ArrayList<DummyGames>()
    private var arrayTournament = ArrayList<BattleDymmy>()
    private var games: GamesData? = null
    private var page = 1
    private lateinit var homeAdapter: GameCommonAdapter
    private lateinit var binding: FragmentTournamentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTournamentGameBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arrayTournament.clear()
        arrayTournament.add(BattleDymmy("2", "Tournament 1", "101"))
        arrayTournament.add(BattleDymmy("2", "Tournament 2", "102"))
        arrayTournament.add(BattleDymmy("2", "Tournament 3", "103"))
        arrayTournament.add(BattleDymmy("2", "Tournament 4", "104"))
        setAdapter()
    }

    fun getGameData(games: GamesData) {
        this.games = games
    }

    private fun observer() {

    }

    private fun setAdapter() {
        mAdapter = TournamentGameAdapter(requireContext(), arrayTournament, {
//            startActivityCustom(IntentHelper.getGameDetailsScreenIntent(context!!,games!!,it))
        })

        var mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTournament.layoutManager = mLayoutManager
        binding.recyclerViewTournament.adapter = mAdapter



        binding.recyclerViewTournament.isNestedScrollingEnabled = false

        arrayGames.clear()
//        arrayGames.add(DummyGames(1, R.drawable.g_one,"Archery","https://win9pro.staging-server.in/games/html_game/cars_pro_race","Portrait"))
//        arrayGames.add(DummyGames(2, R.drawable.g_two,"Bubble Shooter","https://win9pro.staging-server.in/games/html_game/tic_tac_toe","Landscape"))
//        arrayGames.add(DummyGames(3, R.drawable.g_three,"Candy Crush","https://win9pro.staging-server.in/games/html_game/fruit_slash","Landscape"))
//        arrayGames.add(DummyGames(4, R.drawable.g_four,"Fruit Chop","https://win9pro.staging-server.in/games/html_game/flippy_fish","Portrait"))
//        arrayGames.add(DummyGames(5, R.drawable.g_five,"Runner","https://win9pro.staging-server.in/games/html_game/cars_pro_race","Portrait"))

        var linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewHomeGame.layoutManager = linearLayoutManager
        homeAdapter = GameCommonAdapter(requireContext(), arrayGames) {
//            startActivityCustom(IntentHelper.getGameScreenIntent(context!!,it))
//            (activity as GameActivity).finish()
        }

//        val spanCount = 3 // 3 columns
//        val spacing = Math.round(15 * getResources().getDisplayMetrics().density) //90 // 50px  //Math.round(30 * getResources().getDisplayMetrics().density)
//        val includeEdge = true
//        recyclerViewHomeGame.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))


        binding.recyclerViewHomeGame.adapter = homeAdapter

        OverScrollDecoratorHelper.setUpOverScroll(
            binding.recyclerViewHomeGame,
            OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
        )

        binding.recyclerViewHomeGame.isNestedScrollingEnabled = false


//        rcv_walletHistory.addOnScrollListener(object : EndlessPaginationScrollListener() {
//            override fun requestNewPage() {
//                super.requestNewPage()
//                if (walletHistoryRs?.data?.isHaveMoreRecords.equals("Yes") && !isInProgress) {
//                    page++
//                    getWalletPagination()
//                }
//            }
//        })

//        nestedScrollViewTournamentHistory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
//                // end of the scroll view
//                if (leaderboardRs?.data?.isHaveMoreRecords.equals("Yes") && !isInProgress) {
//                    page++
//                    getLeaderBoards(leaderType,true)
//                }
//            }
//        })
    }

//    fun getLeaderBoards(wallype: String,isScroll:Boolean?=false) {
//        this.leaderType = wallype
//        if (isScroll==false){
//            page = 1
//        }
//
//       showProgress()
//        homeViewModel.getLeaderBoard(leaderType, page)
//    }


}
package com.bluboy.android.presentation.game.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.Battle
import com.bluboy.android.data.models.DummyGames
import com.bluboy.android.databinding.FragmentPrizesBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.game.adapters.PrizesAdapter
import com.bluboy.android.presentation.home.adapter.HomeAdapter

class PrizesFragment : BaseFragment() {

    private var leaderType = "Prize"
    private var mAdapter: PrizesAdapter? = null
    var winAmount = ""

    private var battle: Battle? = null

    private var arrayGames = ArrayList<DummyGames>()

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var binding: FragmentPrizesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrizesBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAdapter()

        binding.textViewPoint.text = getString(R.string.x_price_rupees, winAmount)
    }

    private fun observer() {

    }

    fun getGameData(games: Battle) {
        this.battle = games
    }

    private fun setAdapter() {
        mAdapter = PrizesAdapter(battle) {

        }

        var mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPrizes.layoutManager = mLayoutManager
        binding.recyclerViewPrizes.adapter = mAdapter
        binding.recyclerViewPrizes.isNestedScrollingEnabled = false
    }

    fun getLeaderBoards(mAmount: String, isScroll: Boolean? = false) {
        this.winAmount = mAmount
        if (binding.textViewPoint != null)
            binding.textViewPoint.text = getString(R.string.x_price_rupees, mAmount)
    }
}
package com.bluboy.android.presentation.game.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityGameHistoryBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.game.fragments.BattlesHistoryFromHomeFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel

class GameHistoryFromHomeActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private lateinit var binding: ActivityGameHistoryBinding
    private val leaderboardDaily1 = BattlesHistoryFromHomeFragment()
    private var leaderType = "Battles"
    private var page = 1
    private var selected = 0

    //All Game wise History

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    fun setToolBar() {

        binding.toolbar.txtHeader.text = getString(R.string.label_game_history)
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        attachObserver()
      /*  binding.tvBattle.setOnClickListener {
            binding.viewPager.setCurrentItem(0, true)
            delSelectAll()
            selected = 0
        }

        binding.tvTournament.setOnClickListener {
            binding.viewPager.setCurrentItem(1, true)
            delSelectAll()
            selected = 1
        }*/
    }

    override fun onResume() {
        super.onResume()
        setupViewPager()
       // delSelectAll()
    }

    private fun setupViewPager() {
        val pageAdapter = PageAdapter(supportFragmentManager)
        pageAdapter.add(leaderboardDaily1, getString(R.string.label_battle))
       // pageAdapter.add(leaderboardDaily2, getString(R.string.label_tournament))
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.adapter = pageAdapter
        binding.viewPager.currentItem = 0
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                selected = position
                when (position) {
                    0 -> {
                        page = 0
                        leaderType = getString(R.string.label_battle)
                        leaderboardDaily1.getBattleHistory()
                        //delSelectAll()
                    }
                    1 -> {
                        page = 1
                        leaderType = getString(R.string.label_tournament)
                        //delSelectAll()
                    }
                }
            }
        })
    }

   /* private fun delSelectAll() {
        binding.viewBattle.invisible()
        binding.viewTournament.invisible()
        binding.tvBattle.isSelected = false
        binding.tvTournament.isSelected = false
        when (selected) {
            0 -> {
                binding.viewBattle.visible()
                binding.tvBattle.isSelected = true
            }
            1 -> {
                binding.viewTournament.visible()
                binding.tvTournament.isSelected = true
            }
        }
    }*/

    class PageAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val tabNames: java.util.ArrayList<String> = java.util.ArrayList()
        private val fragments: java.util.ArrayList<Fragment> = java.util.ArrayList()

        fun add(fragment: Fragment, title: String) {
            tabNames.add(title)
            fragments.add(fragment)
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabNames[position]
        }
    }

    private fun attachObserver() {
    }
}

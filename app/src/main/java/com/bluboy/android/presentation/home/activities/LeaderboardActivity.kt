package com.bluboy.android.presentation.home.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityLeaderboardBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.home.fragments.leaderboard.LeaderboardDailyFragment
import com.bluboy.android.presentation.home.fragments.leaderboard.LeaderboardMontlyFragment
import com.bluboy.android.presentation.home.fragments.leaderboard.LeaderboardWeeklyFragment
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class LeaderboardActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private lateinit var binding: ActivityLeaderboardBinding

    private val leaderboardDaily1 = LeaderboardDailyFragment()
    private val leaderboardDaily2 = LeaderboardWeeklyFragment()
    private val leaderboardDaily3 = LeaderboardMontlyFragment()


    var selectedTab by Delegates.observable(-1) { property, oldValue, newValue ->
        if (oldValue != newValue) {
            defaultTabInit(newValue)
            binding.viewPager.setCurrentItem(newValue, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()


        setupViewPager()
        /*BounceView.addAnimTo(binding.tab1Active)
        BounceView.addAnimTo(binding.tab2Active)
        BounceView.addAnimTo(binding.tab3Active)*/

        binding.tool.txtHeader.text = getString(R.string.label_leaderboard)
        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        binding.tab1Inactive.setSafeOnClickListener {
            //Daily
            selectedTab = 0
        }

        binding.tab2Inactive.setSafeOnClickListener {
            //Weekly
            selectedTab = 1
        }

        binding.tab3Inactive.setSafeOnClickListener {
            //Monthly
            selectedTab = 2
        }
        selectedTab = 0
    }

    private fun defaultTabInit(pos: Int) {
        binding.apply {
            tab1Active.invisible()
            tab2Active.invisible()
            tab3Active.invisible()
        }

        when (pos) {
            0 -> {
                binding.tab1Active.visible()
                binding.tab1Inactive.invisible()
                binding.tab2Inactive.visible()
                binding.tab3Inactive.visible()
            }
            1 -> {
                binding.tab2Active.visible()
                binding.tab2Inactive.invisible()
                binding.tab1Inactive.visible()
                binding.tab3Inactive.visible()
            }
            2 -> {
                binding.tab3Active.visible()
                binding.tab3Inactive.invisible()
                binding.tab1Inactive.visible()
                binding.tab2Inactive.visible()
            }
        }
    }

    private fun setupViewPager() {
        val pageAdapter = PageAdapter(supportFragmentManager)

        pageAdapter.add(leaderboardDaily1, getString(R.string.daily))
        pageAdapter.add(leaderboardDaily2, getString(R.string.weekly))
        pageAdapter.add(leaderboardDaily3, getString(R.string.monthly))

        binding.viewPager.offscreenPageLimit = 3
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
                selectedTab = position
            }
        })
    }

    class PageAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val tabNames = arrayListOf<String>()
        private val fragments = arrayListOf<Fragment>()

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
}
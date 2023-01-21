package com.bluboy.android.presentation.game.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bluboy.android.R
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.databinding.ActivityGameBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.game.fragments.BattlesGameFragment
import com.bluboy.android.presentation.game.fragments.TournamentGameFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class GameActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private var games: GamesData? = null
    lateinit var binding: ActivityGameBinding
    private val leaderboardDaily1 = BattlesGameFragment()
    private val leaderboardDaily2 = TournamentGameFragment()
    private var leaderType = "Battles"
    private var selected = 0
    var gameBundle = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        games = intent?.getParcelableExtra<GamesData>(AppConstant.GAME)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()

        gameBundle = when (games?.game_key) {
            "fruit_ninja" -> {
                "fruit_chop"
            }
            "candy_crush" -> {
                "candycrush"
            }
            "bluboy_runner" -> {
                "runner_game"
            }
            else -> {
                "templerun"
            } // "9"
        }
    }

    fun setToolBar() {
        binding.textViewOnlinePlayer.text = games?.totalOnlineUsers + " Online"
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
        /*BounceView.addAnimTo(binding.toolbar.llHowToPlay)
        BounceView.addAnimTo(binding.toolbar.imageViewGameHistory)*/
        binding.toolbar.llHowToPlay.setSafeOnClickListener {
            startActivityCustom(
                IntentHelper.getTutorialScreenIntent(
                    this,
                    games?.game_key.toString()
                )
            )
        }

        binding.toolbar.imageViewGameHistory.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getGameHistoryScreenIntent(this, games!!))
        }
    }

    private fun init() {
        attachObserver()
        games?.gameBanner?.let { loadImage(binding.imageViewGameBg, it) }
        loadCornerImageGame(binding.imageViewGame, games?.gameImage)
        binding.textViewGameName.text = games?.gameName

                leaderboardDaily1.getGameData(games!!)
    }

    override fun onResume() {
        super.onResume()
        val file = File(
            this@GameActivity.getExternalFilesDir(null)?.absolutePath
                    + File.separator + "UnityCache/Shared/$gameBundle"
        )

        setupViewPager()
    }

    private fun setupViewPager() {
        val pageAdapter = PageAdapter(supportFragmentManager)
        pageAdapter.add(leaderboardDaily1, getString(R.string.label_battle))

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
                        leaderType = getString(R.string.label_battle)
                    }
                    1 -> {
                        leaderType = getString(R.string.label_tournament)
                    }
                }
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            binding.viewPager.setCurrentItem(0, true)
            selected = 0
            leaderType = getString(R.string.label_battle)
        }, 150)
    }

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

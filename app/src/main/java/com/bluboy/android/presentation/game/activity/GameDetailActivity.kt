package com.bluboy.android.presentation.game.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.location.LocationListener
import com.bluboy.android.R
import com.bluboy.android.data.models.Battle
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.databinding.ActivityGameDetailBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.core.location.MyLocationListener
import com.bluboy.android.presentation.core.location.MyLocationManager
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.dialog.ConfirmPayDialogFragment
import com.bluboy.android.presentation.game.fragments.LeaderboardGameFragment
import com.bluboy.android.presentation.game.fragments.PrizesFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import com.unity3d.player.UnityPlayerActivity
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import java.io.File
import java.util.*
import kotlin.properties.Delegates

class GameDetailActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private var games: GamesData? = null
    private var battle: Battle? = null
    var retrofit: Retrofit? = null
    private lateinit var binding: ActivityGameDetailBinding
    private val prizeFragment = PrizesFragment()
    private val leaderboardDaily2 = LeaderboardGameFragment()
    private var leaderType = "Prize"
    private var page = 1
    private var selected = 0
    private var mLocationProviderHelper: LocationProviderHelper? = null
    private var mLocation: Location? = null
    private var myLocationManager: MyLocationManager? = null
    private var locationPermissionGranted = false
    private var state = ""
    var gameBundle = ""

    var selectedTab by Delegates.observable(-1) { property, oldValue, newValue ->
        if (oldValue != newValue) {
            defaultTabInit(newValue)
            binding.viewPager.setCurrentItem(newValue, true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        games = intent?.getParcelableExtra<GamesData>(AppConstant.GAME)
        battle = intent?.getParcelableExtra<Battle>(AppConstant.BATTLE)
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

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun init() {
        attachObserver()
        games?.gameBanner?.let { loadImage(binding.imageViewGameBg, it) }
        loadCornerImageGame(binding.imageViewGame, games?.gameImage)
        binding.textViewGameName.text = games?.gameName
        if (battle?.tournamentEntryFees == "0") {
            binding.buttonFee.text = "FREE"
        } else {
            binding.buttonFee.text =
                getString(R.string.join_battle_rupees, battle?.tournamentEntryFees)
        }
        binding.textViewEntryFee.text =
            getString(R.string.x_price_rupees, battle?.tournamentEntryFees)
        binding.textViewBattleName.text = battle?.tournamentName
//        if (battle?.gameId == "1") {
//        loadImage(binding.imageViewBattle, R.drawable.battle_img_square)
//        } else {
//            loadImage(imageViewBattle, R.drawable.tournament_img_square)
//        }

        /*BounceView.addAnimTo(binding.buttonFee)
        BounceView.addAnimTo(binding.llPayEntryFee)*/

        binding.buttonFee.setSafeOnClickListener {
            callCheckStateApi()
        }

        binding.buttonPlay.setSafeOnClickListener {
            callCheckStateApi()
        }

        binding.llPayEntryFee.setSafeOnClickListener {
            callCheckStateApi()
        }

        binding.buttonAddCash.setSafeOnClickListener {

            startActivityCustom(
                IntentHelper.getHomeScreenIntent(
                    context = this,
                    addMoney = true
                )
            )
        }

        binding.tab1Inactive.setSafeOnClickListener {
            //prize
            selectedTab = 0
        }

        binding.tab2Inactive.setSafeOnClickListener {
            //Leaderboard
            selectedTab = 1
        }

        selectedTab = 0

        /*binding.tvBattle.setOnClickListener {
            binding.viewPager.setCurrentItem(0, true)
            delSelectAll()
            selected = 0
        }
        binding.tvTournament.setOnClickListener {
            binding.viewPager.setCurrentItem(1, true)
            delSelectAll()
            selected = 1
        }*/

        prizeFragment.getGameData(battle!!)
        prizeFragment.winAmount = battle?.tournamentTotalWinningAmount.toString()
//        leaderboardDaily2.getGameData(games!!)
    }

    private fun callCheckStateApi() {
//        if(locationPermissionGranted && state != "") {
//            showProgress()
//            homeViewModel.checkStateStatus(state)
//        }else{
        checkForPermission()
//        }
    }

    override fun onResume() {
        super.onResume()
        val file = File(
            this@GameDetailActivity.getExternalFilesDir(null)?.absolutePath
                    + File.separator + "UnityCache/Shared/$gameBundle"
        )

        setupViewPager()
//        delSelectAll()
        showProgress()
        page = 1
        homeViewModel.getProfile()
        homeViewModel.getGameList(page)
    }

    private fun startGameCheckWalletBalance() {
        if (battle?.tournamentEntryFees == "0") {
            showProgress()
            homeViewModel.getTournamentJoinCheck(battle?.tournamentId.toString())
        } else {
            /*var tempBonus = (battle?.tournamentBonusLimit?.substring(
                0,
                battle?.tournamentBonusLimit?.length!! - 1
            )?.toFloat())*/
            var tempBonus = battle?.tournamentBonusLimit?.toFloat()
            var tempBonus2 = battle?.tournamentEntryFees?.toFloat()
            var finalAns = (tempBonus!! * tempBonus2!!) / 100
            Log.i("FinalAns = ", finalAns.toString())
            if (battle?.tournamentEntryFees!!.toInt() <= (PrefKeys.getUserCommon()?.userDepositBalance?.toFloat()!! + PrefKeys.getUserCommon()?.userWinningAmountBalance?.toFloat()!! + finalAns)) {
                homeViewModel.getTournamentPriceBreakDown(battle?.tournamentId.toString())
            } else {
                toastError("Insufficient wallet balance")
            }
        }
    }

    private fun setupViewPager() {
        val pageAdapter = PageAdapter(supportFragmentManager)
        pageAdapter.add(prizeFragment, getString(R.string.label_prize))
        pageAdapter.add(leaderboardDaily2, getString(R.string.label_leaderboard))
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.adapter = pageAdapter
        binding.viewPager.currentItem = selectedTab
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
                prizeFragment.winAmount = battle?.tournamentTotalWinningAmount.toString()
                when (position) {
                    0 -> {
                        leaderType = "Prize"
                        prizeFragment.getLeaderBoards(battle?.tournamentTotalWinningAmount.toString())
                    }
                    1 -> {
                        leaderType = "Leaderboard"
                        leaderboardDaily2.getLeaderBoards(games?.gameId.toString())
                    }
                    /*2 -> {
                        leaderType = "Paytm"
                    }*/
                }
            }
        })
    }


    class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
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
        homeViewModel.tournamentPriceBrakDownObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                ConfirmPayDialogFragment.showDialog(
                    supportFragmentManager,
                    battle?.tournamentEntryFees.toString(),
                    it.data.deduction_from_wallet_win_amount.toString(),
                    it.data.deduction_from_bonus_amount.toString(),
                    it.data.total_payable.toString()
                ) {
                    homeViewModel.getTournamentJoinCheck(battle?.tournamentId.toString())
                }
            } else {
                toastError(it.message!!)
            }
        })

        // State check
        homeViewModel.checkStateStausResponse.observe(this, Observer {
            hideProgress()
            if (it.status == 0) {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name),
                    "Administrative have blocked games in $state",
                    getString(R.string.ok),
                    "",
                    {
                        finish()
                    },
                    {

                    },
                    {
                        finish()
                    })
            } else {
                startGameCheckWalletBalance()
            }
        })

        // Join Check
        homeViewModel.tournamentJoinCheckObserver.observe(this, Observer { it ->
            hideProgress()
            if (it.status == 1) {
//                toastError("Game is under development")
                startActivity(Intent(
                    this@GameDetailActivity, UnityPlayerActivity::class.java
                ).also { it1 ->
                    it1.putExtra("gameId", games?.gameId)
                    it1.putExtra("game_key", games?.game_key)
                    it1.putExtra("playerId", PrefKeys.getUserCommon()?.userName)
                    it1.putExtra("TournamentId", battle?.tournamentId)
                    it1.putExtra("Timer", "")
                    it1.putExtra("auth_key", PrefKeys.getAuthKey())
                    it1.putExtra("profileUrl", PrefKeys.getUserCommon()?.profilePic)
                    it1.putExtra("room_join_fee", battle?.tournamentEntryFees)
                    it1.putExtra("game_version", games?.gameVersion)

                    when {
                        games?.game_key.equals("fruit_ninja") -> {
                            it1.putExtra(
                                "show_tutorial",
                                PrefKeys.fruitTutorialFlag
                            )
                            PrefKeys.fruitTutorialFlag = "false"
                        }
                        games?.game_key.equals("bluboy_runner") -> {
                            it1.putExtra(
                                "show_tutorial",
                                PrefKeys.runnerTutorialFlag
                            )
                            PrefKeys.runnerTutorialFlag = "false"
                        }
                        games?.game_key.equals("candy_crush") -> {
                            it1.putExtra(
                                "show_tutorial",
                                PrefKeys.candyCrushTutorialFlag
                            )
                            PrefKeys.candyCrushTutorialFlag = "false"
                        }
                    }

                    it1.putExtra("game_image", games?.gameImage)
                    it1.putExtra("game_banner", games?.gameBanner)
                    it1.putExtra("game_name", games?.gameName)
                    it1.putExtra("app_version", getAppVersion())
                })
            } else {
                toastError(it.message.toString())
            }
        })

        //Profile
        homeViewModel.profileObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                it?.user?.let { it1 -> PrefKeys.setUser(it1) }
                if (battle?.tournamentEntryFees == "0") {
                    binding.buttonPlay.visible()
                    binding.llPayEntryFee.gone()
                    binding.buttonAddCash.gone()
                } else {
                    /*var tempBonus = (battle?.tournamentBonusLimit?.substring(
                        0,
                        battle?.tournamentBonusLimit?.length!! - 1
                    )?.toFloat())*/
                    var tempBonus = battle?.tournamentBonusLimit?.toFloat()

                    Log.i("FinalAns bonus limit= ", tempBonus.toString())
                    var tempBonus2 = battle?.tournamentEntryFees?.toFloat()
                    Log.i("FinalAns entryfeebonu= ", tempBonus2.toString())
                    var finalAns = (tempBonus!! * tempBonus2!!) / 100
                    Log.i("FinalAns = ", finalAns.toString())
                    Log.i("FinalAns P entryFee =>", battle?.tournamentEntryFees.toString())
                    Log.i(
                        "FinalAns P deposite=>",
                        (PrefKeys.getUserCommon()?.userDepositBalance.toString())
                    )
                    Log.i(
                        "FinalAns P winning=>",
                        (PrefKeys.getUserCommon()?.userWinningAmountBalance.toString())
                    )
                    Log.i("FinalAns P Bonus=>", finalAns.toString())
                    Log.i(
                        "FinalAns P total bal =>",
                        (PrefKeys.getUserCommon()?.userDepositBalance?.toFloat()!! + PrefKeys.getUserCommon()?.userWinningAmountBalance?.toFloat()!! + finalAns).toString()
                    )
                    if (battle?.tournamentEntryFees!!.toInt() <= (PrefKeys.getUserCommon()?.userDepositBalance?.toFloat()!! + PrefKeys.getUserCommon()?.userWinningAmountBalance?.toFloat()!! + finalAns) &&
                        battle?.tournamentEntryFees!!.toInt() <= (PrefKeys.getUserCommon()?.userDepositBalance?.toFloat()!! + PrefKeys.getUserCommon()?.userWinningAmountBalance?.toFloat()!! + PrefKeys.getUserCommon()?.userBonusBalance?.toFloat()!!)

                    ) {
                        binding.buttonPlay.gone()
                        binding.llPayEntryFee.visible()
                        binding.buttonAddCash.gone()
                    } else {
                        binding.buttonPlay.gone()
                        binding.llPayEntryFee.gone()
                        binding.buttonAddCash.visible()
                    }
                }
            } else {
                toastError(it.message.toString())
            }
        })

        homeViewModel.gamesListObserver.observe(this, Observer { it ->
            hideProgress()
            if (it.status == 1) {
                it.data.games.forEach { it1 ->
                    if (it1.gameId == games?.gameId) {
                        this.games = it1
                        binding.textViewOnlinePlayer.text = it1.totalOnlineUsers + " Online"
                    }
                }
            }
        })
    }

    // Location Permission
    private fun checkForPermission() {
        MayI.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .onRationale(this::permissionRationaleMulti)
            .onResult(this::permissionResultMulti)
            .onErrorListener(this::inCaseOfError)
            .check()
    }

    private fun permissionRationaleMulti(
        permissions: Array<PermissionBean>,
        token: PermissionToken
    ) {
        showDialog(getString(R.string.app_name),
            getString(R.string.permission_location),
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                token.continuePermissionRequest()
            })
    }

    private fun inCaseOfError(e: Exception) {
        showDialog(getString(R.string.app_name),
            "Error for permission : " + e.message,
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
    }

    private fun permissionResultMulti(
        permissions: Array<PermissionBean>
    ) {
        val isAllPermanentlyDenied = permissions.all {
            it.isPermanentlyDenied
        }
        if (isAllPermanentlyDenied) {
            showDialog(
                getString(R.string.app_name),
                getString(R.string.permission_always_denied),
                getString(R.string.settings), { dialog, which ->
                    dialog.dismiss()
                    openPermissionSettings()
                }, getString(R.string.cancel), { dialog, which ->
                    dialog.dismiss()
                    finishAffinity()
                }
            )
            return
        }
        val isAllPermissionGranted = permissions.all {
            it.isGranted
        }
        if (isAllPermissionGranted) {
            locationPermissionGranted = true
            showProgress()
            //get current Location
            mLocationProviderHelper = LocationProviderHelper(this, LocationListener {
                this.mLocation = it
                Logger.e("Location : " + it.latitude + "," + it.longitude)
                try {
                    mLocationProviderHelper?.stopLocationUpdates()
                } catch (e: Exception) {
                    Log.e("GameDetailActivity", "Location Update Exception :" + e.toString())
                }
            })
            mLocationProviderHelper?.createLocationRequest()
            myLocationManager = MyLocationManager(this)
            myLocationManager?.myLocationManager = object : MyLocationListener {
                override fun onLocationReceived(location: Location) {
                    try {
                        var geocoder = Geocoder(this@GameDetailActivity, Locale.ENGLISH)
                        var addresses: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses != null && addresses.size > 0) {
                            Log.i("State", addresses[0]?.adminArea)
                            if (addresses[0].adminArea != "") {
                                state = addresses[0].adminArea
                                homeViewModel.checkStateStatus(state)
                            } else {
                                toastError("Trouble for access your state, Please try after some time")
                            }
                        } else {
                            if (state == "") {
                                hideProgress()
                                toastError("No any state found for your location")
                            }
                        }
                        Log.e(
                            "GameDetailActivity",
                            "Location :" + location.latitude + " " + location.longitude
                        )
                        myLocationManager?.stopLocation()
                    } catch (e: Exception) {
                        hideProgress()
                        Log.e("GameDetailActivity", "Location Exception :" + e.toString())
                    }
                }

                override fun onLocationError() {
                    hideProgress()
                    toastError("Trouble for access location, Please try after some time")
                }
            }
            myLocationManager?.startLocation()
        } else {
        }
    }

    private fun defaultTabInit(pos: Int) {
        binding.apply {
            tab1Active.invisible()
            tab2Active.invisible()
//            tab3Active.invisible()
        }
        when (pos) {
            0 -> {
                binding.tab1Active.visible()
                binding.tab1Inactive.invisible()
                binding.tab2Inactive.visible()
            }
            1 -> {
                binding.tab2Active.visible()
                binding.tab2Inactive.invisible()
                binding.tab1Inactive.visible()
            }
//            2 -> binding.tab3Active.visible()
        }
    }

}

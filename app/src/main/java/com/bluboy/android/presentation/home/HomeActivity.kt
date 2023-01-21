package com.bluboy.android.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationListener
import com.bluboy.android.R
import com.bluboy.android.data.models.IpResponse
import com.bluboy.android.databinding.ActivityHomeBinding
import com.bluboy.android.databinding.NavHeaderBinding
import com.bluboy.android.domain.network.ServiceApiCall.WebApiCall
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.core.location.MyLocationListener
import com.bluboy.android.presentation.core.location.MyLocationManager
import com.bluboy.android.presentation.dialog.LogoutDialogFragment
import com.bluboy.android.presentation.dialog.PopUpRedirectionHomeDialogFragment
import com.bluboy.android.presentation.dialog.YourClaimDialogFragment
import com.bluboy.android.presentation.home.fragments.BonusFragment
import com.bluboy.android.presentation.home.fragments.HomeFragment
import com.bluboy.android.presentation.home.fragments.SpinWheelFragment
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.wallet.AddMoneyFragment
import com.bluboy.android.presentation.wallet.WalletFragment
import com.pixplicity.easyprefs.library.Prefs
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.layout_bottom_menu.*
import kotlinx.android.synthetic.main.nav_header.*
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class HomeActivity : BaseActivity(), View.OnClickListener {
    companion object {
        var bonusAmt = ""
        var locationTime = ""
    }

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel

    private lateinit var binding: ActivityHomeBinding
    /*private lateinit var navViewHeaderBinding: NavHeaderBinding*/

    private var currentSelection = 0
    internal var doubleBackToExitPressedOnce = false

    val CLICK_INTERVAL = 500
    var doubleTapTime: Long = 0
    var defaultIconSize = 0.65f
    var increasedIconSize = 1.2f
    var openfromDrawer = false
    var openfromWallet = false

    private var page: Int = 1
    private var hasMore: Boolean? = false
    private var isLoading: Boolean? = false

    private var addMoney = false
    private var walletScreen = false
    private var openFromSpin = false
    private var claimDialog = false
    private var openFromSplash = false

    private var mLocationProviderHelper: LocationProviderHelper? = null
    private var mLocation: Location? = null
    private var myLocationManager: MyLocationManager? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()

//        startActivity(Intent(this@HomeActivity,KycActivity::class.java))

        var openFromPush = intent?.getBooleanExtra(AppConstant.OPEN_FROM_PUSH, false)!!
        if (openFromPush) {
            startActivityCustom(IntentHelper.getNotificationScreenIntent(this@HomeActivity))
        }
        init()
//        val sdk = android.os.Build.VERSION.SDK_INT

        val viewHeader = binding.navView.getHeaderView(0)
        /*navViewHeaderBinding = NavHeaderBinding.bind(viewHeader)*/

        textViewNavHome.setBackgroundDrawable(this.resources.getDrawable(R.drawable.side_nav_selection_bg))

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        //DRAWER NAVIGATION
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
                hideKeyboard()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerOpened(drawerView: View) {}
        })

        imageViewProfileNav.setOnClickListener(this)
        textViewUserNameDrawer.setOnClickListener(this)
        textViewViewProfile.setOnClickListener(this)

        /*navViewHeaderBinding.*/tvAppVersion.text = getAppVersion()

        textViewNavMyWallet.setOnClickListener {
            openfromDrawer = true
            closeDrawer()
            showWalletFragment()
        }

        textViewNavLogout.setOnClickListener {
            closeDrawer()
            LogoutDialogFragment.showDialog(supportFragmentManager, {
                //Logout
                showProgress()
                homeViewModel.performLogout()
            }, {
                //cancel
            })
        }

        textViewNavHome.setOnClickListener {
            closeDrawer()
//            startActivityCustom(IntentHelper.getLeaderboardFromHomeScreenIntent(this))
        }

        textViewNavLeaderboard.setOnClickListener {
            startActivityCustom(IntentHelper.getLeaderboardFromHomeScreenIntent(this))
        }

        textViewNavGameHistory.setOnClickListener {
            startActivityCustom(IntentHelper.getGameHistoryFromHomeScreenIntent(this))
        }

        textViewNavSettings.setOnClickListener {
            startActivityCustom(IntentHelper.getSettingsScreenIntent(this))
        }

        textViewNavReferEarn.setOnClickListener {
            startActivityCustom(IntentHelper.getReferAndEarnFromHomeScreenIntent(this))
        }

        textViewNavHowToPlay.setOnClickListener {
            startActivityCustom(IntentHelper.getHowToPlayScreenIntent(this))
        }
    }

    private fun init() {

        attachObserver()
        initClickListener()
        homeViewModel.getSpinList()
        currentSelection = 3
        val homeFragment = HomeFragment()
        val supportFragmentManager = supportFragmentManager
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(container.id, homeFragment, currentSelection.toString())
        transaction.commit()
        // setBottomMenu()
        increaseBottomNavigationIconSize(bottomNavHome)

        claimDialog = intent?.getBooleanExtra(AppConstant.OPEN_FROM_SIGNUP, false)!!
        if (claimDialog) {
            YourClaimDialogFragment.showDialog(
                supportFragmentManager,
                PrefKeys.getUserCommon()?.userBonusBalance.toString()
            ) {
            }
        }

        addMoney =
            intent?.getBooleanExtra(
                AppConstant.OPEN_ADDMONEY,
                false
            )!!
        if (addMoney) {
            showAddMoneyFragment()
        }

        //open wallet from profile
        openFromSpin =
            intent?.getBooleanExtra(
                AppConstant.OPEN_WALLET_FROM_SPIN,
                false
            )!!

        //open wallet from profile
        walletScreen =
            intent?.getBooleanExtra(
                AppConstant.OPEN_WALLET_FROM_PROFILE,
                false
            )!!

        if (walletScreen || openFromSpin) {
            showWalletFragment()
        }

        openFromSplash = intent?.getBooleanExtra(AppConstant.OPEN_FROM_SPLASH, false)!!
        if (PrefKeys.getVariableData()?.POPUP_NOTIFICATION_SHOW == "YES"
            && openFromSplash && !claimDialog && !addMoney
        ) {
            PopUpRedirectionHomeDialogFragment.showDialog(
                supportFragmentManager,
                PrefKeys.getVariableData()!!
            ) {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCircleImage(
            /*navViewHeaderBinding.*/imageViewProfileNav,
            PrefKeys.getUserCommon()?.profilePic
        )
        /*navViewHeaderBinding.*/
        textViewUserNameDrawer.text = PrefKeys.getUserCommon()?.userName
        homeViewModel.getSettingVar()
        val retrofit = Retrofit.Builder().baseUrl("https://api.ipify.org").addConverterFactory(
            GsonConverterFactory.create()
        ).build()
        val webApiCall = retrofit.create(WebApiCall::class.java)
        val call: Call<IpResponse> = webApiCall.getIPAddress("json")
        call.enqueue(object : Callback<IpResponse> {
            override fun onResponse(call: Call<IpResponse>, response: Response<IpResponse>) {
                if (PrefKeys.getAuthKey() != "") {
                    homeViewModel.getCheckRootDevice(
                        response.body()?.ip.toString(),
                        "N"
                    )
                }
            }

            override fun onFailure(call: Call<IpResponse>, t: Throwable) {
                Log.i("IpAddress >>", t.message.toString())
            }
        })
        if (PrefKeys.getUserCommon()?.stateId == "0")
            checkForPermission()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageViewProfileNav, R.id.textViewUserName, R.id.textViewViewProfile -> {
                // closeDrawer()
                startActivityCustom(IntentHelper.getProfileScreenIntent(this))
            }
        }
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun openDrawer() {
        loadCircleImage(
            /*navViewHeaderBinding.*/imageViewProfileNav,
            PrefKeys.getUserCommon()?.profilePic
        )

        /*navViewHeaderBinding.*/textViewUserNameDrawer.text = PrefKeys.getUserCommon()?.userName
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListener() {
//        BounceView.addAnimTo(llMyGames)
//        BounceView.addAnimTo(bottomNavHome)
//        BounceView.addAnimTo(llLeaderboard)
//        BounceView.addAnimTo(llReferAndRun)

        viewSpin.setSafeOnClickListener {
            showSpinFragment()
        }

        viewAddMoney.setSafeOnClickListener {
            openfromWallet = false
            addMoney = false
            showAddMoneyFragment()
        }

        viewHome.setSafeOnClickListener {
            showHomeFragment()
        }

        viewWallet.setSafeOnClickListener {
            openfromDrawer = false
            showWalletFragment()
        }

        viewBonus.setSafeOnClickListener {
            showBonusFragment()
        }
        /*llLeaderboard.setOnClickListener {
            if ((doubleTapTime + CLICK_INTERVAL) < System.currentTimeMillis()) {
                doubleTapTime = System.currentTimeMillis()
                showLeaderboardFragment()
            }
        }*/
        /*llReferAndRun.setOnClickListener {
            if ((doubleTapTime + CLICK_INTERVAL) < System.currentTimeMillis()) {
                doubleTapTime = System.currentTimeMillis()
                showMyReferAndEarnFragment()
            }
        }*/
    }

    fun launchBottomNavFragmentByIndex(index: Int) {
        openfromWallet = true
        currentSelection = index
        if (currentSelection == 2) {
            val addMoneyFragment = AddMoneyFragment()
            changeFragment(addMoneyFragment, currentSelection.toString())

            increaseBottomNavigationIconSize(bottomNavAddMoney)
            //setBottomMenu()
        }
        if (currentSelection == 1) {
            changeFragment(SpinWheelFragment(), currentSelection.toString())
            increaseBottomNavigationIconSize(bottomNavSpinner)
            //setBottomMenu()
        }
    }

    fun showSpinFragment() {
        if (currentSelection != 1) {
            currentSelection = 1
            changeFragment(SpinWheelFragment(), currentSelection.toString())
            increaseBottomNavigationIconSize(bottomNavSpinner)
            //setBottomMenu()
        }
//        BounceView.addAnimTo(bottomNavSpinnerBig)
    }

    fun showAddMoneyFragment() {
        if (currentSelection != 2) {
            currentSelection = 2
            changeFragment(AddMoneyFragment(), currentSelection.toString())

            increaseBottomNavigationIconSize(bottomNavAddMoney)
            //setBottomMenu()
        }
    }

    fun showHomeFragment() {
        if (currentSelection != 3) {
            currentSelection = 3
            changeFragment(HomeFragment(), currentSelection.toString())

            increaseBottomNavigationIconSize(bottomNavHome)
            //setBottomMenu()
        }
//        BounceView.addAnimTo(bottomNavHomeBig)
    }

    fun showWalletFragment() {
        if (currentSelection != 4) {
            currentSelection = 4
            changeFragment(WalletFragment(), currentSelection.toString())

            increaseBottomNavigationIconSize(bottomNavWallet)
            //setBottomMenu()
        }
    }

    fun showBonusFragment() {
        if (currentSelection != 5) {
            currentSelection = 5
            changeFragment(BonusFragment(), currentSelection.toString())

            increaseBottomNavigationIconSize(bottomNavGift)
            //setBottomMenu()
        }
    }

    private fun increaseBottomNavigationIconSize(view: AppCompatImageView) {
        decreaseBottomNavigationIconSize()
        view.scaleX = increasedIconSize
        view.scaleY = increasedIconSize
    }

    private fun decreaseBottomNavigationIconSize() {
        bottomNavSpinner.scaleX = defaultIconSize
        bottomNavSpinner.scaleY = defaultIconSize

        bottomNavAddMoney.scaleX = defaultIconSize
        bottomNavAddMoney.scaleY = defaultIconSize

        bottomNavHome.scaleX = defaultIconSize
        bottomNavHome.scaleY = defaultIconSize

        bottomNavWallet.scaleX = defaultIconSize
        bottomNavWallet.scaleY = defaultIconSize

        bottomNavGift.scaleX = defaultIconSize
        bottomNavGift.scaleY = defaultIconSize
    }

    private fun changeFragment(fragment: Fragment?, tag: String) {
        if (fragment != null) {
            val supportFragmentManager = supportFragmentManager
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(container.id, fragment, tag)
            transaction.commit()
        }
    }

    override fun onBackPressed() {
        if (!PrefKeys.touchEnable) {
            return
        }

        if (addMoney) {
            super.onBackPressed()
        } else if (walletScreen && currentSelection == 4) {
            super.onBackPressed()
        } else if (openFromSpin && currentSelection == 4) {
            showSpinFragment()
        } else
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                closeDrawer()
            } else if (currentSelection != 3) {
                if ((currentSelection == 4) and openfromDrawer) openDrawer()
                if ((currentSelection == 2) and openfromWallet) showWalletFragment() else showHomeFragment()
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }

                this.doubleBackToExitPressedOnce = true
                showMessage(getString(R.string.back_to_exit))
                Handler(Looper.getMainLooper()).postDelayed(
                    { doubleBackToExitPressedOnce = false },
                    2000
                )
            }
    }

    private fun attachObserver() {
        homeViewModel.varSettingRS.observe(this, Observer {
            if (it.status == 1) {
                PrefKeys.setVariableData(it.data!!)
            }
        })

        homeViewModel.updateUserStateResponse.observe(this, Observer {
            if (it.status == 1) {

            }
        })

        homeViewModel.logoutRSLiveData.observe(this, Observer {
            it.apply {
                if (this.status == 1) {
                    hideProgress()
                    Prefs.putString(PrefKeys.AuthKey, "")
//                    PrefKeys.setUser(User())
                    startActivityCustom(
                        IntentHelper.getLoginScreenIntent(
                            context = this@HomeActivity,
                            isClearFlag = true
                        )
                    )
                } else {
                    toastError(this.message!!)
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

            //get current Location
            mLocationProviderHelper = LocationProviderHelper(this, LocationListener {
                this.mLocation = it

//                locationTime= it.time.toString()

                Log.v("globaltime ", locationTime)


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
                        var geocoder = Geocoder(this@HomeActivity, Locale.ENGLISH)
                        var addresses: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses != null && addresses.size > 0) {
                            Log.i("State", addresses[0].adminArea)
                            if (addresses[0].adminArea != "") {
                                homeViewModel.getUpdateUserState(addresses[0].adminArea)
                            } else {
                                toastError("Trouble for access your state, Please try after some time")
                            }
                        } else {
//                            if(state == ""){
//                                hideProgress()
//                                toastError("No any state found for your location")
//                            }
                        }
                        Log.e(
                            "HomeActivity",
                            "Location :" + location.latitude + " " + location.longitude
                        )
                        myLocationManager?.stopLocation()
                    } catch (e: Exception) {
                        hideProgress()
                        Log.e("HomeActivity", "Location Exception :" + e.toString())
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

}

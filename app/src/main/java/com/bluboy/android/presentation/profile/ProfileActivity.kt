package com.bluboy.android.presentation.profile

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.data.models.UserGame
import com.bluboy.android.databinding.ActivityProfileBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.profile.adapter.UserGameAdapter
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private lateinit var profileGameAdapter: UserGameAdapter
    private var arrayGames = ArrayList<UserGame>()
    private lateinit var binding: ActivityProfileBinding
    var kycStatus = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_my_profile)

        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.imageViewSetting.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getSettingsScreenIntent(this))
        }
        binding.toolbar.imgWallet.setSafeOnClickListener {
            startActivityCustom(
                IntentHelper.getHomeScreenIntent(
                    context = this,
                    wallet = true
                )
            )
//            launchBottomNavFragmentByIndex(4)

//            startActivityCustom(IntentHelper.getWalletScreenIntent(this))
        }

        binding.groupBeneficiary.setAllOnClickListener {
            startActivityCustom(IntentHelper.getBeneficiaryManageScreenIntent(this))
        }

        binding.groupKYC.setAllOnClickListener {
            if (!kycStatus)
                startActivityCustom(IntentHelper.getKYCIntent(this, true))
            else
                startActivityCustom(IntentHelper.getKYCDetailIntent(this))
        }
    }

    private fun init() {
        attachObserver()
        binding.imageViewReferEarn.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getReferAndEarnFromHomeScreenIntent(this))
        }
        binding.imageViewEditProfile.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getEditProfileScreenIntent(this))
        }

        binding.textViewReferAndEarn.text =
            getString(R.string.x_price_rupees, PrefKeys.getVariableData()?.referredBonus)

        arrayGames.clear()
        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewProfileGame.layoutManager = linearLayoutManager
        profileGameAdapter = UserGameAdapter(this, arrayGames) {
            val gamesData = GamesData(
                "", it.gameBanner, "",
                "", it.gameId, it.gameImage, it.gameName,
                "", "", "",
                "", "",
                "", "N", "", ""
            )
//            startActivityCustom(IntentHelper.getGameHistoryScreenIntent(this, gamesData))

            if (gamesData.gameCommingSoon == "Y") {
            } else {
                startActivityCustom(
                    IntentHelper.getGameScreenIntent(
                        this@ProfileActivity,
                        gamesData
                    )
                )
            }
        }
        binding.recyclerViewProfileGame.adapter = profileGameAdapter
    }

    override fun onResume() {
        super.onResume()
        showProgress()
        arrayGames.clear()
        homeViewModel.getProfile()
        homeViewModel.getSettingVar()
    }

    private fun attachObserver() {
        homeViewModel.profileObserver.observe(this, Observer {
            hideProgress()
            it?.apply {
                if (this.status == 1) {
                    PrefKeys.setUser(this.user!!)
                    loadImage(binding.imageViewProfile, this.user!!.profilePic)

                    if (this.user!!.name.isNullOrBlank()) {
                        binding.textViewUserName.text = this.user!!.userName
                    } else {
                        binding.textViewUserName.text = this.user!!.name
                    }

                    binding.textViewEmail.text = this.user!!.email
                    binding.textViewMobile.text = this.user!!.phone
                    binding.textViewTotalWinnings.text = getString(
                        R.string.x_price_rupees,
                        this.user!!.userWinningAmountBalance.toString()
                    )
                    binding.textViewGamesWon.text = this.user!!.wonGameCount.toString()
                    binding.textViewGamesPlayed.text = this.user!!.totalGameCount.toString()

                    // KYC data
                    binding.kycStatus.visible()
                    if (this.user?.kycStatus == "APPROVED") {
                        kycStatus = true
                        binding.kycStatus.text = "Approved"
                        binding.forward2.gone()
                    } else if (this.user?.kycStatus == "REJECTED") {
                        kycStatus = false
                        binding.kycStatus.text = "Rejected"
                        binding.forward2.visible()
                    } else if (this.user?.kycStatus == "IN_REVIEW") {
                        kycStatus = true
                        binding.kycStatus.text = "In Review"
                        binding.forward2.gone()
                    } else {
                        kycStatus = false
                        binding.kycStatus.text = "Pending"
                        binding.forward2.visible()
                    }

                    this.user?.userGames?.let { it1 -> arrayGames.addAll(it1) }
                    if (arrayGames.size == 0) {
                        binding.textGames.gone()
                    } else {
                        binding.textGames.visible()
                    }
                    profileGameAdapter.notifyDataSetChanged()
                } else {
                }
            }
        })

        homeViewModel.varSettingRS.observe(this, Observer {
            if (it.status == 1) {
                PrefKeys.setVariableData(it.data!!)
            }
        })
    }
}

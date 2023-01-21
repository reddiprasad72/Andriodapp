package com.bluboy.android.presentation.settings

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.pixplicity.easyprefs.library.Prefs
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivitySettingsBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.LogoutDialogFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel

class SettingActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel

    private lateinit var binding: ActivitySettingsBinding
    private var currentSelection = -1

    private var page: Int = 1
    private var hasMore: Boolean? = false
    private var isLoading: Boolean? = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    fun setToolBar() {
        binding.tool.txtHeader.text = getString(R.string.settings)

        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        attachObserver()
        /*BounceView.addAnimTo(binding.imageViewPushOff)
        BounceView.addAnimTo(binding.imageViewPushOn)*/

        if (PrefKeys.getUserCommon()?.pushNotificationStatus == AppConstant.Yes) {
            binding.imageViewPushOn.visible()
            binding.imageViewPushOff.gone()
        } else {
            binding.imageViewPushOn.gone()
            binding.imageViewPushOff.visible()
        }
        binding.llLogout.setSafeOnClickListener {
            LogoutDialogFragment.showDialog(supportFragmentManager, {
                //Logout
                showProgress()
                homeViewModel.performLogout()
            }, {
                //cancel
            })
        }
        binding.llAboutUs.setSafeOnClickListener {
            startActivityCustom(
                IntentHelper.getCMSScreenIntent(
                    this,
                    getString(
                        R.string.label_about_empire,
                        getString(R.string.app_name)
                    )
                )
            )
        }
        binding.llPrivacyPolicy.setSafeOnClickListener {
            startActivityCustom(
                IntentHelper.getCMSScreenIntent(
                    this,
                    getString(R.string.label_privacy_policy)
                )
            )
        }
        binding.llTermsCondition.setSafeOnClickListener {
            startActivityCustom(
                IntentHelper.getCMSScreenIntent(
                    this,
                    getString(R.string.label_setting_terms_condition)
                )
            )
        }
        binding.imageViewPushOff.setOnClickListener {
            binding.imageViewPushOn.visible()
            binding.imageViewPushOff.gone()
            if (PrefKeys.getUserCommon()?.pushNotificationStatus == AppConstant.No) {
                homeViewModel.getPushNotification(AppConstant.Yes)
            }
        }
        binding.imageViewPushOn.setOnClickListener {
            binding.imageViewPushOff.visible()
            binding.imageViewPushOn.gone()
            if (PrefKeys.getUserCommon()?.pushNotificationStatus == AppConstant.Yes) {
                homeViewModel.getPushNotification(AppConstant.No)
            }
        }
        binding.llCustomerSupport.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getContactUsScreenIntent(this))
        }
        binding.llFaq.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getFaqScreenIntent(this))
        }
    }

    private fun attachObserver() {
        homeViewModel.pushNotificatioObserver.observe(this, Observer {
            it?.apply {
                var user = PrefKeys.getUserCommon()
                if (PrefKeys.getUserCommon()?.pushNotificationStatus == AppConstant.Yes) {
                    user?.pushNotificationStatus = AppConstant.No
                } else {
                    user?.pushNotificationStatus = AppConstant.Yes
                }
                PrefKeys.setUser(user!!)

                if (binding.imageViewPushOn.isVisible)
                    toastSuccess("Push Notification turned on Successfully")
                else
                    toastSuccess("Push Notification turned off Successfully")
            }
        })

        homeViewModel.logoutRSLiveData.observe(this, Observer {
            it.apply {
                if (this.status == 1) {
                    hideProgress()
                    Prefs.putString(PrefKeys.AuthKey, "")
//                    PrefKeys.setUser(User())
                    startActivityCustom(IntentHelper.getLoginScreenIntent(this@SettingActivity))
                } else {
                    toastError(this.message!!)
                }
            }
        })
    }
}

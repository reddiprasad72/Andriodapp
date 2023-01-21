package com.bluboy.android.presentation.profile

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.bluboy.android.R
import com.bluboy.android.data.models.Coupon
import com.bluboy.android.databinding.ActivityKycBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel


class KycActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private lateinit var binding: ActivityKycBinding
    private var openFromProfile = false
    private var REQUEST_KYC = 100

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKycBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        openFromProfile = intent?.getBooleanExtra(AppConstant.FROM_PROFILE, false)!!
        init()

    }

    fun setToolBar() {
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.txtHeader.text = "KYC Verification"
    }

    private fun init() {

        binding.constraintPAN.setOnClickListener {
            startActivityCustom(
                IntentHelper.getKYCPANIntent(
                    context = this,
                    openFromProfile = openFromProfile
                ), REQUEST_KYC
            )
        }

        binding.constraintAadhaar.setOnClickListener {
            startActivityCustom(
                IntentHelper.getKYCAadhaarIntent(
                    context = this,
                    openFromProfile = openFromProfile
                ), REQUEST_KYC
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_KYC) {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }
}

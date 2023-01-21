package com.bluboy.android.presentation.profile

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityKycDetailsBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel

class KycDetailActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private lateinit var binding: ActivityKycDetailsBinding
    private var openFromProfile = false
    private var REQUEST_KYC = 100

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKycDetailsBinding.inflate(layoutInflater)
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
        binding.toolbar.txtHeader.text = "KYC Details"
    }

    private fun init() {
        attachObserver()
        showProgress()
        homeViewModel.getSubmittedKYC()
    }

    private fun attachObserver() {
        homeViewModel.detailsKYCObserver.observe(this) {
            hideProgress()
            it?.apply {
                if (this.status == 1) {

                    it.data?.kycDetails?.let { it1 ->
                        binding.docStatus.text = "KYC Status : ${it1.status}"
                        binding.docNumber.text = "Document Number : ${it1.documentNumber}"
                        binding.docType.text = "Document Type : ${it1.documentType}"

                        if (binding.docType.text.contains("Aadhar")) {
                            binding.imageViewFrontSide.visible()
                            binding.imageViewBackSide.visible()
                            binding.docLabel.text = "Document Images:"
                            loadImage(binding.imageViewBackSide, it1.backPic)
                            loadImage(binding.imageViewFrontSide, it1.frontPic)
                        } else {
                            binding.imageViewFrontSide.visible()
                            binding.imageViewBackSide.gone()
                            binding.docLabel.text = "Document Image:"
                            loadImage(binding.imageViewFrontSide, it1.frontPic)
                        }
                    }
                }
            }
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

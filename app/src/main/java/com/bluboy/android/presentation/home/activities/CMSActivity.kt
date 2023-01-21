package com.bluboy.android.presentation.home.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.text.parseAsHtml
import androidx.lifecycle.Observer
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityCmsBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.utility.AppConstant.Companion.ABOUT_US
import com.bluboy.android.presentation.utility.AppConstant.Companion.PRIVACY_POLICY
import com.bluboy.android.presentation.utility.AppConstant.Companion.TERMS_AND_CONDITION
import org.koin.android.viewmodel.ext.android.viewModel

class CMSActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel

    private lateinit var binding: ActivityCmsBinding

    var cmsType = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCmsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()

        init()
    }

    private fun init() {
        attachObserver()
        cmsType = intent.getStringExtra(AppConstant.cmsType).toString()
        binding.tool.txtHeader.text = cmsType
        getGeneralCms()

        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getGeneralCms() {
        var code = ""
        if (StringUtils.equals(
                cmsType, getString(
                    R.string.label_about_empire,
                    getString(R.string.app_name)
                )
            )
        ) {
            code = ABOUT_US
        }
        if (StringUtils.equals(cmsType, getString(R.string.label_privacy_policy))) {
            code = PRIVACY_POLICY
        }
        if (StringUtils.equals(cmsType, getString(R.string.label_terms_condition)) ||
            StringUtils.equals(cmsType, getString(R.string.label_setting_terms_condition))
        ) {
            code = TERMS_AND_CONDITION
        }
        homeViewModel.getCMSPageData(code)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun attachObserver() {
        homeViewModel.cmsPageRSLiveData.observe(this, Observer {
            it?.apply {
                hideProgress()
                if (it.status == 1) {
                    binding.longText.text = cmsData?.pageContent!!.parseAsHtml()
                    binding.longText.movementMethod = ScrollingMovementMethod()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}



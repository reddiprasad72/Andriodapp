package com.bluboy.android.presentation.wallet

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityAddBeneficiaryBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class BeneficiaryAddActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()

    private val leaderboardDaily1 = BeneficiaryBankFragment()
    private val leaderboardDaily2 = BeneficiaryUpiFragment()
//    private val leaderboardDaily3 = BeneficiaryPaytmFragment()

    override fun getBaseViewModel() = homeViewModel
    var beneficiaryType = ""
    private var leaderType = "daily"
    var transferMode = "banktransfer"

    private lateinit var binding: ActivityAddBeneficiaryBinding

    var selectedTab by Delegates.observable(-1) { property, oldValue, newValue ->
        if (oldValue != newValue) {
            defaultTabInit(newValue)
            binding.viewPager.setCurrentItem(newValue, true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBeneficiaryBinding.inflate(layoutInflater)
//        setAdjustPan()
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()

        transferMode = intent.getStringExtra(AppConstant.TRANSFER_MODE).toString()

        when (transferMode) {
            "banktransfer" -> {
                selectedTab = 0
            }
            "upi" -> {
                selectedTab = 1
            }
            /*"paytm" -> {
                selectedTab = 2
            }*/
        }

        setupViewPager()

        binding.tab1Inactive.setSafeOnClickListener {
            //Bank
            selectedTab = 0
        }

        binding.tab2Inactive.setSafeOnClickListener {
            //UPI
            selectedTab = 1
        }

        /*binding.tab3Inactive.setSafeOnClickListener {
            //Paytm
            selectedTab = 2
        }*/

        beneficiaryType = intent.getStringExtra(AppConstant.BENEFICIARY_TYPE)!!
        if (beneficiaryType == AppConstant.BENEFICIARY_EDIT) {
            binding.toolbar.txtHeader.text = getString(R.string.beneficiary_edit)
        } else {
            binding.toolbar.txtHeader.text = getString(R.string.beneficiary_add)
        }
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
        attachObserver()
    }

    private fun setupViewPager() {
        val pageAdapter = PageAdapter(supportFragmentManager)
        pageAdapter.add(leaderboardDaily1, getString(R.string.label_bank_account))
        pageAdapter.add(leaderboardDaily2, getString(R.string.label_upi_id))
//        pageAdapter.add(leaderboardDaily3, getString(R.string.label_paytm))
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
                when (position) {
                    0 -> {
                        leaderType = "Bank Account"
                    }
                    1 -> {
                        leaderType = "UPI ID"
                    }
                    /*2 -> {
                        leaderType = "Paytm"
                    }*/
                }
            }
        })
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
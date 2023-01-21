package com.bluboy.android.presentation.transaction

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityTransactionBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class TransactionActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel

    private lateinit var binding: ActivityTransactionBinding

    private val transactionPage1 = TransactionAllFragment()
    private val transactionPage2 = TransactionWinningFragment()
    private val transactionPage3 = TransactionCashFragment()
    private val transactionPage4 = TransactionWithdrawFragment()
    private var walletType = "A"

    var selectedTab by Delegates.observable(-1) { property, oldValue, newValue ->
        if (oldValue != newValue) {
            defaultTabInit(newValue)
            binding.viewPager.setCurrentItem(newValue, true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    private fun defaultTabInit(pos: Int) {
        binding.apply {
            tab1Active.invisible()
            tab2Active.invisible()
            tab3Active.invisible()
            tab4Active.invisible()


        }
        when (pos) {
            0 -> {
                binding.tab1Active.visible()
                binding.tab1Inactive.invisible()

                binding.tab2Inactive.visible()
                binding.tab3Inactive.visible()
                binding.tab4Inactive.visible()
            }
            1 -> {
                binding.tab2Active.visible()
                binding.tab2Inactive.invisible()

                binding.tab1Inactive.visible()
                binding.tab3Inactive.visible()
                binding.tab4Inactive.visible()
            }
            2 -> {
                binding.tab3Active.visible()
                binding.tab3Inactive.invisible()

                binding.tab1Inactive.visible()
                binding.tab2Inactive.visible()
                binding.tab4Inactive.visible()
            }
            3 -> {
                binding.tab4Active.visible()
                binding.tab4Inactive.invisible()

                binding.tab1Inactive.visible()
                binding.tab2Inactive.visible()
                binding.tab3Inactive.visible()
            }
        }
    }


    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_transaction)
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        attachObserver()
        setupViewPager()

        binding.tab1Inactive.setSafeOnClickListener {
            //All
            selectedTab = 0
        }

        binding.tab2Inactive.setSafeOnClickListener {
            //Wining
            selectedTab = 1
        }

        binding.tab3Inactive.setSafeOnClickListener {
            //Cash
            selectedTab = 2
        }


        binding.tab4Inactive.setSafeOnClickListener {
            //Cash
            selectedTab = 3
        }

        selectedTab = 0
    }

    private fun setupViewPager() {
        val pageAdapter =
            PageAdapter(
                supportFragmentManager
            )
        pageAdapter.add(transactionPage1, getString(R.string.label_All))
        pageAdapter.add(transactionPage2, getString(R.string.label_winning))
        pageAdapter.add(transactionPage3, getString(R.string.label_cash))
        pageAdapter.add(transactionPage4, getString(R.string.label_withdraw))

        binding.viewPager.offscreenPageLimit = 4
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

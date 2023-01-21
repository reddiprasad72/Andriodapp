package com.bluboy.android.presentation.wallet

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.Coupon
import com.bluboy.android.databinding.ActivityPromocodeBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.wallet.adapters.PromocodeAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class PromocodeActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel
    private lateinit var promoAdapter: PromocodeAdapter
    private var arrayListCoupon = ArrayList<Coupon>()

    private lateinit var binding: ActivityPromocodeBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromocodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_promocode_s)
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        attachObserver()
//        showProgress()
        binding.mShimmerPromocode.visible()
        binding.llEmpty.gone()

        homeViewModel.getPromocodeList()

        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPromocode.layoutManager = linearLayoutManager
        promoAdapter = PromocodeAdapter(arrayListCoupon) {
            val resultIntent = Intent()
            resultIntent.putExtra("result", it)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        binding.recyclerViewPromocode.adapter = promoAdapter
    }

    private fun attachObserver() {
        homeViewModel.promocodeResponse.observe(this, Observer {
//            hideProgress()
            binding.mShimmerPromocode.gone()
            if (it.status == 1) {
                binding.recyclerViewPromocode.visible()
                binding.llEmpty.gone()
                it.data?.coupons?.let { it1 -> arrayListCoupon.addAll(it1) }
                promoAdapter.notifyDataSetChanged()
            } else {
                binding.recyclerViewPromocode.gone()
                binding.llEmpty.visible()
            }
        })
    }
}

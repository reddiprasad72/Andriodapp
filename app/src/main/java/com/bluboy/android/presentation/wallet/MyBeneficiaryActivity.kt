package com.bluboy.android.presentation.wallet

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.BeneficiaryData
import com.bluboy.android.databinding.ActivityMyBeneficiaryBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.AccountDetailDialogFragment
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.wallet.adapters.MyBeneficiaryAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class MyBeneficiaryActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private var myBeneficiaryAdapter: MyBeneficiaryAdapter? = null
    private var arrayListBeneficiary = ArrayList<BeneficiaryData>()
    private lateinit var binding: ActivityMyBeneficiaryBinding
    private var globalPosition = -1
    var userDepositBalance = "0"
    var userWinningBalance = "0"
    var userTotalBalance = "0"
    var userCashBonus = "0"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBeneficiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_my_beneficiary)
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        attachObserver()
        binding.mShimmerBeneficiary.visible()
        binding.llEmpty.root.gone()
        binding.btnAddBeneficiary.setOnClickListener {
            startActivityCustom(
                IntentHelper.getAddBeneficiaryScreenIntent(
                    this,
                    AppConstant.BENEFICIARY_ADD,
                    "banktransfer"
                )
            )
        }

        myBeneficiaryAdapter = MyBeneficiaryAdapter(arrayListBeneficiary)
        { position, view, beneficiaryData ->
            if (view.id == R.id.imageViewDelete) {

                CommonAppDialogFragment.showDialog(
                    supportFragmentManager,
                    getString(R.string.app_name),
                    "Are you sure you want to delete this beneficiary?",
                    "Yes",
                    "No",
                    {
                        //delete
                        globalPosition = position
                        showProgress()
                        homeViewModel.getBeneficiaryDelete(beneficiaryData.beneficiaryId)
                    }, {}, {})

            } else {
                AccountDetailDialogFragment.showDialog(supportFragmentManager, beneficiaryData)
            }
        }
        var mLayoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewBeneficiary.layoutManager = mLayoutManager
        binding.recyclerViewBeneficiary.adapter = myBeneficiaryAdapter
    }

    override fun onResume() {
        super.onResume()
//        showProgress()
        binding.mShimmerBeneficiary.visible()
        binding.llEmpty.root.gone()
        binding.recyclerViewBeneficiary.gone()
        binding.btnAddBeneficiary.gone()
        arrayListBeneficiary.clear()
        homeViewModel.getBeneficiaryList("all")
    }

    private fun attachObserver() {

        homeViewModel.beneficiaryListObserver.observe(this) {
//            hideProgress()
            binding.mShimmerBeneficiary.gone()
            if (it.status == 1) {
                binding.btnAddBeneficiary.visible()
                binding.recyclerViewBeneficiary.visible()
                binding.llEmpty.root.gone()
                arrayListBeneficiary.clear()

                it.data?.beneficiaryData?.let { it1 ->
                    arrayListBeneficiary.addAll(it1)
                }

                myBeneficiaryAdapter?.notifyDataSetChanged()
            } else {
                binding.recyclerViewBeneficiary.gone()
                binding.btnAddBeneficiary.visible()
                binding.llEmpty.root.visible()
            }
        }

        homeViewModel.beneficiaryDeleteObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                homeViewModel.getBeneficiaryList("all")
               /* arrayListBeneficiary.removeAt(globalPosition)
                myBeneficiaryAdapter?.notifyItemRemoved(globalPosition)
                myBeneficiaryAdapter?.notifyItemRangeChanged(
                    globalPosition,
                    arrayListBeneficiary.size
                )*/
                toastSuccess(it?.message.toString())
            } else {
                toastError(it?.message.toString())
            }
        })
    }
}

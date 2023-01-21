package com.bluboy.android.presentation.wallet

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.BeneficiaryData
import com.bluboy.android.data.models.CheckWithdrawalTransactionData
import com.bluboy.android.databinding.ActivityWithdrawDetailBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.dialog.WithdrawDialogFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.wallet.adapters.WithdrawDetailAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class WithdrawDetailActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private var walletDetailAdapter: WithdrawDetailAdapter? = null
    private var arrayListBeneficiary = ArrayList<BeneficiaryData>()
    private lateinit var binding: ActivityWithdrawDetailBinding
    var transferMode = "banktransfer"
    var amount = ""
    private var checkWithdrawTransaction: CheckWithdrawalTransactionData? = null
    private var chargePercentage = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_withdraw_cash)
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        attachObserver()
        transferMode = intent.getStringExtra(AppConstant.TRANSFER_MODE).toString()
        amount = intent.getStringExtra(AppConstant.TRANSFER_AMOUNT).toString()
        checkWithdrawTransaction = intent.getParcelableExtra(AppConstant.checkWithdrawTransaction)
        chargePercentage = intent.getStringExtra(AppConstant.CHARGE).toString()
        binding.textViewAmount.text = "₹" + amount

        if (transferMode == "banktransfer") {
            loadImage(binding.imageViewBank, R.drawable.bank_icon)
            binding.textViewTransfer.text = "Bank Transfer"
            binding.textViewSelectUpi.text = "Select Bank"
        } else if (transferMode == "upi") {
            loadImage(binding.imageViewBank, R.drawable.bheem_upi)
            binding.textViewTransfer.text = "UPI Transfer"
            binding.textViewSelectUpi.text = "Select UPI"
        } else {
            loadImage(binding.imageViewBank, R.drawable.paytm)
            binding.textViewTransfer.text = "Paytm Transfer"
            binding.textViewSelectUpi.text = "Select Paytm"
        }

        /* // Withdraw Limit
         binding.textViewAmountLimit.text =
             "₹" + checkWithdrawTransaction?.allowedTotalWithdrawAmounts
         binding.textViewTransactionLimit.text =
             checkWithdrawTransaction?.allowedTotalWithdrawTransactions
         binding.textViewadminCharge.text = chargePercentage + " %"*/


        binding.btnAddBeneficiary.setSafeOnClickListener {
            startActivityCustom(
                IntentHelper.getAddBeneficiaryScreenIntent(
                    this,
                    AppConstant.BENEFICIARY_ADD,
                    transferMode
                )
            )
        }
        walletDetailAdapter = WithdrawDetailAdapter(arrayListBeneficiary) { _, _, beneficiaryData ->
            WithdrawDialogFragment.showDialog(supportFragmentManager, amount, beneficiaryData) {
                showProgress()
                homeViewModel.withdraw(
                    amount,
                    beneficiaryData.beneficiaryId
                )
            }
        }
        var mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewBeneficiaryWithdraw.layoutManager = mLayoutManager
        binding.recyclerViewBeneficiaryWithdraw.adapter = walletDetailAdapter
    }

    override fun onResume() {
        super.onResume()
        showProgress()
        arrayListBeneficiary.clear()
        homeViewModel.getBeneficiaryList(transferMode)
    }

    private fun attachObserver() {
        homeViewModel.beneficiaryListObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                binding.btnAddBeneficiary.visible()
                binding.recyclerViewBeneficiaryWithdraw.visible()
                binding.llEmpty.root.gone()
                binding.groupConstraintList.visible()
                arrayListBeneficiary.clear()
                it.data?.beneficiaryData?.let { it1 -> arrayListBeneficiary.addAll(it1) }
                walletDetailAdapter?.notifyDataSetChanged()
            } else {
                binding.recyclerViewBeneficiaryWithdraw.gone()
                binding.btnAddBeneficiary.visible()
                binding.llEmpty.root.visible()
                binding.groupConstraintList.gone()
            }
        })

        homeViewModel.withDrawObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), it?.message.toString(),
                    getString(R.string.ok), "",
                    {
                        startActivityCustom(
                            IntentHelper.getHomeScreenIntent(
                                context = this@WithdrawDetailActivity,
                                isClearFlag = true
                            )
                        )
                        finishAffinity()
                    },
                    {},
                    {})
            } else {
                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), it?.message.toString(),
                    getString(R.string.ok), "",
                    {
                        startActivityCustom(
                            IntentHelper.getHomeScreenIntent(
                                context = this@WithdrawDetailActivity,
                                isClearFlag = true
                            )
                        )
                        finishAffinity()
                    },
                    {},
                    {})
            }
        })

    }
}

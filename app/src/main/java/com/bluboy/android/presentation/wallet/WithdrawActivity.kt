package com.bluboy.android.presentation.wallet

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bluboy.android.R
import com.bluboy.android.data.models.CheckWithdrawalTransactionData
import com.bluboy.android.databinding.ActivityWithdrawBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel

class WithdrawActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private var transferMode = ""
    private lateinit var binding: ActivityWithdrawBinding
    private var checkWithdrawTransaction: CheckWithdrawalTransactionData? = null
    private var chargePercentage = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        attachObserver()

        init()
    }

    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_withdraw_cash)
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getSettingVar()
    }

    private fun init() {
        homeViewModel.getPaymentGatewayIncidents()

        checkWithdrawTransaction = intent.getParcelableExtra(AppConstant.checkWithdrawTransaction)


        binding.checkboxBank.isChecked = true
        transferMode = "banktransfer"
        chargePercentage = if (PrefKeys.getVariableData()?.bankUpiType == "HYPTO") {
            PrefKeys.getVariableData()?.payoutHyptoBankCharge.toString()
        } else {
            PrefKeys.getVariableData()?.bankCharge.toString()
        }


        // Withdraw Limit
        /* binding.textViewAmountLimit.text =
             "₹" + checkWithdrawTransaction?.allowedTotalWithdrawAmounts
         binding.textViewTransactionLimit.text =
             checkWithdrawTransaction?.allowedTotalWithdrawTransactions
         binding.textViewadminCharge.text =
             if (chargePercentage.contains("%")) chargePercentage else chargePercentage + "%"*/


//        textViewWithdrawalLimit.text = "* Daily withdrawal limit ₹${checkWithdrawTransaction?.allowedTotalWithdrawAmounts} \n* Daily transaction limit ${checkWithdrawTransaction?.allowedTotalWithdrawTransactions}"
        EditTextSearchCommon(binding.editTextAmount).watch {
            chargeCount(it)
        }
        binding.textViewAmount.text = "₹ " + PrefKeys.getUserCommon()?.userWinningAmountBalance

        if (PrefKeys.getVariableData()?.bankUpiPayout == "1") {
            binding.groupBank.visible()
        } else {
            binding.groupBank.gone()
        }

        if (PrefKeys.getVariableData()?.paytmPayout == "1") {
            binding.groupPaytm.visible()
        } else {
            binding.groupPaytm.gone()
        }

        if (PrefKeys.getVariableData()?.bankUpiPayout == "1" ||
            PrefKeys.getVariableData()?.paytmPayout == "1"
        ) {
            binding.buttonWithDrawMoney.visible()
        } else {
            binding.buttonWithDrawMoney.gone()
        }

        binding.buttonWithDrawMoney.setSafeOnClickListener {
            if (!binding.editTextAmount.text?.isEmpty()!!) {
                if (checkWithdrawTransaction != null
                    && checkWithdrawTransaction?.remainTotalWithdrawAmount != null
                    && checkWithdrawTransaction?.remainTotalWithdrawAmount!! >= binding.editTextAmount.text.toString()
                        .toInt()
                ) {
                    if (checkWithdrawTransaction != null
                        && checkWithdrawTransaction?.remainTotalWithdrawTransactions != null
                        && checkWithdrawTransaction?.remainTotalWithdrawTransactions!! > 0
                    ) {
                        if (PrefKeys.getVariableData()?.bankUpiPayout == "1" || PrefKeys.getVariableData()?.paytmPayout == "1") {
                            if (transferMode != "") {
                                when {
                                    binding.editTextAmount.text?.isEmpty()!! -> {
                                        toastError("Please enter amount")
                                    }
                                    binding.editTextAmount.text.toString()
                                        .toInt() < PrefKeys.getVariableData()?.mINWITHDRAWAMOUNT?.toInt()!! -> {
                                        toastError("Minimum Withdraw Amount should be greater than or equal to ${PrefKeys.getVariableData()?.mINWITHDRAWAMOUNT}")
                                    }
                                    binding.editTextAmount.text.toString()
                                        .toInt() > PrefKeys.getUserCommon()?.userWinningAmountBalance?.toFloat()!! -> {
                                        toastError("Not withdraw amount more than ${PrefKeys.getUserCommon()?.userWinningAmountBalance}")
                                    }
                                    else -> {
                                        startActivityCustom(
                                            IntentHelper.getWithdrawDetailScreenIntent(
                                                this,
                                                transferMode,
                                                binding.editTextAmount.text.toString(),
                                                checkWithdrawTransaction!!, chargePercentage
                                            )
                                        )
                                    }
                                }
                            } else {
                                toastError("Please select Payment mode")
                            }
                        } else {
                            toastError("Sorry, Now you can't withdraw money.Please wait some time")
                        }
                    } else {
                        toastError("Your daily transaction limit is ${checkWithdrawTransaction?.allowedTotalWithdrawTransactions}")
                    }
                } else {
                    toastError("Your daily withdrawal limit is ₹${checkWithdrawTransaction?.allowedTotalWithdrawAmounts}")
                }
            } else {
                toastError("Please enter amount")
            }
        }

        binding.checkboxBank.setOnClickListener {
            deselectAll()
            binding.checkboxBank.isChecked = true
            transferMode = "banktransfer"
            chargeCount(binding.editTextAmount.text.toString())
        }

        binding.checkboxUPI.setOnClickListener {
            deselectAll()
            binding.checkboxUPI.isChecked = true
            transferMode = "upi"
            chargeCount(binding.editTextAmount.text.toString())
        }

        binding.checkboxPaytm.setOnClickListener {
            deselectAll()
            binding.checkboxPaytm.isChecked = true
            transferMode = "paytm"
            chargeCount(binding.editTextAmount.text.toString())
        }
    }

    private fun chargeCount(it: String) {
        if (transferMode != "") {
            when (transferMode) {
                "banktransfer" -> {
                    chargePercentage = if (PrefKeys.getVariableData()?.bankUpiType == "HYPTO") {
                        PrefKeys.getVariableData()?.payoutHyptoBankCharge.toString()
                    } else {
                        PrefKeys.getVariableData()?.bankCharge.toString()
                    }
                    binding.textViewReciveType.text = "to your bank account."
                }

                "upi" -> {
                    chargePercentage = if (PrefKeys.getVariableData()?.bankUpiType == "HYPTO") {
                        PrefKeys.getVariableData()?.payoutHyptoUpiCharge.toString()
                    } else {
                        PrefKeys.getVariableData()?.upiCharge.toString()
                    }
                    binding.textViewReciveType.text = "to your bank account."
                }

                "paytm" -> {
                    chargePercentage = PrefKeys.getVariableData()?.paytmCharge.toString()
                    binding.textViewReciveType.text = "to your paytm wallet."
                }
            }

            /* binding.textViewadminCharge.text =
                 if (chargePercentage.contains("%")) chargePercentage else chargePercentage + "%"*/

            if (it.length != 0) {
                if (it.toFloat() > PrefKeys.getUserCommon()?.userWinningAmountBalance?.toFloat()!!) {
                    toastError("Not withdraw amount more than ${PrefKeys.getUserCommon()?.userWinningAmountBalance}")
                } else {
                    if (it.length != 0) {

                        binding.llAmount.visible()

                        if (chargePercentage.isBlank()
                            || chargePercentage.equals("0")
                            || chargePercentage.equals("0%")
                            || chargePercentage.equals("0 %")
                        ) binding.llAmount.gone()

                        if (chargePercentage.contains("%")) {
                            chargePercentage =
                                chargePercentage.substring(0, chargePercentage.length - 1)
                            var chargeAmount = (chargePercentage.toFloat() * it.toFloat()) / 100
                            binding.editTextAmountCharge.text =
                                String.format("%s %.2f", "₹", (it.toFloat() - chargeAmount))
                        } else {
                            binding.editTextAmountCharge.text = String.format(
                                "%s %.2f",
                                "₹",
                                (it.toFloat() - chargePercentage.toFloat())
                            )
                        }

                    } else {
                        binding.llAmount.gone()
                        binding.editTextAmountCharge.text = "₹ " + "0"
                    }
                }
            } else {
                binding.llAmount.gone()
                binding.editTextAmountCharge.text = "₹ " + "0"
            }
        }
    }

    private fun deselectAll() {
        binding.checkboxBank.isChecked = false
        binding.checkboxPaytm.isChecked = false
        binding.checkboxUPI.isChecked = false
    }

    private fun attachObserver() {
        homeViewModel.varSettingRS.observe(this, Observer {
            if (it.status == 1) {
                PrefKeys.setVariableData(it.data!!)
            }
        })

        homeViewModel.paymentGatewayIncidentsObserver.observe(this) {
            if (it.status == 1) {
                if (it.cASHFREE != null && it.cASHFREE.isNotEmpty()) {
                    for (i in 0 until it.cASHFREE.size) {
                        if (it.cASHFREE[i].instrumentsType == "net_banking") {
                            binding.textViewBankSuccessRate.visible()
                            binding.textViewBankSuccessRate.text = it.cASHFREE[i].incidentMessage
                        }
                        if (it.cASHFREE[i].instrumentsType == "upi") {
                            binding.textViewUpiSuccessRate.visible()
                            binding.textViewUpiSuccessRate.text = it.cASHFREE[i].incidentMessage
                        }
                    }
                }
            }
        }
    }
}

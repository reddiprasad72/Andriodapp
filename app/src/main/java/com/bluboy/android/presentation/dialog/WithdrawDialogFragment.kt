package com.bluboy.android.presentation.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.R
import com.bluboy.android.data.models.BeneficiaryData
import com.bluboy.android.databinding.DialogWithdrawBinding
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder
import com.bluboy.android.presentation.utility.gone
import com.bluboy.android.presentation.utility.visible

class WithdrawDialogFragment : BlurDialogFragment() {
    private lateinit var beneficiaryData: BeneficiaryData
    private var amount = ""

    //Dialog Withdrawal Confirmation

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            amount: String,
            beneficiaryData: BeneficiaryData,
            claimListener: (beneficiaryData: BeneficiaryData) -> Unit
        ) {
            val generalAppDialogFragment = WithdrawDialogFragment().apply {}
            generalAppDialogFragment.claimListener = claimListener
            generalAppDialogFragment.amount = amount
            generalAppDialogFragment.beneficiaryData = beneficiaryData
            generalAppDialogFragment.show(
                fragmentManager,
                WithdrawDialogFragment::class.java.simpleName
            )
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@WithdrawDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var cancelListener: (beneficiaryData: String) -> Unit

    private lateinit var claimListener: (beneficiaryData: BeneficiaryData) -> Unit

    fun newInstance(): WithdrawDialogFragment? {
        val fragment = WithdrawDialogFragment()
        fragment.setStyle(
            DialogFragment.STYLE_NO_TITLE,
            R.style.EtsyBlurDialogTheme
        )
        return fragment
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogSlideAnimation
    }

    override fun blurConfig(): BlurConfig {
        return BlurConfig.Builder()
            .overlayColor(Color.argb(150, 0, 0, 0)) // semi-transparent white color
            .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
            .debug(true)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_withdraw, null, false)
        val binding = DialogWithdrawBinding.bind(view)

        binding.textViewAmount.text = "Amount : ₹ $amount"
        binding.textViewAccountHolderName.text = beneficiaryData.beneficiaryName
        binding.textViewNickName.text = beneficiaryData.beneficiaryNickName
        binding.textViewBankName.text = beneficiaryData.beneficiaryBankName
        binding.textViewAccountNumber.text = beneficiaryData.beneficiaryAccountNumber

        when (beneficiaryData.transferMode) {

            "banktransfer" -> {
                binding.textViewBankName.text = beneficiaryData.beneficiaryBankName
                binding.tvBankNameLabel.text = "Bank Name"
                binding.groupAccountNumber.visible()
            }

            "upi" -> {
                binding.textViewBankName.text = beneficiaryData.beneficiaryVpa
                binding.tvBankNameLabel.text = getString(R.string.label_upi_name)
                binding.groupAccountNumber.gone()
            }

            "paytm" -> {
                binding.textViewBankName.text = beneficiaryData.beneficiaryPhone
                binding.tvBankNameLabel.text = getString(R.string.label_paytm)
                binding.groupAccountNumber.gone()
            }
        }

        /*BounceView.addAnimTo(binding.imageViewClose)
        BounceView.addAnimTo(binding.buttonWithDrawMoney)*/

        binding.imageViewClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.buttonWithDrawMoney.setOnClickListener {
            claimListener.invoke(beneficiaryData)
            dismissAllowingStateLoss()
        }

        return view
    }


}
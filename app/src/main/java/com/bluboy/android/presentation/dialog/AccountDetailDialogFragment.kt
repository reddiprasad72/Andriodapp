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
import com.bluboy.android.databinding.DialogAccountDetailBinding
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder
import com.bluboy.android.presentation.utility.*

class AccountDetailDialogFragment : BlurDialogFragment() {
    private lateinit var beneficiaryData: BeneficiaryData

    //Dialog to see Account Details

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            beneficiaryData: BeneficiaryData,
        ) {
            val generalAppDialogFragment = AccountDetailDialogFragment().apply {}
            generalAppDialogFragment.beneficiaryData = beneficiaryData
            generalAppDialogFragment.show(
                fragmentManager,
                AccountDetailDialogFragment::class.java.simpleName
            )
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@AccountDetailDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var cancelListener: (beneficiaryData: String) -> Unit

    fun newInstance(): AccountDetailDialogFragment? {
        val fragment = AccountDetailDialogFragment()
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
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_account_detail, null, false)
        val binding = DialogAccountDetailBinding.bind(view)

        binding.textViewAccountHolderName.text = beneficiaryData.beneficiaryName
        binding.textViewNickName.text = beneficiaryData.beneficiaryNickName
        binding.textViewAccountNumber.text = beneficiaryData.beneficiaryAccountNumber

        binding.textViewBankName.text = beneficiaryData.beneficiaryBankName

        when (beneficiaryData.transferMode) {

            "banktransfer" -> {
                binding.textViewBankName.text = beneficiaryData.beneficiaryBankName
                binding.tvBankNameLabel.text = getString(R.string.label_bank_name)
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

        /*BounceView.addAnimTo(binding.imageViewClose)*/
        binding.imageViewClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
        return view
    }


}
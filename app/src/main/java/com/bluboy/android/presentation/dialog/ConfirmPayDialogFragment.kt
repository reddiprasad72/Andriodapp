package com.bluboy.android.presentation.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.R
import com.bluboy.android.databinding.DialogConfirmPayBinding
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder

class ConfirmPayDialogFragment : BlurDialogFragment() {
    private var entryFee = ""
    private var deductionFromWalletAmount = ""
    private var deductionFromBonusAmount = ""
    private var totalPayableAmount = ""

    //Dialog appears with confirmation for entering into Paid Games

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            entryFee: String,
            deductionFromWalletAmount: String,
            deductionFromBonusAmount: String,
            totalPayableAmount: String,
            claimListener: (beneficiaryData: String) -> Unit
        ) {
            val generalAppDialogFragment = ConfirmPayDialogFragment().apply {}
            generalAppDialogFragment.claimListener = claimListener
            generalAppDialogFragment.entryFee = entryFee
            generalAppDialogFragment.deductionFromWalletAmount = deductionFromWalletAmount
            generalAppDialogFragment.deductionFromBonusAmount = deductionFromBonusAmount
            generalAppDialogFragment.totalPayableAmount = totalPayableAmount
            generalAppDialogFragment.show(
                fragmentManager,
                ConfirmPayDialogFragment::class.java.simpleName
            )
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@ConfirmPayDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var cancelListener: (beneficiaryData: String) -> Unit

    private lateinit var claimListener: (beneficiaryData: String) -> Unit

    fun newInstance(): ConfirmPayDialogFragment? {
        val fragment = ConfirmPayDialogFragment()
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

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_pay, null, false)
        val binding = DialogConfirmPayBinding.bind(view)

        binding.textViewAccountHolderName.text = getString(R.string.x_price_rupees, entryFee)
        binding.textViewNickName.text =
            "- ${getString(R.string.x_price_rupees, deductionFromWalletAmount)}"
        binding.textViewBankName.text =
            "- ${getString(R.string.x_price_rupees, deductionFromBonusAmount)}"
        binding.textViewAccountNumber.text = getString(R.string.x_price_rupees, totalPayableAmount)

        /*BounceView.addAnimTo(binding.imageViewClose)
        BounceView.addAnimTo(binding.buttonWithDrawMoney)*/

        binding.imageViewClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.buttonWithDrawMoney.setOnClickListener {
            claimListener.invoke("")
            dismissAllowingStateLoss()
        }

        return view
    }


}
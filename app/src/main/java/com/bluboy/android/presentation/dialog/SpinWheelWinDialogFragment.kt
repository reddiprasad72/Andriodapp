package com.bluboy.android.presentation.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.R
import com.bluboy.android.databinding.DialogSpinWheelBinding
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder

class SpinWheelWinDialogFragment : BlurDialogFragment() {
    private var message = ""

    //Dialog with Spin Win non Zero Amount

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            message: String,
            claimListener: (beneficiaryData: String) -> Unit
        ) {
            val generalAppDialogFragment = SpinWheelWinDialogFragment().apply {}
            generalAppDialogFragment.claimListener = claimListener
            generalAppDialogFragment.message = message
            generalAppDialogFragment.show(
                fragmentManager,
                SpinWheelWinDialogFragment::class.java.simpleName
            )
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setWindowAnimations(R.style.dialog_animation_fade)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@SpinWheelWinDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var claimListener: (beneficiaryData: String) -> Unit

    fun newInstance(): SpinWheelWinDialogFragment? {
        val fragment = SpinWheelWinDialogFragment()
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
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_spin_wheel, null, false)
        val binding = DialogSpinWheelBinding.bind(view)

        binding.textViewAmount.text = "₹ $message"
        binding.textViewDescription.text = getString(R.string.message_spin_wheel_win, "₹" + message)

        /*BounceView.addAnimTo(binding.imageViewClose)
        BounceView.addAnimTo(binding.buttonCollectReward)*/

        binding.imageViewClose.setOnClickListener {
            claimListener.invoke("")
            dismissAllowingStateLoss()
        }
        binding.buttonCollectReward.setOnClickListener {
            claimListener.invoke("")
            dismissAllowingStateLoss()
        }
        return view
    }
}
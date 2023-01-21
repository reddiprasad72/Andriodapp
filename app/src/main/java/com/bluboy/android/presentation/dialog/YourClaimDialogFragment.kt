package com.bluboy.android.presentation.dialog

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.R
import com.bluboy.android.databinding.DialogYourClaimBinding
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder

class YourClaimDialogFragment : BlurDialogFragment() {
    private var message = ""

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            message: String,
            claimListener: (beneficiaryData: String) -> Unit
        ) {
            val generalAppDialogFragment = YourClaimDialogFragment().apply {}
            generalAppDialogFragment.claimListener = claimListener
            generalAppDialogFragment.message = message
            generalAppDialogFragment.show(
                fragmentManager,
                YourClaimDialogFragment::class.java.simpleName
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
                add(this@YourClaimDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var claimListener: (beneficiaryData: String) -> Unit

    fun newInstance(): YourClaimDialogFragment? {
        val fragment = YourClaimDialogFragment()
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

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_your_claim, null, false)
        val binding = DialogYourClaimBinding.bind(view)

        Handler(Looper.getMainLooper()).postDelayed({
            claimListener.invoke("")
            dismissAllowingStateLoss()
        }, 9000)

        /*BounceView.addAnimTo(binding.imageViewClose)
        BounceView.addAnimTo(binding.buttonCollectReward)*/

        binding.textViewDescription.text = getString(
            R.string.message_success_registered,
            getString(R.string.app_name)
        )

        val span = SpannableStringBuilder(binding.textViewDescription.text)
        span.setSpan(
            StyleSpan(Typeface.BOLD),
            (binding.textViewDescription.text.length) - (getString(R.string.app_name).length) - 1,
            binding.textViewDescription.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewDescription.text = span

        binding.imageViewClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        binding.textViewAmount.text = "₹" + message
        binding.buttonCollectReward.setOnClickListener {
            claimListener.invoke("")
            dismissAllowingStateLoss()
        }
        return view
    }
}
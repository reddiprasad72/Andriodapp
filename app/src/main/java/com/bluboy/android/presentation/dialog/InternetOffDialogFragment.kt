package com.bluboy.android.presentation.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.R
import com.bluboy.android.databinding.DialogInternetBinding
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogNotCloseFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder

class InternetOffDialogFragment : BlurDialogNotCloseFragment() {

    //Dialog appears when internet connection is lost in between

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            logoutListener: () -> Unit,
            cancelListener: (beneficiaryData: String) -> Unit
        ) {
            val generalAppDialogFragment = InternetOffDialogFragment().apply {}
            generalAppDialogFragment.logoutListener = logoutListener
            generalAppDialogFragment.cancelListener = cancelListener
            generalAppDialogFragment.show(
                fragmentManager,
                InternetOffDialogFragment::class.java.simpleName
            )
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@InternetOffDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var logoutListener: () -> Unit
    private lateinit var cancelListener: (beneficiaryData: String) -> Unit

    fun newInstance(): InternetOffDialogFragment? {
        val fragment = InternetOffDialogFragment()
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
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_internet, null, false)
        val binding = DialogInternetBinding.bind(view)

        /*BounceView.addAnimTo(binding.buttonLogout)*/

        binding.textViewMessage.text = getString(
            R.string.message_no_internet_connection,
            getString(R.string.app_name)
        )

        binding.imageViewClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        binding.buttonLogout.setOnClickListener {
            logoutListener.invoke()
            dismissAllowingStateLoss()
        }
        return view
    }
}
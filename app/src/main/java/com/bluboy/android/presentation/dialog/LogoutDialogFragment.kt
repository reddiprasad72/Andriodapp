package com.bluboy.android.presentation.dialog

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.R
import com.bluboy.android.databinding.DialogLogoutBinding
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder

class LogoutDialogFragment : BlurDialogFragment() {

    //Logout Dialog

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            logoutListener: () -> Unit,
            cancelListener: (beneficiaryData: String) -> Unit
        ) {
            val generalAppDialogFragment = LogoutDialogFragment().apply {}
            generalAppDialogFragment.logoutListener = logoutListener
            generalAppDialogFragment.cancelListener = cancelListener
            generalAppDialogFragment.show(
                fragmentManager,
                LogoutDialogFragment::class.java.simpleName
            )
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@LogoutDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var logoutListener: () -> Unit
    private lateinit var cancelListener: (beneficiaryData: String) -> Unit


    fun newInstance(): LogoutDialogFragment? {
        val fragment = LogoutDialogFragment()
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
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_logout, null, false)
        val binding = DialogLogoutBinding.bind(view)

        /*BounceView.addAnimTo(binding.imageViewClose)
        BounceView.addAnimTo(binding.buttonLogout)*/

        var span = SpannableStringBuilder()
            .append(getString(R.string.message_logout))
            .bold {
                append(
                    getString(R.string.app_name)
                )
            }
            .append(" App?")

        binding.textViewMessage.text = span

        binding.imageViewClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        binding.buttonLogout.setOnClickListener {
            logoutListener.invoke()
            dismissAllowingStateLoss()
        }

        binding.buttonCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        return view
    }
}
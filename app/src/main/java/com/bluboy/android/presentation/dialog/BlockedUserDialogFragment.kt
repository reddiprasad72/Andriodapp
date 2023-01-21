package com.bluboy.android.presentation.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.R
import com.bluboy.android.databinding.DialogBlockedUserBinding
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder
import com.bluboy.android.presentation.utility.lazyFast

class BlockedUserDialogFragment : BlurDialogFragment() {

    //Dialog appears when blocked user access the application

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_MESSAGE = "message"
        private const val KEY_POSITIVE = "positive"
        private const val KEY_NEGATIVE = "negative"

        fun showDialog(
            fragmentManager: FragmentManager,
            title: String?, message: String,
            positive: String, negative: String,
            positiveListener: () -> Unit = {},
            negativeListener: () -> Unit = {},
            closeListener: () -> Unit = {}
        ) {
            val generalAppDialogFragment = BlockedUserDialogFragment().apply {
                arguments = bundleOf(
                    KEY_TITLE to title,
                    KEY_MESSAGE to message,
                    KEY_POSITIVE to positive,
                    KEY_NEGATIVE to negative
                )
            }

            generalAppDialogFragment.positiveListener = positiveListener
            generalAppDialogFragment.negativeListener = negativeListener
            generalAppDialogFragment.closeListener = closeListener
            generalAppDialogFragment.show(
                fragmentManager,
                BlockedUserDialogFragment::class.java.simpleName
            )
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@BlockedUserDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var positiveListener: () -> Unit
    private lateinit var negativeListener: () -> Unit
    private lateinit var closeListener: () -> Unit

    private val title: String by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getString(KEY_TITLE, "")
    }

    private val message by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getString(KEY_MESSAGE, "")
    }

    private val positive by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getString(KEY_POSITIVE, "")
    }

    private val negative by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getString(KEY_NEGATIVE, "")
    }


    fun newInstance(): BlockedUserDialogFragment? {
        val fragment = BlockedUserDialogFragment()
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

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_blocked_user, null, false)
        val binding = DialogBlockedUserBinding.bind(view)

        binding.textViewTitle.text = title
        binding.textViewMessage.text = message
        binding.btnNo.text = negative
        /*BounceView.addAnimTo(binding.btnNo)*/

        binding.btnNo.setOnClickListener {
            negativeListener.invoke()
            dismissAllowingStateLoss()
        }

        binding.imageViewClose.setOnClickListener {
            closeListener.invoke()
            dismissAllowingStateLoss()
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
package com.bluboy.android.presentation.dialog

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.R
import com.bluboy.android.data.models.VariableData
import com.bluboy.android.databinding.DialogPopupRedirectionHomeBinding
import com.bluboy.android.presentation.home.HomeActivity
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder

class PopUpRedirectionHomeDialogFragment : BlurDialogFragment() {
    private var variableData: VariableData? = null
    private var shareText: String = ""
    private lateinit var binding: DialogPopupRedirectionHomeBinding

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            variableData: VariableData,
            claimListener: (beneficiaryData: String) -> Unit
        ) {
            val generalAppDialogFragment = PopUpRedirectionHomeDialogFragment().apply {}
            generalAppDialogFragment.claimListener = claimListener
            generalAppDialogFragment.variableData = variableData
            generalAppDialogFragment.show(
                fragmentManager,
                PopUpRedirectionHomeDialogFragment::class.java.simpleName
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
                add(this@PopUpRedirectionHomeDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private lateinit var claimListener: (beneficiaryData: String) -> Unit

    fun newInstance(): PopUpRedirectionHomeDialogFragment? {
        val fragment = PopUpRedirectionHomeDialogFragment()
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
        binding = DialogPopupRedirectionHomeBinding.inflate(inflater, container, false);

        /*BounceView.addAnimTo(binding.imageViewClose)
        BounceView.addAnimTo(binding.buttonWhatsApp)
        BounceView.addAnimTo(binding.buttonShare)*/

        binding.imageViewClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        binding.textViewDescription.text = variableData?.POPUP_NOTIFICATION_CONTENT
        binding.textViewDescription.gone()
        shareText = "${PrefKeys.getVariableData()?.refer_friend_message}" +
                " ${PrefKeys.getVariableData()?.dOWNLOADURL} " +
                "and use my referral code: ${PrefKeys.getUserCommon()?.referralCode}"
//        loadImage(binding.imageViewPopUp,variableData?.POPUP_NOTIFICATION_IMAGE) // checking for full image
        ///
        binding.imageViewGamePromotion.visible()
        loadCornerImage(binding.imageViewGamePromotion, variableData?.POPUP_NOTIFICATION_IMAGE)
        binding.imageViewGamePromotion.setOnClickListener {
            if (variableData?.POPUP_NOTIFICATION_URL != "")
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(variableData?.POPUP_NOTIFICATION_URL)
                    )
                )
        }
        ///
        when (variableData?.POPUP_NOTIFICATION_TYPE) {
            "GAME_PROMOTION" -> {
                binding.buttonWhatsApp.gone()
                binding.buttonShare.gone()
                binding.buttonPromocode.gone()
                binding.buttonPlayNow.visible()

                binding.buttonPlayNow.setOnClickListener {
                    variableData?.POPUP_NOTIFICATION_GAME_PROMOTION_ARRAY?.let { it1 ->
                        IntentHelper.getGameScreenIntent(
                            requireActivity(),
                            it1
                        )
                    }?.let { it2 -> startActivityCustom(it2) }
                    dismissAllowingStateLoss()
                }
            }
            "REFERRAL" -> {
                binding.buttonWhatsApp.visible()
                binding.buttonShare.visible()
                binding.buttonPlayNow.gone()
                binding.buttonPromocode.gone()
                binding.buttonWhatsApp.setOnClickListener {
                    val url =
                        "https://api.whatsapp.com/send?text=$shareText"//phone="${PrefKeys.getVariableData()?.whatsAppContact}"
                    if (isAppInstalled("com.whatsapp.w4b")) {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    } else if (isAppInstalled("com.whatsapp")) {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    } else {
                        Toast.makeText(
                            activity,
                            "Whatsapp app not installed in your phone",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                    dismissAllowingStateLoss()
                }
                binding.buttonShare.setOnClickListener {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                    sendIntent.setType("text/plain")
                    startActivity(sendIntent)
                    dismissAllowingStateLoss()
                }
            }
            "PROMO_CODES" -> {
                binding.buttonWhatsApp.gone()
                binding.buttonShare.gone()
                binding.buttonPlayNow.gone()
                binding.buttonPromocode.visible()
                binding.buttonPromocode.setOnClickListener {
                    (activity as HomeActivity).showAddMoneyFragment()

//                    startActivityCustom(IntentHelper.getAddCashScreenIntent(requireContext()))
                    dismissAllowingStateLoss()
                }
            }
        }

        return binding.root
    }

    private fun isAppInstalled(packageName: String): Boolean {
        val pm1 = activity?.packageManager
        var appInstalled = try {
            pm1?.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return appInstalled
    }
}
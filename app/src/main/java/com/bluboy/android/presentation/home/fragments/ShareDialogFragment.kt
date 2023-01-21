package com.bluboy.android.presentation.home.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bluboy.android.BuildConfig
import com.bluboy.android.R
import com.bluboy.android.databinding.FragmentShareDialogBinding
import com.bluboy.android.presentation.home.activities.ReferAndEarnActivity
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurConfig
import com.bluboy.android.presentation.utility.customBlurDialogFragment.BlurDialogFragment
import com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata.SmartAsyncPolicyHolder
import com.bluboy.android.presentation.utility.setSafeOnClickListener
import com.bluboy.android.presentation.utility.titleCase
import com.bluboy.android.presentation.utility.toastError
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class ShareDialogFragment : BlurDialogFragment() {

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager
        ) {
            val generalAppDialogFragment = ShareDialogFragment().apply {}
            generalAppDialogFragment.show(
                fragmentManager,
                ShareDialogFragment::class.java.simpleName
            )
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().apply {
                add(this@ShareDialogFragment, tag)
                commitAllowingStateLoss()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }


    fun newInstance(): ShareDialogFragment? {
        val fragment = ShareDialogFragment()
        fragment.setStyle(
            DialogFragment.STYLE_NO_TITLE,
            R.style.EtsyBlurDialogTheme
        )
        return fragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogSlideAnimation
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun blurConfig(): BlurConfig {
        return BlurConfig.Builder()
            .overlayColor(Color.argb(150, 0, 0, 0)) // semi-transparent white color
            .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
            .debug(true)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_share_dialog, null, false)
        val binding = FragmentShareDialogBinding.bind(view)

        binding.viewClose.setSafeOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.shareWhatsapp.setSafeOnClickListener {
            shareSocial("whatsapp")
        }

        binding.shareTwitter.setSafeOnClickListener {
            shareSocial("twitter")
        }
        binding.shareMessenger.setSafeOnClickListener {
            shareSocial("messenger")
        }
        binding.shareFacebook.setSafeOnClickListener {
            shareSocial("facebook")
        }


        return view
    }

    private fun shareSocial(appType: String) {
        val url =
            "https://blueboy.staging-server.in/uploads/logo.png"
        //PrefKeys.getUserCommon()?.profilePic.toString()
        Glide.with(requireContext())
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(900, 900) {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    shareImage(bitmap, appType)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    fun shareImage(bitmap: Bitmap, app: String) {
        // save bitmap to cache directory
        try {
            val cachePath = File(context?.getCacheDir(), "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream =
                FileOutputStream(cachePath.toString() + "/image.png") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var contentUri: Uri? = null
        try {
            val imagePath = File(context?.getCacheDir(), "images")
            val newFile = File(imagePath, "image.png")
            contentUri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID.toString() + ".provider",
                newFile
            )
        } catch (e: Exception) {
        }

        val shareIntent = Intent()
        shareIntent.setAction(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
        if (contentUri != null) {
            shareIntent.setDataAndType(
                contentUri,
                context?.getContentResolver()?.getType(contentUri)
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, (activity as ReferAndEarnActivity).shareText)
        when (app) {
            "whatsapp" -> {
                shareIntent.setPackage(getString(R.string.whatsappPackage))
            }
            "facebook" -> {
                shareIntent.setPackage(getString(R.string.facebookPackage))
            }
            "messenger" -> {
                shareIntent.setPackage(getString(R.string.messengerPackage))
            }
            "twitter" -> {
                shareIntent.setPackage(getString(R.string.twitterPackage))
            }
        }

        shareIntent.setType("image/png")



        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            requireContext().toastError("Please install ${app.titleCase()} first")
        }

        dialog?.dismiss()
    }

}
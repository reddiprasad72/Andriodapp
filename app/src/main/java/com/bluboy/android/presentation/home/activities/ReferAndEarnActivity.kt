package com.bluboy.android.presentation.home.activities

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.BuildConfig
import com.bluboy.android.R
import com.bluboy.android.data.models.Refer
import com.bluboy.android.data.models.User
import com.bluboy.android.databinding.ActivityLeaderboardBinding
import com.bluboy.android.databinding.ActivityReferAndEarnBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.LogoutDialogFragment
import com.bluboy.android.presentation.home.HomeActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.home.adapter.MyReferAdapter
import com.bluboy.android.presentation.home.fragments.ShareDialogFragment
import com.bluboy.android.presentation.utility.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.properties.Delegates

class ReferAndEarnActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private lateinit var binding: ActivityReferAndEarnBinding
    private var arrayMyRefer = ArrayList<Refer>()
    private lateinit var myReferAdapter: MyReferAdapter
    private var userData: User? = null
    private var page = 1
    var shareText: String = ""
    private var referral: String = ""

    var selectedTab by Delegates.observable(-1) { property, oldValue, newValue ->
        if (oldValue != newValue) {
//            defaultTabInit(newValue)
            // binding.viewPager.setCurrentItem(newValue, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferAndEarnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()

        attachObserver()

        binding.tool.txtHeader.text = getString(R.string.label_refer_earn)
        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        //not for coming soon
        /*BounceView.addAnimTo(binding.facebook)
        BounceView.addAnimTo(binding.messenger)
        BounceView.addAnimTo(binding.twitter)
        BounceView.addAnimTo(binding.whatsapp)
        BounceView.addAnimTo(binding.textViewCode)*/

        binding.textViewGetRsTen.text = getString(
            R.string.message_get_every_referral,
            PrefKeys.getVariableData()?.referredBonus
        )

        binding.textViewRefferDetail.text = getString(
            R.string.message_refer_deposite_every_time,
            PrefKeys.getVariableData()?.referredBonus
        )

        /*binding.whatsapp.setSafeOnClickListener {
            shareSocial("whatsapp")
        }
        binding.twitter.setSafeOnClickListener {
            shareSocial("twitter")
        }
        binding.messenger.setSafeOnClickListener {
            shareSocial("messenger")
        }
        binding.facebook.setSafeOnClickListener {
            shareSocial("facebook")
        }*/

        /*binding.tab1Inactive.setSafeOnClickListener {
            //Daily
            selectedTab = 0
        }

        binding.tab2Inactive.setSafeOnClickListener {
            //Weekly
            selectedTab = 1
        }*/

        binding.imageViewShare.setSafeOnClickListener {
            ShareDialogFragment.showDialog(supportFragmentManager)
//            setBottomSheetVisibility(true)
        }

        /*binding.imageViewShare.setSafeOnClickListener {
//            shareSocial()
            val url = "https://blueboy.staging-server.in/uploads/logo.png"
//            val url = "https://empiregames-new.staging-server.in/uploads/general/default-user.png"
            //PrefKeys.getUserCommon()?.profilePic.toString()
            Glide.with(this@ReferAndEarnActivity)
                .asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>(900, 900) {
                    override fun onResourceReady(
                        bitmap: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        shareImage(bitmap)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }*/

        selectedTab = 0
        binding.textViewCode.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("ReferralCode", referral)//shareText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this@ReferAndEarnActivity, "Referral Code copied", Toast.LENGTH_SHORT)
                .show()
        }

        arrayMyRefer.clear()
        var linearLayoutManager =
            LinearLayoutManager(this@ReferAndEarnActivity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewMyRefer.layoutManager = linearLayoutManager
        myReferAdapter = MyReferAdapter(this@ReferAndEarnActivity, arrayMyRefer) {
        }
        binding.recyclerViewMyRefer.adapter = myReferAdapter
        init()
    }

    /*private fun shareSocial() {
        val url =
            "https://blueboy.staging-server.in/uploads/general/default-user.png"
        //PrefKeys.getUserCommon()?.profilePic.toString()
        Glide.with(this@ReferAndEarnActivity)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(900, 900) {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    shareImage(bitmap)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }*/

    override fun onResume() {
        super.onResume()
        page = 1
        homeViewModel.getProfile()
        homeViewModel.ApiReferralHistory(page)
    }

    /*private fun defaultTabInit(pos: Int) {
        binding.apply {
            tab1Active.invisible()
            tab2Active.invisible()
            myReferGroup.gone()
            scroll.gone()
            llEmpty.root.gone()
        }

        when (pos) {
            0 -> {
                binding.tab1Active.visible()
                binding.scroll.visible()
                binding.myReferGroup.gone()
            }

            1 -> {
                binding.tab2Active.visible()
                binding.scroll.gone()
                binding.myReferGroup.visible()

                if (arrayMyRefer.size != 0) {
                    binding.llEmpty.root.gone()
                    binding.recyclerViewMyRefer.visible()
                } else {
                    binding.llEmpty.root.visible()
                    binding.recyclerViewMyRefer.gone()
                }
            }
        }
    }*/

    @SuppressLint("FragmentLiveDataObserve")
    private fun attachObserver() {
        homeViewModel.profileObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                it.user?.let { it1 -> PrefKeys.setUser(it1) }
                userData = it.user
                referral = it.user!!.referralCode

                binding.textViewCode.text = it.user?.referralCode
                shareText =
                    "${PrefKeys.getVariableData()?.refer_friend_message}" +
                            " ${PrefKeys.getVariableData()?.dOWNLOADURL} " +
                            "and use my referral code: ${it.user?.referralCode}"
            }
        })

        homeViewModel.referralHistoryObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {
                arrayMyRefer.clear()
                arrayMyRefer.addAll(it.data.referData)
                checkHistory()
                myReferAdapter.notifyDataSetChanged()
            } else {
                arrayMyRefer.clear()
                checkHistory()
            }
        })
    }

    fun checkHistory() {
        if (arrayMyRefer.size != 0) {
            binding.llEmpty.gone()
            binding.recyclerViewMyRefer.visible()
        } else {
            binding.llEmpty.visible()
            binding.recyclerViewMyRefer.gone()
        }
    }

    private fun init() {
        attachObserver()
    }

    /*private fun shareReferralCode() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND

        intent.type = "text/plain"
        val subject = "Bluboy App join invitation"
        val message = "\n" +
                "\n" +
                "Hi there!\n" +
                "\n" +
                "Recently I’ve started using this gaming app.\n" +
                "\n" +
                "You can download this app from play store from below link:\n" +
                "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n" +
                "You can use below referral code while sign up:" +
                "\nReferral code- $referral\"" +
                "\n"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(Intent.createChooser(intent, "Share"))
    }*/

    fun shareImage(bitmap: Bitmap) {
        // save bitmap to cache directory
        try {
            val cachePath = File(this.cacheDir, "images")
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
            val imagePath = File(this.cacheDir, "images")
            val newFile = File(imagePath, "image.png")
            contentUri = FileProvider.getUriForFile(
                this@ReferAndEarnActivity,
                BuildConfig.APPLICATION_ID.toString() + ".provider",
                newFile
            )
        } catch (e: Exception) {
        }

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
        if (contentUri != null) {
            shareIntent.setDataAndType(
                contentUri,
                this.contentResolver?.getType(contentUri)
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)

        shareIntent.type = "image/png"
        startActivity(Intent.createChooser(shareIntent, "Choose an app"))

        /*try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            toastError("Please install ${app.titleCase()} first")
        }*/
    }
}
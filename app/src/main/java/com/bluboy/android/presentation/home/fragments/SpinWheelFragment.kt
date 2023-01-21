package com.bluboy.android.presentation.home.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bluboy.android.R
import com.bluboy.android.data.models.SpinData
import com.bluboy.android.databinding.FragmentSpinWheelBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.dialog.SpinWheelWinDialogFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.utility.spincircle.LuckyItem
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SpinWheelFragment : BaseFragment() {
    private val homeViewModel: HomeViewModel by viewModel()

    private var alreadySpinned = "Y"
    private var lastTimeSpin = ""
    private var winnningPrice = ""
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var mp: MediaPlayer
    private lateinit var mContext: Context

    private lateinit var binding: FragmentSpinWheelBinding
    private lateinit var animation: Animation

    var luckyArray: ArrayList<LuckyItem> = ArrayList()
    var colorArray: ArrayList<Int> = ArrayList()
    var spinPrice: ArrayList<SpinData> = ArrayList()
    var isCorrectTime: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSpinWheelBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        isCorrectTime = isTimeAutomatic(mContext)
        init()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.txtHeader.text = ""
        binding.toolbar.txtHeader.invisible()
        /*binding.toolbar.imageViewBack.gone()*/
        binding.toolbar.imageViewBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun init() {

        attachObserver()
        luckyArray.clear()
        colorArray.clear()
        spinPrice.clear()
        mp = MediaPlayer.create(mContext, R.raw.spinwheel)
        // showProgress()
        binding.mShimmerView.visible()
        homeViewModel.getSpinList()
        binding.imageViewClose.setOnClickListener { requireActivity().onBackPressed() }
        animation = AnimationUtils.loadAnimation(mContext, R.anim.swinging)

        // spinner
        if (!isCorrectTime) {
            binding.buttonSpinYourWheel.gone()
            binding.textViewAvailabeAfterTime.visible()
            binding.textViewAvailabeAfterTime.text =
                "Incorrect date time. Please check your clock Setting."
        }

        binding.buttonSpinYourWheel.setOnClickListener {
            if (!isTimeAutomatic(mContext)) {
                CommonAppDialogFragment.showDialog(
                    childFragmentManager,
                    getString(R.string.app_name),
                    "Incorrect Date Time Detected", "OK", "", {
                    }, {

                    })
                return@setOnClickListener
            }
            binding.buttonSpinYourWheel.gone()
            disableTouch(requireActivity())

            if (alreadySpinned == "N") {
                if (::mp.isInitialized) {
                    mp.start()
                }

                if (!mp.isPlaying)
                    mp.start()

                var index = getRandomIndex()
                binding.buttonSpinYourWheel.gone()
                binding.luckyWheel.startLuckyWheelWithTargetIndex(index)
                binding.imageViewHead.startAnimation(animation)

            } else {
                requireActivity().toastError("Please come back tomorrow for next spin")
            }
        }

        binding.luckyWheel.setLuckyRoundItemSelectedListener { index ->
            binding.imageViewHead.clearAnimation()
            showProgress()

            winnningPrice = spinPrice[index].spinAmount
            homeViewModel.getSpinWin(spinPrice[index].spinId)
        }
    }



    override fun onResume() {
        super.onResume()
        mp = MediaPlayer.create(mContext, R.raw.spinwheel)
    }

    private fun attachObserver() {
        homeViewModel.spinListObserver.observe(requireActivity(), androidx.lifecycle.Observer {
            hideProgress()

            if (it.status == 1) {
                alreadySpinned = it.data.userSpin
                lastTimeSpin = it.data.lastSpinTime

                spinPrice.clear()
                spinPrice.addAll(it.data.spins)

                colorArray.clear()
                colorArray.add(0xffFFE5D6.toInt())
                colorArray.add(0xffD1373F.toInt())
                colorArray.add(0xff8F00FF.toInt())
                colorArray.add(0xff00E5FF.toInt())
                colorArray.add(0xffFFAA00.toInt())
                colorArray.add(0xff000853.toInt())
                colorArray.add(0xffF6C358.toInt())
                colorArray.add(0xff531F00.toInt())

                colorArray.add(0xffFFE5D6.toInt())
                colorArray.add(0xffD1373F.toInt())
                colorArray.add(0xff8F00FF.toInt())
                colorArray.add(0xff00E5FF.toInt())
                colorArray.add(0xffFFAA00.toInt())
                colorArray.add(0xff000853.toInt())
                colorArray.add(0xffF6C358.toInt())
                colorArray.add(0xff531F00.toInt())

                colorArray.add(0xffFFE5D6.toInt())
                colorArray.add(0xffD1373F.toInt())
                colorArray.add(0xff8F00FF.toInt())
                colorArray.add(0xff00E5FF.toInt())
                colorArray.add(0xffFFAA00.toInt())
                colorArray.add(0xff000853.toInt())
                colorArray.add(0xffF6C358.toInt())
                colorArray.add(0xff531F00.toInt())

                luckyArray.clear()
                for (i in 0 until spinPrice.size) {
                    var luckyItem = LuckyItem()
                    luckyItem.color = colorArray[i]
                    luckyItem.secondaryText = "₹" + spinPrice[i].spinAmount
                    if (spinPrice[i].spinAmount == "0") {
                        luckyItem.topText = "Better luck"
                        luckyItem.thirdText = "next"
                        luckyItem.fourText = "t i  m  e"
                    } else {
                        luckyItem.topText = ""
                        luckyItem.thirdText = ""
                        luckyItem.fourText = ""
                    }
                    luckyArray.add(luckyItem)
                }
                binding.luckyWheel.setData(luckyArray)
                binding.luckyWheel.setRound(26)
                binding.mShimmerView.gone()

                if (luckyArray.isEmpty()) {
                    binding.buttonSpinYourWheel.gone()
                } else
                    if (lastTimeSpin == "0" || alreadySpinned == "N") {

                        isCorrectTime = isTimeAutomatic(mContext)
                        if (!isCorrectTime) {
                            binding.buttonSpinYourWheel.gone()
                            binding.textViewAvailabeAfterTime.visible()
                            binding.textViewAvailabeAfterTime.text =
                                "Incorrect date time. Please check your clock Setting."
                        } else {
                            binding.buttonSpinYourWheel.visible()
                            binding.textViewAvailabeAfterTime.gone()
                        }
                    } else {
                        binding.buttonSpinYourWheel.gone()
                        binding.textViewAvailabeAfterTime.visible()
                        if (isCorrectTime) {
                            countDown()
                        } else {
                            binding.textViewAvailabeAfterTime.text =
                                "Incorrect date time. Please check your clock Setting."
                        }
                    }
            }
        })

        homeViewModel.spinWinObserver.observe(requireActivity(), androidx.lifecycle.Observer {
            hideProgress()
            enableTouch(requireActivity())
            if (it.status == 1) {
                binding.buttonSpinYourWheel.gone()
                binding.textViewAvailabeAfterTime.visible()

                //countDown()

                if (winnningPrice == "0") {
                    CommonAppDialogFragment.showDialog(
                        childFragmentManager,
                        getString(R.string.app_name),
                        "Better luck next time !!!", "OK", "", {
                            startActivityCustom(
                                IntentHelper
                                    .getHomeScreenIntent(
                                        context = mContext, isClearFlag = true
                                    )
                            )
                        }, {

                        })
                } else {
                    if (winnningPrice.isNotBlank())
                        SpinWheelWinDialogFragment.showDialog(childFragmentManager, winnningPrice) {

                            startActivityCustom(
                                IntentHelper.getHomeScreenIntent(
                                    context = mContext,
                                    isClearFlag = true,
                                    openFromSpin = true
                                )
                            )
                        }
                }
            } else {
                startActivityCustom(
                    IntentHelper
                        .getHomeScreenIntent(mContext)
                )
            }
        })
    }

    //Countdown Timer after Spin Occur to Stop for 24 Hours
    private fun countDown() {
        countDownTimer = object : CountDownTimer(
            (lastTimeSpin.toLong() * 1000) - Calendar.getInstance().time.time,
            1000
        ) {
            override fun onTick(p0: Long) {
                val millis: Long = p0
                val hms = String.format(
                    "%02d : %02d : %02d",
                    (TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(
                        TimeUnit.MILLISECONDS.toDays(
                            millis
                        )
                    )),
                    (TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
                    (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millis)
                    ))
                )

                binding.textViewAvailabeAfterTime.text = "Next free spin after " + hms//set text
            }

            override fun onFinish() {
                luckyArray.clear()
                colorArray.clear()
                colorArray.clear()
                spinPrice.clear()
                //showProgress()
                homeViewModel.getSpinList()
            }
        }
        countDownTimer.start()
    }

    //Getting Random Index to Stop Pin
    private fun getRandomIndex(): Int {
        var random = Random()
        return random.nextInt(luckyArray.size)
    }

    override fun onStop() {
        super.onStop()
        if (::mp.isInitialized) {
            mp.stop()
            mp.release()
        }
        binding.imageViewHead.clearAnimation()
        binding.luckyWheel.setStop(true)
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    //Checking if Automatic Time Setting is Enabled in Device
    private fun isTimeAutomatic(c: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) === 1
        } else {
            Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1
        }
    }
}
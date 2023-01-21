package com.bluboy.android.presentation.loginsignup.activities

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.pixplicity.easyprefs.library.Prefs
import com.bluboy.android.R
import com.bluboy.android.data.models.IpResponse
import com.bluboy.android.databinding.ActivityOtpVerificationBinding
import com.bluboy.android.domain.network.ServiceApiCall.WebApiCall
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.loginsignup.LoginSignupViewModel
import com.bluboy.android.presentation.utility.*
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern

class OtpVerificationActivity : BaseActivity() {

    private val loginSignupViewModel: LoginSignupViewModel by viewModel()

    override fun getBaseViewModel() = loginSignupViewModel

    private lateinit var binding: ActivityOtpVerificationBinding

    private var smsVerifyCatcher: SmsVerifyCatcher? = null

    var countSend = 1;

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setAdjustNothing()
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        init()

        smsVerifyCatcher = SmsVerifyCatcher(
            this
        ) { message ->
            Log.i("CODE", message.toString())
            val code: String = parseCode(message).toString()
            //Parse verification code
            Log.i("CODE", code.toString())
            binding.pinView.setText("")
            binding.pinView.setText(code.toString())
            showProgress()
            loginSignupViewModel.verifyOtp(
                binding.pinView.text.toString(),
                Prefs.getString(PrefKeys.PushTokenKey, ""),
                getDeviceName(),
                getDeviceUniqueId(),
                AppConstant.DeviceTypeAndroid
            )
//            etCode.setText(code) //set code in edit text
            //then you can send verification code to server
        }
    }

    override fun onStart() {
        super.onStart()
        smsVerifyCatcher?.onStart();
    }

    override fun onStop() {
        super.onStop()
        smsVerifyCatcher?.onStop();
    }

    fun parseCode(message: String?): String? {
        val p: Pattern = Pattern.compile("\\b\\d{4}\\b")
        val m: Matcher = p.matcher(message)
        var code = ""
        while (m.find()) {
            code = m.group(0)
        }
        return code
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults!!)
        smsVerifyCatcher!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun init() {

        binding.textFourDigit.text =
            "${getString(R.string.label_four_digit)} ${PrefKeys.getUserCommon()?.phone!!}"

        binding.tvResendOtp.setText(getString(R.string.otp_text))

        val wordtoSpan: Spannable =
            SpannableString(getString(R.string.otp_text))

        val clickSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(this@OtpVerificationActivity, R.color.colorTerms)
                ds.isUnderlineText = false // this remove the underline
                ds.isFakeBoldText = true
            }

            override fun onClick(textView: View) {
                // handle click event
            }
        }
        wordtoSpan.setSpan(
            clickSpan,
            25,
            binding.tvResendOtp.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvResendOtp.text = wordtoSpan

        binding.tool.txtHeader.text = getString(R.string.verify_code)
        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ipify.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        val webApiCall = retrofit.create(WebApiCall::class.java)
        val call: Call<IpResponse> = webApiCall.getIPAddress("json")
        call.enqueue(object : Callback<IpResponse> {
            override fun onResponse(call: Call<IpResponse>, response: Response<IpResponse>) {
                if (PrefKeys.getAuthKey() != "") {
                    loginSignupViewModel.getCheckRootDevice(response.body()?.ip.toString(), "N")
                }
            }

            override fun onFailure(call: Call<IpResponse>, t: Throwable) {
                Log.i("IpAddress >>", t.message.toString())
            }
        })

        attachObserver()
        setupClickListeners()
        startTimer()

    }

    private fun attachObserver() {
        loginSignupViewModel.optVerificationRSLiveData.observe(this, Observer {
            it?.apply {
                hideProgress()
                if (it.status == 1) {
                    it?.user?.let { it1 -> PrefKeys.setUser(it1) }

                    if (it.user?.isProfileCompleted.equals(AppConstant.No)) {
                        startActivityCustom(IntentHelper.getSignupScreenIntent(this@OtpVerificationActivity))
                    } else {
                        startActivityCustom(
                            IntentHelper.getHomeScreenIntent(
                                context = this@OtpVerificationActivity,
                                isClearFlag = true
                            )
                        )
                    }
                } else {
                    if (StringUtils.isValid(it.message!!)) {
                        toastError(it.message!!)
                        binding.pinView.setText("")
                    }

                    if (countSend > 2) {
                        Prefs.putString(PrefKeys.AuthKey, "")
                        startActivityCustom(IntentHelper.getLoginScreenIntent(this@OtpVerificationActivity))
                    }
                }
            }
        })

        loginSignupViewModel.resendOtpRSLiveData.observe(this, Observer {
            it?.apply {
                hideProgress()

                startTimer()
                binding.tvResendOtp.isEnabled = false
                binding.tvResendOtp.isVisible = false

                if (countSend < 3) {
                    binding.textViewOtpLabel.visible()
                    binding.textViewOtp.visible()
                }

                binding.tvResendOtp.setTextColor(
                    ContextCompat.getColor(
                        this@OtpVerificationActivity,
                        R.color.colorWhiteOpacity50
                    )
                )

                CommonAppDialogFragment.showDialog(supportFragmentManager,
                    getString(R.string.app_name), this.message.orEmpty(),
                    getString(R.string.ok), "",
                    {
//                        killProcess()
                    },
                    {},
                    {
//                                finish()
                    })
//                showDialog(getString(R.string.app_name),
//                    this.message.orEmpty(),
//                    getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
//                        dialog.dismiss()
//                    })
//                startTimer()
            }
        })
    }

    private fun startTimer() {
        object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
//                binding.tvSec onds.visible()
//                binding.tvResendCode.invisible()
                var second = ""
                if ((millisUntilFinished / 1000) < 10) {
                    second = "0" + (millisUntilFinished / 1000)
                } else {
                    second = (millisUntilFinished / 1000)?.toString()
                }
                binding.textViewOtp.text = "00:${second}"
            }

            override fun onFinish() {
//                showProgress()
                binding.tvResendOtp.isEnabled = true

                if (countSend < 3)
                    binding.tvResendOtp.isVisible = true

                binding.textViewOtpLabel.invisible()
                binding.textViewOtp.invisible()
                binding.tvResendOtp.setTextColor(
                    ContextCompat.getColor(
                        this@OtpVerificationActivity,
                        R.color.colorWhite
                    )
                )
//                loginSignupViewModel.resendOtp(PrefKeys.getUserCommon()?.phone!!)
//                startTimer()
//                binding.tvSeconds.invisible()
//                binding.tvResendCode.visible()
            }
        }.start()
    }

    private fun setupClickListeners() {

        binding.tvResendOtp.setSafeOnClickListener {
            countSend++
            showProgress()
            loginSignupViewModel.resendOtp(PrefKeys.getUserCommon()?.phone!!)
        }


        binding.buttonSubmit.setSafeOnClickListener {
//            startActivityCustom(IntentHelper.getSignupScreenIntent(this@OtpVerificationActivity))
            showProgress()
            loginSignupViewModel.verifyOtp(
                binding.pinView.text.toString(),
                Prefs.getString(PrefKeys.PushTokenKey, ""),
                getDeviceName(),
                getDeviceUniqueId(),
                AppConstant.DeviceTypeAndroid
            )
        }

        /*BounceView.addAnimTo(binding.buttonSubmit)*/


//        binding.btnVerify.setOnClickListener {
//            verifyOtp()
//        }
//
//        binding.tvResendCode.setOnClickListener {
//            resendOtp()
//        }
//
//        binding.ivBack.setOnClickListener {
//            onBackPressed()
//        }
    }

//    private fun verifyOtp() {
//        val otp = binding.otpView.text.toString()
//        if (otp.length < this.resources.getInteger(R.integer.otp_length)) {
//            showDialog(getString(R.string.app_name),
//                getString(R.string.error_otp),
//                getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
//                    dialog.dismiss()
//                })
//            return
//        }
//        showProgress()
//        val otpVerificationPRQ = OtpVerificationPRQ(
//            user?.authKey.orEmpty(),
//            otp
//        )
//        loginSignupViewModel.verifyOtp(otpVerificationPRQ)
//    }


}
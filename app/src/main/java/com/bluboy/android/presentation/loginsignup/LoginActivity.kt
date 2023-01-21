package com.bluboy.android.presentation.loginsignup

import android.Manifest
import android.content.DialogInterface
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationListener
import com.pixplicity.easyprefs.library.Prefs
import com.bluboy.android.R
import com.bluboy.android.data.models.UserLoginPRQ
import com.bluboy.android.databinding.ActivityLoginBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.core.location.MyLocationListener
import com.bluboy.android.presentation.core.location.MyLocationManager
import com.bluboy.android.presentation.utility.*
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    private val loginSignupViewModel: LoginSignupViewModel by viewModel()
    override fun getBaseViewModel() = loginSignupViewModel
    private var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityLoginBinding
    private var mLocationProviderHelper: LocationProviderHelper? = null
    private var mLocation: Location? = null
    private var myLocationManager: MyLocationManager? = null
    private var readSMSPermission = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setAdjustPan()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        init()

        mLocationProviderHelper = LocationProviderHelper(this, LocationListener {
            this.mLocation = it
            Logger.e("Location : " + it.latitude + "," + it.longitude)
            mLocationProviderHelper?.stopLocationUpdates()
        })

        mLocationProviderHelper?.createLocationRequest()

        myLocationManager = MyLocationManager(this)
        myLocationManager?.myLocationManager = object : MyLocationListener {
            override fun onLocationReceived(location: Location) {
                Log.e(
                    "OtpLoginActivity",
                    "Location :" + location.latitude + " " + location.longitude
                )
                myLocationManager?.stopLocation()
            }

            override fun onLocationError() {
            }
        }
        myLocationManager?.startLocation()
    }

    private fun init() {
        attachObserver()
        checkForPermission()

        binding.checkboxTermsConditions.setOnCheckedChangeListener { buttonView, isChecked ->
            hideKeyboardFrom(buttonView)
        }

        binding.buttonGetStarted.setSafeOnClickListener {
            if (readSMSPermission) {
                if (isValid()) {

                    showProgress()
                    var loginPRQ = UserLoginPRQ()
                    loginPRQ.phone = binding.editTextMobile.text.toString()
                    loginPRQ.device_token = Prefs.getString(PrefKeys.PushTokenKey, "")
                    loginPRQ.device_name = getDeviceName()
                    loginPRQ.device_unique_id = getDeviceUniqueId()
                    loginPRQ.device_type = AppConstant.DeviceTypeAndroid
                    loginPRQ.app_version = getAppVersion()

                    mLocation.apply {
                        loginPRQ.latitude = this?.latitude.toString()
                        loginPRQ.longitude = this?.longitude.toString()
                        loginSignupViewModel.login(loginPRQ)
                    }
                }
            } else {
                checkForPermission()
            }
        }

        /*BounceView.addAnimTo(binding.buttonGetStarted)*/

        val wordtoSpan: Spannable =
            SpannableString(getString(R.string.i_agree_with_the_terms))

        val termClick = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivityCustom(
                    IntentHelper.getCMSScreenIntent(
                        this@LoginActivity, getString(R.string.label_terms_condition)
                    )
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.text_heading_color)
                ds.isUnderlineText = false
            }
        }

        val privacyClick = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivityCustom(
                    IntentHelper.getCMSScreenIntent(
                        this@LoginActivity, getString(R.string.label_privacy_policy)
                    )
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.text_heading_color)
                ds.isUnderlineText = false
            }
        }

        wordtoSpan.setSpan(
            privacyClick,
            40,
            binding.iAgree.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        wordtoSpan.setSpan(
            termClick,
            17,
            35,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.iAgree.text = wordtoSpan
        binding.iAgree.movementMethod = LinkMovementMethod.getInstance()
        binding.iAgree.highlightColor = ContextCompat.getColor(this, R.color.colorTransparent)

    }

    private fun attachObserver() {
        loginSignupViewModel.loginUserRSLiveData.observe(this, Observer {
            it?.apply {
                hideProgress()
                if (this.isSuccess()) {
                    this.user?.apply {
                        PrefKeys.setUser(this)
                        var user = PrefKeys.getUserCommon()
                        user?.phoneVerificationStatus = "N"
                        PrefKeys.setUser(user!!)
                    }
                    toastSuccess(this.message.orEmpty())
//                    if (this.isSuccess() && this.user?.phoneVerificationStatus == AppConstant.Yes && user?.isProfileCompleted == AppConstant.Yes) {
//                        startActivityCustom(
//                            IntentHelper.getHomeScreenIntent(this@LoginActivity, true)
//                        )
//                    } else {
                    startActivityCustom(
                        IntentHelper
                            .getOtpScreenIntent(this@LoginActivity)
                    )
//                    }
                } else {
                    toastError(this.message.orEmpty())
                }
            }
        })
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
        }
        this.doubleBackToExitPressedOnce = true
        showMessage(getString(R.string.back_to_exit))
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun isValid(): Boolean {
        if (StringUtils.isEmpty(binding.editTextMobile.text.toString())) {
            toastError(getString(R.string.error_mobile_empty))
            return false
        } else if (!StringUtils.isValidPhone(binding.editTextMobile.text.toString())) {
            toastError(getString(R.string.error_mobile))
            return false
        } else if (binding.editTextMobile.text.toString().startsWith('0')) {
            toastError(getString(R.string.error_mobile))
            return false
        } else if (!binding.checkboxTermsConditions.isChecked) {
            toastError(getString(R.string.validation_accept_terms_condition))
            return false
        }
        return true
    }

    private fun checkForPermission() {
        MayI.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_SMS,
            )
            .onRationale(this::permissionRationaleMulti)
            .onResult(this::permissionResultMulti)
            .onErrorListener(this::inCaseOfError)
            .check()
    }

    private fun permissionRationaleMulti(
        permissions: Array<PermissionBean>,
        token: PermissionToken
    ) {
        showDialog(getString(R.string.app_name),
            getString(R.string.permission_read_my_sms),
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                token.continuePermissionRequest()
            })
    }

    private fun inCaseOfError(e: Exception) {
        showDialog(getString(R.string.app_name),
            "Error for permission : " + e.message,
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
    }

    private fun permissionResultMulti(
        permissions: Array<PermissionBean>
    ) {
        val isAllPermanentlyDenied = permissions.all {
            it.isPermanentlyDenied
        }
        if (isAllPermanentlyDenied) {
            showDialog(
                getString(R.string.app_name),
                getString(R.string.permission_always_denied),
                getString(R.string.settings), { dialog, which ->
                    dialog.dismiss()
                    openPermissionSettings()
                }, getString(R.string.cancel), { dialog, which ->
                    dialog.dismiss()
                    finish()
                }
            )
            return
        }
        val isAllPermissionGranted = permissions.all {
            it.isGranted
        }
        if (isAllPermissionGranted) {
            readSMSPermission = true
        } else {
        }
    }
}

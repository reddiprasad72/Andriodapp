package com.bluboy.android.presentation.loginsignup

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.LocationListener
import com.bluboy.android.R
import com.bluboy.android.data.models.CompleteProfilePRQ
import com.bluboy.android.databinding.ActivitySignupBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.core.location.MyLocationListener
import com.bluboy.android.presentation.core.location.MyLocationManager
import com.bluboy.android.presentation.imagePickerCropper.app.SCropImageViewFragment
import com.bluboy.android.presentation.loginsignup.adapter.StateListAdapter
import com.bluboy.android.presentation.utility.*
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.*

class SignupActivity : BaseActivity() {

    private val loginSignupViewModel: LoginSignupViewModel by viewModel()

    override fun getBaseViewModel() = loginSignupViewModel
    private var doubleBackToExitPressedOnce = false

    private lateinit var binding: ActivitySignupBinding

    private var locationManager: MyLocationManager? = null
    private var mLocationProviderHelper: LocationProviderHelper? = null
    private var mLocation: Location? = null

    private var profileImageUri: Uri? = null
    private var file: File? = null
    private var userNameValid = false

    private lateinit var myStateListAdapater: StateListAdapter
    private var stateId = "0"

    private val PICK_PDF_FILE = 2

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setAdjustPan()
        binding = ActivitySignupBinding.inflate(layoutInflater)
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

    private fun init() {
        attachObserver()

        binding.tool.txtHeader.text = getString(R.string.label_signup)

        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        EditTextSearchCommon(binding.editTextCreateUserName).watch {
            if (it.length >= 3) {
                loginSignupViewModel.checkUserNameAvailabel(it)
            } else {
                userNameValid = false
                binding.editTextCreateUserName.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_tick_deselect,
                    0
                )
            }
        }

        val wordtoSpan: Spannable =
            SpannableString(getString(R.string.i_agree_with_the_terms))

        val termClick = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivityCustom(
                    IntentHelper.getCMSScreenIntent(
                        this@SignupActivity, getString(R.string.label_terms_condition)
                    )
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(this@SignupActivity, R.color.colorTerms)
                ds.isUnderlineText = false
            }
        }

        val privacyClick = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivityCustom(
                    IntentHelper.getCMSScreenIntent(
                        this@SignupActivity, getString(R.string.label_privacy_policy)
                    )
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(this@SignupActivity, R.color.colorTerms)
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

        binding.editTextState.setOnClickListener {
            binding.recyclerViewState.visible()
            var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.recyclerViewState.layoutManager = linearLayoutManager
            myStateListAdapater = PrefKeys.getVariableData()?.stateList?.let { it1 ->
                StateListAdapter(it1) {
                    binding.recyclerViewState.gone()
                    binding.editTextState.setText(it.stateName)
                    stateId = it.stateId
                }
            }!!
            binding.recyclerViewState.adapter = myStateListAdapater
        }

        setupClickListeners()
//        checkGpsStatus()
    }

    private fun attachObserver() {
        loginSignupViewModel.completeProfileRSLiveData.observe(this, Observer {
            it?.apply {
                hideProgress()
                if (this.status == 1) {
                    if (this.isSuccess()) {
                        toastSuccess(getString(R.string.message_register_successfully))
                        this.user?.apply {
                            PrefKeys.setUser(this)
                        }

                        startActivityCustom(
                            IntentHelper.getHomeScreenIntent(
                                context = this@SignupActivity,
                                isClearFlag = false,
                                openFromSignup = true
                            )
                        )
                    }
                } else {
                    toastError(this.message.toString())
                }
            }
        })

        loginSignupViewModel.checkUserNameAvailableObserver.observe(this, Observer {
            it.apply {
                hideProgress()
                if (this.status == 1) {
                    userNameValid = true
                    binding.editTextCreateUserName.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_tick_select,
                        0
                    )
                } else {
                    userNameValid = false
                    binding.editTextCreateUserName.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_tick_deselect,
                        0
                    )
                }
            }
        })
    }

    private fun setupClickListeners() {
        binding.buttonSubmit.setSafeOnClickListener {
            if (isValid()) {
                val vAutKey = PrefKeys.getAuthKey()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                var name = " "
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                val vUserName = binding.editTextCreateUserName.text.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                val vEmail = binding.editTextMail.text.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                val vLatitude = mLocation?.latitude.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                val vLongitude = mLocation?.longitude.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                val referralCode = binding.editTextReferral.text.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                val deviceUniqueId = getDeviceUniqueId()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                var state_id =
                    stateId.toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                var checkBoxStatus = if (binding.checkboxTermsConditions.isChecked) "Y" else "N"
                var termsConditionsCheckBox =
                    checkBoxStatus.toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                var imageBody: MultipartBody.Part? = null
                if (profileImageUri != null) {
                    imageBody = file?.asRequestBody("image/*".toMediaTypeOrNull())?.let { it1 ->
                        MultipartBody.Part.createFormData(
                            AppConstant.profile_pic,
                            file?.name,
                            it1
                        )
                    }
                }
                var updateProfilePRQ =
                    CompleteProfilePRQ(
                        vAutKey,
                        name,
                        vUserName,
                        vEmail,
                        vLatitude,
                        vLongitude,
                        referralCode,
                        deviceUniqueId,
                        imageBody, termsConditionsCheckBox, state_id
                    )

                showProgress()
                loginSignupViewModel.completeProfile(updateProfilePRQ)
            }
        }

        /*BounceView.addAnimTo(binding.buttonSubmit)*/

        /*binding.imageViewProfileDummy.setSafeOnClickListener {
            checkForPermission()
        }*/

        binding.imageViewProfile.setSafeOnClickListener {
            checkForPermission()
        }
    }

    private fun isValid(): Boolean {
        if (StringUtils.isEmpty(binding.editTextCreateUserName.text.toString())) {
            toastError(getString(R.string.validation_enter_username))
            return false
        } else if (!StringUtils.isValid(binding.editTextCreateUserName.text.toString(), 3)) {
            toastError(getString(R.string.validation_username))
            return false
        } else if (!userNameValid) {
            toastError(getString(R.string.validation_valid_username))
            return false
        } else if (StringUtils.isEmpty(binding.editTextMail.text.toString())) {
            toastError(getString(R.string.validation_email))
            return false
        } else if (!StringUtils.isValidEmail(binding.editTextMail.text.toString())) {
            toastError(getString(R.string.validation_email_valid))
            return false
        } else if (StringUtils.isEmpty(binding.editTextState.text.toString())) {
            toastError(getString(R.string.validation_select_State))
            return false
        } else if (!binding.checkboxTermsConditions.isChecked) {
            toastError(getString(R.string.validation_accept_terms_condition))
            return false
        }
        return true
    }

    //#region - storage permission
    private fun checkForPermission() {
        MayI.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
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
            getString(R.string.permission_storage),
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
            openImagePicker()
        } else {
            checkForLocationPermission()
        }
    }

    private fun openImagePicker() {

        ImagePicker.with(this)
//                .cropSquare()                 //Crop image(Optional), Check Customization for more option
            .compress(1024)          //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    //endregion

    //#region - location permission

    private fun checkGpsStatus() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            checkForLocationPermission()
        } else {
            showGPSDisabledAlertToUser()
        }
    }

    private fun showGPSDisabledAlertToUser() {
        showDialog(getString(R.string.app_name),
            getString(R.string.gps_permission_msg),
            getString(R.string.yes), { dialog, which ->
                dialog.dismiss()

                val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(callGPSSettingIntent)

            },
            getString(R.string.cancel), { dialog, which ->
                dialog.dismiss()
            })
    }

    private fun checkForLocationPermission() {
        MayI.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .onRationale(this::permissionRationaleMultiLocation)
            .onResult(this::permissionResultMultiLocation)
            .onErrorListener(this::inCaseOfErrorLocation)
            .check()
    }

    private fun permissionRationaleMultiLocation(
        permissions: Array<PermissionBean>,
        token: PermissionToken
    ) {
        showDialog(getString(R.string.app_name),
            getString(R.string.permission_location),
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                token.continuePermissionRequest()
            })
    }

    private fun inCaseOfErrorLocation(e: Exception) {
        showDialog(getString(R.string.app_name),
            "Error for permission : " + e.message,
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
    }

    private fun permissionResultMultiLocation(
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
            getCurrentLocation()
        }
    }

    //location for skip login

    private fun getCurrentLocation() {
        toastSuccess(getString(R.string.str_location_fetching))
        locationManager = MyLocationManager(this)
        locationManager?.myLocationManager = locationListener
        locationManager?.startLocation()
    }

    private var locationListener = object : MyLocationListener {
        override fun onLocationReceived(location: Location) {
            val strLocation = String.format("%f,%f", location.latitude, location.longitude)
            Logger.d("Location received : $strLocation")
            locationManager?.stopLocation()
        }

        override fun onLocationError() {
            showDialog(
                getString(R.string.app_name),
                getString(R.string.location_error),
                getString(R.string.ok), { dialog, which ->
                    dialog.dismiss()
                }, getString(R.string.cancel), { dialog, which ->
                    dialog.dismiss()
                })
        }
    }

    fun showSelectedFile(uri: Uri) {
        loadCircleImage(binding.imageViewProfile, uri)
        binding.imageViewProfileDummy.isVisible = false
        binding.logoBack.isInvisible = true
        profileImageUri = uri
        Logger.d("Crop URI =>" + uri)
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val pdfBytes = (contentResolver?.openInputStream(uri))?.readBytes()
            file = File(
                getExternalFilesDir(null),
                "profile_${Calendar.getInstance().time}.png"
            )
            if (file!!.exists())
                file!!.delete()
            try {
                val fos = FileOutputStream(file!!.path)
                fos.write(pdfBytes)
                fos.close()
            } catch (e: Exception) {
                Logger.d("Crop URI" + "Exception in file callback $e")
            }
        } else {
            var mPath = FilePath.getPath(this, uri)
            file = File(mPath)
        }
    }
    //endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (resultCode) {
                Activity.RESULT_OK -> {
//                    profileImageUri = data?.data
//                    file = ImagePicker.getFile(data)
//                    profileImageUri?.apply {
//                        showSelectedFile()
//                    }
                    val bundle = Bundle()
                    bundle.putString("URI_DATA", data?.data.toString())
                    bundle.putString("OPEN_FROM", "From_Signup")

                    var loadFragment = SCropImageViewFragment()
                    loadFragment.arguments = bundle
                    val manager: FragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = manager.beginTransaction()
                    transaction.add(R.id.container, loadFragment, "SCropImageViewFragment")
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    /* Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()*/
                }
            }
        } catch (e: Exception) {
        }
    }
}
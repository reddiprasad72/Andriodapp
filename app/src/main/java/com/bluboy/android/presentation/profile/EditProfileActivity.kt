package com.bluboy.android.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.LocationListener
import com.bluboy.android.R
import com.bluboy.android.data.models.CompleteProfilePRQ
import com.bluboy.android.databinding.ActivityEditProfileBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.core.location.MyLocationListener
import com.bluboy.android.presentation.core.location.MyLocationManager
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.imagePickerCropper.app.SCropImageViewFragment
import com.bluboy.android.presentation.loginsignup.LoginSignupViewModel
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

class EditProfileActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val loginSignupViewModel: LoginSignupViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel
    private var profileImageUri: Uri? = null
    private var file: File? = null
    private var userNameValid = true
    private var selectIMageTag = false

    private var mLocationProviderHelper: LocationProviderHelper? = null
    private var mLocation: Location? = null
    private var myLocationManager: MyLocationManager? = null

    private lateinit var binding: ActivityEditProfileBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
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

            override fun onLocationError() {}
        }
        myLocationManager?.startLocation()
    }

    fun setToolBar() {
        binding.toolbar.txtHeader.text = getString(R.string.label_edit_profile)
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        if (PrefKeys.getUserCommon()?.name != null) {
            val name = PrefKeys.getUserCommon()?.name?.trim()
            binding.tvName.setText(name)
        }

        if (PrefKeys.getUserCommon()?.name != null) {
            var name = PrefKeys.getUserCommon()?.name?.trim()
            binding.editTextName.setText(name)
        }
        binding.editTextUserName.setText(PrefKeys.getUserCommon()?.userName)
        binding.editTextEmail.setText(PrefKeys.getUserCommon()?.email)
        binding.editTextPhone.setText(PrefKeys.getUserCommon()?.phone)
        loadImage(binding.imageViewProfile, PrefKeys.getUserCommon()?.profilePic)
    }

    private fun init() {
        attachObserver()
        binding.imageViewEditProfile.setSafeOnClickListener {
            selectIMageTag = true
            checkForPermission()
        }

        EditTextSearchCommon(binding.editTextUserName).watch {
            if (it.length >= 3) {
                loginSignupViewModel.checkUserNameAvailabel(it)
            } else {
                userNameValid = false
                binding.editTextUserName.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_tick_deselect,
                    0
                )
            }
        }

        /*BounceView.addAnimTo(binding.btnSave)*/

        binding.btnSave.setSafeOnClickListener {
            if (isValid()) {
                val vAutKey = PrefKeys.getAuthKey()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                val name = binding.editTextName.text.toString().ifBlank {
                    " "
                }.toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                val vUserName = binding.editTextUserName.text.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                val vEmail = binding.editTextEmail.text.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                val vLatitude = mLocation?.latitude.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                val vLongitude = mLocation?.longitude.toString()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                val deviceUniqueId = getDeviceUniqueId()
                    .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                var state_id =
                    PrefKeys.getUserCommon()?.stateId?.toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

                var imageBody: MultipartBody.Part? = null
                if (profileImageUri != null) {
                    imageBody = MultipartBody.Part.createFormData(
                        AppConstant.profile_pic,
                        file!!.name,
                        file!!.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                var updateProfilePRQ = CompleteProfilePRQ(
                    vAutKey,
                    name,
                    vUserName,
                    vEmail,
                    vLatitude,
                    vLongitude,
                    "".toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull()),
                    deviceUniqueId,
                    imageBody,
                    "Y".toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull()),
                    state_id!!
                )

                showProgress()
                loginSignupViewModel.completeProfile(updateProfilePRQ)

            }
        }
    }

    private fun isValid(): Boolean {
        if (!StringUtils.isValid(binding.editTextUserName.text.toString())) {
            toastError(getString(R.string.validation_username))
            return false
        } else if (!userNameValid) {
            toastError(getString(R.string.validation_valid_username))
            return false
        } else if (!StringUtils.isValidEmail(binding.editTextEmail.text.toString())) {
            toastError(getString(R.string.validation_email))
            return false
        }
        return true
    }

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

    private fun permissionResultMulti(permissions: Array<PermissionBean>) {
        var isAllPermissionGranted = true
        permissions.forEach {
            if (!it.isGranted) {
                isAllPermissionGranted = false
                return@forEach
            }
        }
        if (isAllPermissionGranted) {

            openImagePicker()
//            ImagePicker.with(this)
//                .crop()                 //Crop image(Optional), Check Customization for more option
//                .compress(1024)          //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080(Optional)
//                .start()
        } else {
            showDialog(getString(R.string.app_name),
                getString(R.string.permission_error),
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
        }
    }

    private fun inCaseOfError(e: Exception) {
        showDialog(getString(R.string.app_name),
            "Error for permission : " + e.message,
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
    }

    private fun openImagePicker() {

        ImagePicker.with(this)
//                .cropSquare()                 //Crop image(Optional),
//                Check Customization for more option
            .compress(1024)          //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    private fun attachObserver() {
        loginSignupViewModel.completeProfileRSLiveData.observe(this, Observer {
            it?.apply {
                hideProgress()
                if (this.status == 1) {
                    toastSuccess(getString(R.string.message_edit_profile))
                    this.user?.apply {
                        PrefKeys.setUser(this)
                    }
                    finish()
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
                    binding.editTextUserName.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_tick_select,
                        0
                    )
                } else {
                    userNameValid = false
                    binding.editTextUserName.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_tick_deselect,
                        0
                    )
                }
            }
        })
    }

    fun showSelectedFile(uri: Uri) {
        loadImage(binding.imageViewProfile, uri.toString())
        profileImageUri = uri
        Logger.d("Crop URI =>" + uri)
        // Android 9 uri = file:///data/user/0/com.sikandarji.android/cache/cropped1175050468947853708.jpg
        //Android 10 uri = content://com.sikandarji.android.cropper.fileprovider/my_images/Pictures/cropped9109423492658804739.jpg
//        file = File(uri.path)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && selectIMageTag) {
            //Image Uri will not be null for RESULT_OK
//            profileImageUri = data?.data

//            getSupportFragmentManager().beginTransaction()
//            .add(R.id.container, SCropImageViewFragment.newInstance("data1","data2"),"MyFragment")
//            .commit();

            val bundle = Bundle()
            bundle.putString("URI_DATA", data?.data.toString())
            bundle.putString("OPEN_FROM", "From_Edit")

            var loadFragment = SCropImageViewFragment()
            loadFragment.arguments = bundle
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.container, loadFragment, "SCropImageViewFragment")
            transaction.addToBackStack(null)
            transaction.commit()

            //You can get File object from intent
//            file =  ImagePicker.getFile(data)!!

//            Handler(Looper.getMainLooper()).postDelayed({
//                profileImageUri?.apply {
//                    showSelectedFile()
//                }
//            },500)

            //You can also get File Path from intent
//            val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            /* Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()*/
        }
    }
}

package com.bluboy.android.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.bluboy.android.R
import com.bluboy.android.data.models.UpdateKYCPRQ
import com.bluboy.android.databinding.ActivityKycPanBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.dialog.CommonAppDialogFragment
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.imagePickerCropper.app.SCropImageViewFragment
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

class KycPANActivity : BaseActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = homeViewModel
    private lateinit var binding: ActivityKycPanBinding
    private var openFromProfile = false
    private var selectIMageTag = false
    private var profileImageUri: Uri? = null
    private var file: File? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKycPanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        openFromProfile = intent?.getBooleanExtra(AppConstant.FROM_PROFILE, false)!!
        init()
    }

    fun setToolBar() {
        binding.toolbar.imageViewBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.txtHeader.text = "PAN Card KYC"
    }

    private fun init() {
        attachObserver()

        binding.buttonContinue.setOnClickListener {
            if (binding.editTextPan.text.toString().isBlank()) {
                toastError("Please enter PAN Card Number")
            } else if (!isPanValid(binding.editTextPan.text.toString())) {
                toastError("Please enter valid PAN Card Number")
            } else if (profileImageUri == null) {
                toastError("Please upload PAN card photo")
            } else {
                showProgress()
                uploadPanImage()
            }
        }

        binding.buttonUploadFrontSide.setSafeOnClickListener {
            selectIMageTag = true
            checkForPermission()
        }
    }

    private fun attachObserver() {
        homeViewModel.updateKYCObserver.observe(this, Observer {
            hideProgress()
            if (it.status == 1) {

                CommonAppDialogFragment.showDialog(
                    supportFragmentManager,
                    getString(R.string.app_name),
                    it.message ?: "Document upload successfully",
                    "OK", "",
                    {
                        val intent = Intent()
                        intent.putExtra("MESSAGE", "Document uploaded")
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }, { })
            } else {
                toastError(it.message.toString())
            }
        })
    }

    private fun uploadPanImage() {
        var imageBody: MultipartBody.Part? = null
        var imageBody2: MultipartBody.Part? = null

        if (profileImageUri != null) {
            imageBody = file?.asRequestBody("image/*".toMediaTypeOrNull())?.let { it1 ->
                MultipartBody.Part.createFormData(
                    "front_pic",
                    file?.name,
                    it1
                )
            }

            val vAutKey = PrefKeys.getAuthKey()
                .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
            val kycType =
                "PAN Card".toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
            val kycNumber = binding.editTextPan.text.toString()
                .toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())

            var updateKYCPQR = UpdateKYCPRQ(
                vAutKey, kycType, kycNumber, imageBody, imageBody2
            )
            homeViewModel.submitKYC(updateKYCPQR)
        }
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
//                .cropSquare()                 //Crop image(Optional), Check Customization for more option
            .compress(1024)          //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    fun showSelectedFile(uri: Uri) {
        loadCornerImage(binding.imageViewPan, uri)
        profileImageUri = uri
        Logger.d("Crop URI =>" + uri)
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val pdfBytes = (contentResolver?.openInputStream(uri))?.readBytes()
            file = File(
                getExternalFilesDir(null),
                "kyc_${Calendar.getInstance().time}.png"
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
            bundle.putString("OPEN_FROM", "From_KYC")

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

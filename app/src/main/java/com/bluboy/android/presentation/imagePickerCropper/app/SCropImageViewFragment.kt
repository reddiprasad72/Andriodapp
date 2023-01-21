package com.bluboy.android.presentation.imagePickerCropper.app

import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.*
import com.bluboy.android.R
import com.bluboy.android.databinding.FragmentCropImageViewBinding
import com.bluboy.android.presentation.imagePickerCropper.domain.SCropImageViewContract
import com.bluboy.android.presentation.imagePickerCropper.presenter.SCropImageViewPresenter
import com.bluboy.android.presentation.loginsignup.SignupActivity
import com.bluboy.android.presentation.profile.EditProfileActivity
import com.bluboy.android.presentation.profile.KycAadhaarActivity
import com.bluboy.android.presentation.profile.KycPANActivity
import com.bluboy.android.presentation.utility.Logger


internal class SCropImageViewFragment :
    Fragment(),
    SCropImageViewContract.View,
//    SOptionsDialogBottomSheet.Listener,
    OnSetImageUriCompleteListener,
    OnCropImageCompleteListener {

    companion object {
        fun newInstance() = SCropImageViewFragment()
    }

    private lateinit var binding: FragmentCropImageViewBinding
    private val presenter = SCropImageViewPresenter()
    private var photoUri: Uri? = null
    private var openFrom = "From_Edit"
//    private var options: SOptionsDomain? = null
    private val openPicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.cropImageView.setImageUriAsync(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentCropImageViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        presenter.bind(this)
        presenter.onViewCreated()
        photoUri = arguments?.getString("URI_DATA")?.toUri()
        openFrom = arguments?.getString("OPEN_FROM").toString()
        Logger.d("URI -> " + photoUri.toString())
        binding.cropImageView.setImageUriAsync(photoUri)

        binding.cropImageView.let {
            it.setOnSetImageUriCompleteListener(this)
            it.setOnCropImageCompleteListener(this)
//            if (savedInstanceState == null) it.imageResource = R.drawable.bg_bs
        }

        binding.settings.setOnClickListener {
//            SOptionsDialogBottomSheet.show(childFragmentManager, options, this)
            binding.cropImageView.croppedImageAsync()
        }

        binding.closeButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.searchImage.setOnClickListener {
            openPicker.launch("image/*")
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.crop_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_action_crop -> {
                binding.cropImageView.croppedImageAsync()
                return true
            }
            R.id.main_action_rotate -> {
                binding.cropImageView.rotateImage(90)
                return true
            }
            R.id.main_action_flip_horizontally -> {
                binding.cropImageView.flipImageHorizontally()
                return true
            }
            R.id.main_action_flip_vertically -> {
                binding.cropImageView.flipImageVertically()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDetach() {
        super.onDetach()
        binding.cropImageView.setOnSetImageUriCompleteListener(null)
        binding.cropImageView.setOnCropImageCompleteListener(null)
    }

    override fun onSetImageUriComplete(view: CropImageView, uri: Uri, error: Exception?) {
        if (error != null) {
            Log.e("AIC", "Failed to load image by URI", error)
            Toast.makeText(activity, "Image load failed: " + error.message, Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onCropImageComplete(view: CropImageView, result: CropResult) {
        handleCropResult(result)
    }

    private fun handleCropResult(result: CropResult?) {
        if (result != null && result.error == null) {
            val imageBitmap =
                if (binding.cropImageView.cropShape == CropImageView.CropShape.OVAL)
                    result.bitmap?.let { CropImage.toOvalBitmap(it) }
                else result.bitmap
            context?.let { Log.v("File Path", result.getUriFilePath(it).toString()) }
            result.uriContent?.let {
                if (openFrom == "From_Edit")
                (activity as EditProfileActivity?)?.showSelectedFile(it)
                else if(openFrom == "From_Signup")
                    (activity as SignupActivity?)?.showSelectedFile(it)
                else if(openFrom == "From_Aadhar")
                    (activity as KycAadhaarActivity?)?.showSelectedFile(it)
                else if(openFrom == "From_AadharBack")
                    (activity as KycAadhaarActivity?)?.showSelectedFile(it,true)
                else
                    (activity as KycPANActivity?)?.showSelectedFile(it)
            }
            activity?.supportFragmentManager?.popBackStack()
//            SCropResultActivity.start(this, imageBitmap, result.uriContent, result.sampleSize)
        } else {
            Log.e("AIC", "Failed to crop image", result?.error)
            Toast
                .makeText(activity, "Crop failed: ${result?.error?.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun setOptions() {
        binding.cropImageView.cropRect = Rect(100, 300, 500, 1200)
//        onOptionsApplySelected(options)
    }
}

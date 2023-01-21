package com.bluboy.android.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluboy.android.presentation.utility.AppConstant
import com.bluboy.android.presentation.utility.loadImage
import com.bluboy.android.databinding.ActivityPhotoZoomBinding

class PhotoZoomActivity : AppCompatActivity() {

    private var url: String? = null
    private lateinit var binding: ActivityPhotoZoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityPhotoZoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url = intent.getStringExtra(AppConstant.URL_LINK)

        loadImage(binding.imageViewSingle, url)
        binding.imageViewClose.setOnClickListener {
            finish()
        }
    }
}
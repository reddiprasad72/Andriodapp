package com.bluboy.android.presentation.core

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bluboy.android.R
import com.bluboy.android.databinding.LayoutDialogProgressBinding


class CustomProgressDialog(context: Context) : Dialog(context, R.style.full_screen_dialog) {

    private lateinit var binding: LayoutDialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutDialogProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(context).asGif().load(R.raw.loader).into(binding.progress)
    }
}
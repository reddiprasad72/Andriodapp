package com.bluboy.android.presentation.imagePickerCropper.domain


internal interface SCropImageViewContract {
    fun interface View {
        fun setOptions()
    }

    interface Presenter {
        fun bind(view: View)
        fun unbind()
        fun onViewCreated()
    }
}

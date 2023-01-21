package com.bluboy.android.presentation.imagePickerCropper.presenter

import com.bluboy.android.presentation.imagePickerCropper.domain.SCropImageViewContract

internal class SCropImageViewPresenter : SCropImageViewContract.Presenter {

    private var view: SCropImageViewContract.View? = null

    override fun bind(view: SCropImageViewContract.View) {
        this.view = view
    }

    override fun unbind() {
        view = null
    }

    override fun onViewCreated() {
        view?.setOptions()
    }


}

package com.bluboy.android.presentation.utility.customBlurDialogFragment.holderdata;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bluboy.android.presentation.utility.customBlurDialogFragment.AsyncPolicy;
import com.bluboy.android.presentation.utility.customBlurDialogFragment.SmartAsyncPolicy;


public enum SmartAsyncPolicyHolder {
    INSTANCE;

    private AsyncPolicy smartAsyncPolicy;

    public void init(@NonNull Context context) {
        smartAsyncPolicy = new SmartAsyncPolicy(context, true);
    }

    public AsyncPolicy smartAsyncPolicy() {
        return smartAsyncPolicy;
    }
}

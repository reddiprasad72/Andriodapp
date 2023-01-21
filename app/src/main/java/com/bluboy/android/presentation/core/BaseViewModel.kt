package com.bluboy.android.presentation.core

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluboy.android.data.Either
import com.bluboy.android.data.models.User
import com.bluboy.android.domain.exceptions.MyException
import com.bluboy.android.presentation.utility.PrefKeys
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.bluboy.android.data.models.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    val exceptionLiveData: MutableLiveData<Exception> = MutableLiveData()
    val statusLiveData: MutableLiveData<Int> = MutableLiveData()

    var user: User? = null

    var job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    init {
        getUserProfile()
    }

    /**
     * For separate the success and exception from network call
     *
     * @param T - Generic type of your class
     * @param either - your either which have success or exception
     * @param successLiveData - your success livedata
     */
    fun <T> postValue(either: Either<MyException, T>, successLiveData: MutableLiveData<T>) {
        either.either({
            exceptionLiveData.postValue(it)
        }, {
            if (it is BaseResponse) {
                if (it.status == 4) { // Server Maintenance mode
                    statusLiveData.postValue(it.status)
                } else if (it.status == 5) {
                    statusLiveData.postValue(it.status)
                } else if (it.status == 6) {  // Force update for all screen
                    statusLiveData.postValue(it.status)
                } else {
                    statusLiveData.postValue(it.status)  // Debugging check
                    successLiveData.postValue(it)
                }
            } else {
//             Debugging check
                if (it is BaseResponse) {
                    statusLiveData.postValue(it.status)
                }

                successLiveData.postValue(it)
            }
        })
    }


    fun getUserProfile() {
        val userProfile = Prefs.getString(PrefKeys.UserProfile, "")
        if (userProfile.isNotEmpty()) {
            user = Gson().fromJson(userProfile, User::class.java)
        }
    }
}
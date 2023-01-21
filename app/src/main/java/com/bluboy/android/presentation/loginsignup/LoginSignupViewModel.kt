package com.bluboy.android.presentation.loginsignup


import androidx.lifecycle.MutableLiveData
import com.bluboy.android.data.contract.LoginSignupRepo
import com.bluboy.android.data.models.*
import com.bluboy.android.presentation.core.BaseViewModel
import kotlinx.coroutines.launch

class LoginSignupViewModel(private val loginSignupRepo: LoginSignupRepo) : BaseViewModel() {

    val loginUserRSLiveData: MutableLiveData<RegisterUserRS> = MutableLiveData()
    val completeProfileRSLiveData: MutableLiveData<RegisterUserRS> = MutableLiveData()
    val optVerificationRSLiveData: MutableLiveData<RegisterUserRS> = MutableLiveData()
    val resendOtpRSLiveData: MutableLiveData<BaseResponse> = MutableLiveData()
    val checkUserNameAvailableObserver: MutableLiveData<BaseResponse> = MutableLiveData()
    val checkRootDeviceObserver: MutableLiveData<BaseResponse> = MutableLiveData()



    fun login(loginPRQ: UserLoginPRQ) {
        launch {
            postValue(loginSignupRepo.login(loginPRQ), loginUserRSLiveData)
        }
    }

    fun completeProfile(completeProfilePRQ: CompleteProfilePRQ) {
        launch {
            postValue(loginSignupRepo.completeProfile(completeProfilePRQ), completeProfileRSLiveData)
        }
    }

    fun verifyOtp(verificationPRQ: String,device_token: String,device_name: String,device_unique_id: String,device_type: String) {
        launch {
            postValue(loginSignupRepo.verifyOtp(verificationPRQ,device_token,device_name,device_unique_id,device_type), optVerificationRSLiveData)
        }
    }

    fun resendOtp(phone: String) {
        launch {
            postValue(
                loginSignupRepo.resendOtp(phone),
                resendOtpRSLiveData
            )
        }
    }

    fun checkUserNameAvailabel(username: String) {
        launch { postValue(loginSignupRepo.checkUserNameAvailabel(username), checkUserNameAvailableObserver)
        }
    }

    fun getCheckRootDevice(ipAddress: String,isRoot: String) {
        launch { postValue(loginSignupRepo.getCheckRootDevice(ipAddress,isRoot), checkRootDeviceObserver)
        }
    }





}
package com.bluboy.android.presentation.splash

import androidx.lifecycle.MutableLiveData
import com.bluboy.android.data.contract.HomeRepo
import com.bluboy.android.data.models.BaseResponse
import com.bluboy.android.data.models.ForceUpdateRs
import com.bluboy.android.data.models.VarSettingRS
import com.bluboy.android.presentation.core.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel constructor(private val homeRepo: HomeRepo) : BaseViewModel() {
    val varSettingRS: MutableLiveData<VarSettingRS> = MutableLiveData()
    val forceUpdateResponse: MutableLiveData<ForceUpdateRs> = MutableLiveData()
    val checkAppHashKeyResponse: MutableLiveData<ForceUpdateRs> = MutableLiveData()
    val checkRootDeviceObserver: MutableLiveData<BaseResponse> = MutableLiveData()

    fun getSettingVar() {
        launch {
            postValue(homeRepo.getSettingVar(), varSettingRS)
        }
    }

    fun forceUpdateApp(version : String){
        launch {
            postValue(homeRepo.forceUpdateApp(version), forceUpdateResponse)

        }
    }

    fun checkAppHashKey(hashKey : String,bundleId: String){
        launch {
            postValue(homeRepo.checkAppHashKey(hashKey,bundleId), checkAppHashKeyResponse)

        }
    }



    fun getCheckRootDevice(ipAddress: String,isRoot: String) {
        launch { postValue(homeRepo.getCheckRootDevice(ipAddress,isRoot), checkRootDeviceObserver)
        }
    }


}
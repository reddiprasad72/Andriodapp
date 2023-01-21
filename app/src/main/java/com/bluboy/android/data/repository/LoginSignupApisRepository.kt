package com.bluboy.android.data.repository


import com.bluboy.android.BuildConfig
import com.bluboy.android.data.BaseRepository
import com.bluboy.android.data.Either
import com.bluboy.android.data.contract.LoginSignupRepo
import com.bluboy.android.data.models.*
import com.bluboy.android.domain.exceptions.MyException
import com.bluboy.android.domain.network.LoginSignupApiService
import com.bluboy.android.presentation.utility.AppConstant
import com.bluboy.android.presentation.utility.PrefKeys
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class LoginSignupApisRepository constructor(
    private val loginSignupApiService: LoginSignupApiService
) : LoginSignupRepo, BaseRepository() {

    override suspend fun login(userListPRQ: UserLoginPRQ): Either<MyException, RegisterUserRS> {
        return either {
            loginSignupApiService.login(
                userListPRQ.phone,
                userListPRQ.latitude,
                userListPRQ.longitude,
                userListPRQ.device_token,
                userListPRQ.device_type,
                userListPRQ.device_name,
                userListPRQ.device_unique_id,
                userListPRQ.app_version)

        }
    }

    override suspend fun completeProfile(completeProfilePRQ: CompleteProfilePRQ): Either<MyException, RegisterUserRS> {
        return either {
            loginSignupApiService.completeProfile(
                completeProfilePRQ.auth_key,completeProfilePRQ.name,completeProfilePRQ.username, completeProfilePRQ.email,
                completeProfilePRQ.latitude, completeProfilePRQ.longitude, completeProfilePRQ.referralCode,completeProfilePRQ.device_unique_id,
                completeProfilePRQ.vImage,completeProfilePRQ.accept_terms_conditions,completeProfilePRQ.state_id, BuildConfig.VERSION_NAME.toRequestBody(AppConstant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
            )
        }
    }


    override suspend fun verifyOtp(verificationPRQ: String,device_token: String,device_name: String,device_unique_id: String,device_type: String): Either<MyException, RegisterUserRS> {
        return either {
            loginSignupApiService.verifyOtp(
                PrefKeys.getAuthKey(),
                verificationPRQ
                ,device_token, device_type,device_name,
                device_unique_id,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun resendOtp(phone: String): Either<MyException, BaseResponse> {
        return either {
            loginSignupApiService.resendOtp(
                PrefKeys.getAuthKey(),
                phone,BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun checkUserNameAvailabel(
        username: String,
    ): Either<MyException, BaseResponse> {
        return either {
            loginSignupApiService.checkUserNameAvailabel(PrefKeys.getAuthKey(),username,BuildConfig.VERSION_NAME)
        }
    }

    override suspend fun getCheckRootDevice(ipAddress: String,isRoot: String): Either<MyException, BaseResponse> {
        return either {
            loginSignupApiService.getCheckRootDevice(PrefKeys.getAuthKey(),ipAddress,isRoot,BuildConfig.VERSION_NAME)
        }
    }
}
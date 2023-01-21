package com.bluboy.android.data.contract

import com.bluboy.android.data.Either
import com.bluboy.android.data.models.*
import com.bluboy.android.domain.exceptions.MyException


interface LoginSignupRepo {
    suspend fun login(userListPRQ: UserLoginPRQ): Either<MyException, RegisterUserRS>
    suspend fun completeProfile(completeProfilePRQ: CompleteProfilePRQ): Either<MyException, RegisterUserRS>
    suspend fun verifyOtp(verificationPRQ: String,device_token: String,device_name: String,device_unique_id: String,device_type: String): Either<MyException, RegisterUserRS>
    suspend fun resendOtp(phone: String): Either<MyException, BaseResponse>
    suspend fun checkUserNameAvailabel(username: String): Either<MyException, BaseResponse>
    suspend fun getCheckRootDevice(ipAddress: String,isRoot: String): Either<MyException, BaseResponse>

}
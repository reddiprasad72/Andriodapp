package com.bluboy.android.domain.network

import com.bluboy.android.data.models.BaseResponse
import com.bluboy.android.data.models.RegisterUserRS
import com.bluboy.android.presentation.utility.ApiConstant
import com.bluboy.android.presentation.utility.AppConstant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface LoginSignupApiService {

    @FormUrlEncoded
    @POST(ApiConstant.ApiLogin)
    suspend fun login(
        @Field(AppConstant.phone) phone: String,
        @Field(AppConstant.latitude) latitude: String,
        @Field(AppConstant.longitude) longitude: String,
        @Field(AppConstant.device_token) deviceToken: String,
        @Field(AppConstant.device_type) deviceType: String,
        @Field(AppConstant.device_name) deviceName: String,
        @Field(AppConstant.device_unique_id) deviceUniqueId: String,
        @Field(AppConstant.app_version) appVersion: String
    ): RegisterUserRS


    @Multipart
    @POST(ApiConstant.ApiCompleteProfile)
    suspend fun completeProfile(
        @Part(AppConstant.authKey) authKey: RequestBody,
        @Part(AppConstant.name) name: RequestBody,
        @Part(AppConstant.username) username: RequestBody,
        @Part(AppConstant.email) email: RequestBody,
        @Part(AppConstant.latitude) latitude: RequestBody,
        @Part(AppConstant.longitude) longitude: RequestBody,
        @Part(AppConstant.referral_code) referralCode: RequestBody,
        @Part(AppConstant.device_unique_id) deviceUniqueId: RequestBody,
        @Part profile_pic: MultipartBody.Part?,
        @Part(AppConstant.accept_terms_conditions) accept_terms_conditions: RequestBody,
        @Part(AppConstant.state_id) state_id: RequestBody,
        @Part(AppConstant.PARAM_APP_VERSION) appVersion: RequestBody
    ): RegisterUserRS

    @FormUrlEncoded
    @POST(ApiConstant.ApiOtpVerification)
    suspend fun verifyOtp(
        @Field(AppConstant.authKey) vAuthKey: String,
        @Field(AppConstant.otp) otp: String,
        @Field(AppConstant.device_token) deviceToken: String,
        @Field(AppConstant.device_type) deviceType: String,
        @Field(AppConstant.device_name) deviceName: String,
        @Field(AppConstant.device_unique_id) deviceUniqueId: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
//        @Field(AppConstant.language) language: String = PrefKeys.getLanguageSelectedCapital()
    ): RegisterUserRS


    @FormUrlEncoded
    @POST(ApiConstant.ApiResendOtp)
    suspend fun resendOtp(
        @Field(AppConstant.authKey) vAuthKey: String,
        @Field(AppConstant.phone) phone: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiCheckUserNameAvailabel)
    suspend fun checkUserNameAvailabel(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.user_name) userName: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiCheckRootDevice)
    suspend fun getCheckRootDevice(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.ip_address) ipAddress: String,
        @Field(AppConstant.is_root) isRoot: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse
}
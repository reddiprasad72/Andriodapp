package com.bluboy.android.data.models

import android.os.Parcelable
import com.bluboy.android.presentation.utility.AppConstant
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import okhttp3.MultipartBody
import okhttp3.RequestBody


@Parcelize
data class RegisterUserPRQ(
    @SerializedName(AppConstant.language) var language: String? = "",
    @SerializedName(AppConstant.user_type) var user_type: String? = "",
    @SerializedName(AppConstant.fullname) var fullname: String? = "",
    @SerializedName(AppConstant.phone) var phone: String? = "",
    @SerializedName(AppConstant.email) var email: String? = "",
    @SerializedName(AppConstant.password) var password: String? = "",
    @SerializedName(AppConstant.timezone_detail) var timezone_detail: String? = "",
    @SerializedName(AppConstant.app_version) var app_version: String? = "",
    @SerializedName(AppConstant.device_type) var device_type: String? = "",
    @SerializedName(AppConstant.device_token) var device_token: String? = "",
    @SerializedName(AppConstant.device_name) var device_name: String? = "",
    @SerializedName(AppConstant.latitude) var latitude: String? = "",
    @SerializedName(AppConstant.longitude) var longitude: String? = "",
    @SerializedName(AppConstant.profile_pic) var profile_pic: String? = ""
) : Parcelable



data class UserLoginPRQ(
    var phone: String ="",
    var latitude: String = "",
    var longitude: String = "",
    var device_token: String = "",
    var device_type: String = "",
    var device_name: String = "",
    var device_unique_id: String = "",
    var app_version: String = ""
)

data class CompleteProfilePRQ(
    var auth_key: RequestBody,
    var name: RequestBody,
    var username: RequestBody,
    var email: RequestBody,
    var latitude: RequestBody,
    var longitude: RequestBody,
    var referralCode: RequestBody,
    var device_unique_id: RequestBody,
    var vImage: MultipartBody.Part? = null,
    var accept_terms_conditions: RequestBody,
    var state_id: RequestBody
)

data class SubmitKYC(
    var value: RequestBody,
    var type: RequestBody,
    var vImage: MultipartBody.Part? = null,
)

data class UpdateKYCPRQ(
    var auth_key: RequestBody,
    var kyc_type: RequestBody,
    var kyc_number: RequestBody,
    var kyc_image: MultipartBody.Part? = null,
    var kyc_image2: MultipartBody.Part? = null
)

@Parcelize
data class UpdateProfilePRQ(
    @SerializedName(AppConstant.language) var language: String? = "",
    @SerializedName(AppConstant.fullname) var fullname: String? = "",
    @SerializedName(AppConstant.profile_pic) var profile_pic: String? = ""
) : Parcelable

data class RegisterUserRS(
    @SerializedName("data")
    var user: User? = null
) : BaseResponse()

@Parcelize
data class User(
    @SerializedName("app_version")
    val appVersion: String,
    @SerializedName("auth_key")
    val authKey: String,
    @SerializedName("device_name")
    val deviceName: String,
    @SerializedName("device_token")
    val deviceToken: String,
    @SerializedName("device_type")
    val deviceType: String,
    @SerializedName("email")
    var email: String = "",
    @SerializedName("is_profile_completed")
    val isProfileCompleted: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("phone_verification_status")
    var phoneVerificationStatus: String,
    @SerializedName("profile_pic")
    val profilePic: String,
    @SerializedName("push_notification_status")
    var pushNotificationStatus: String,
    @SerializedName("referral_code")
    val referralCode: String,
    @SerializedName("socket_id")
    val socketId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_user_tournament")
    val totalUserTournament: String,
    @SerializedName("total_wallet_amount")
    val totalWalletAmount: String,
    @SerializedName("user_bonus_balance")
    val userBonusBalance: String,
    @SerializedName("user_deposit_balance")
    val userDepositBalance: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("user_wallet_points")
    val userWalletPoints: String,
    @SerializedName("user_winning_amount_balance")
    val userWinningAmountBalance: String,
    @SerializedName("total_game_count")
    val totalGameCount: Int,
    @SerializedName("won_game_count")
    val wonGameCount: Int,
    @SerializedName("lost_game_count")
    val lostGameCount: Int,
    @SerializedName("user_games")
    val userGames: List<UserGame>,
    @SerializedName("state_id")
    val stateId: String,
    @SerializedName("kyc_status")
    val kycStatus: String,
    @SerializedName("aadhar_card_kyc_status")
    val kycAadharStatus: String = "",
    @SerializedName("pan_card_kyc_status")
    val kycPanStatus: String = ""
    ) : Parcelable

@Parcelize
data class UserGame(
    @SerializedName("game_banner")
    val gameBanner: String,
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("game_image")
    val gameImage: String,
    @SerializedName("game_name")
    val gameName: String
) : Parcelable


data class OtpVerificationPRQ(
    @SerializedName(AppConstant.authKey) var authKey: String,
    @SerializedName(AppConstant.iPhoneVerificationCode) var iPhoneVerificationCode: String,
)

data class ResendOtpPRQ(
    @SerializedName(AppConstant.authKey) var authKey: String,
)

data class AutoLoginPRQ(
    @SerializedName(AppConstant.authKey) var vAuthKey: String,
    @SerializedName(AppConstant.app_version) var appVersion: String,
    @SerializedName(AppConstant.device_token) var vDeviceToken: String,
    @SerializedName(AppConstant.vTimezone) var vTimezone: String,
    @SerializedName(AppConstant.vDeviceUniqueId) var vDeviceUniqueId: String

)

data class ForgotPasswordPRQ(
    @SerializedName(AppConstant.email) var email: String? = ""
)

data class LoginPRQ(
    @SerializedName(AppConstant.email) var email: String? = "",
    @SerializedName(AppConstant.password) var password: String? = "",
    @SerializedName(AppConstant.app_version) var app_version: String? = "",
    @SerializedName(AppConstant.device_type) var device_type: String? = "",
    @SerializedName(AppConstant.device_token) var device_token: String? = "",
    @SerializedName(AppConstant.device_name) var device_name: String? = ""
)

data class SendOtpPRQ(
    @SerializedName("vMobile") var vMobile: String? = ""
)

data class SendOtpRS(
    @SerializedName("data")
    var sendOtp: SendOtp? = null
) : BaseResponse()

@Parcelize
data class SendOtp(
    @SerializedName("otp")
    var otp: String? = ""
) : Parcelable

data class ChangePasswordPRQ(
    @SerializedName(AppConstant.authKey) var authKey: String,
    @SerializedName(AppConstant.oldPassword) var oldPassword: String,
    @SerializedName(AppConstant.newPassword) var newPassword: String
)

data class ResetPasswordPRQ(
    @SerializedName(AppConstant.email) var email: String,
    @SerializedName(AppConstant.password) var password: String,
    @SerializedName(AppConstant.otp) var otp: String
)

data class ResetPasswordRS(
    @SerializedName("data") var user: User? = null
) : BaseResponse()
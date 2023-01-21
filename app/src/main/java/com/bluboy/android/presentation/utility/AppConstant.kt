package com.bluboy.android.presentation.utility

/**
 * Created by Parth Patibandha on 01,November,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

class AppConstant {

    companion object {
        val TIMEOUT = 300L

        const val INTENT_SETTINGS = 999

        const val GET_MY_OFFER_DATA = 579
        const val GET_PAYTM = 580

        const val pageSizeData = 30


        const val PageStart = 1
        const val page = "page"
        const val page_size = "page_size"
        const val data = "data"
        const val perPage = "per_page"
        const val total = "total"
        const val totalPages = "total_pages"

        const val DB_NAME = "capermint_db"
        const val TABLE_NAME_USER = "users"

        const val id = "id"
        const val avatar = "avatar"
        const val firstName = "first_name"
        const val lastName = "last_name"

        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_ID = "extra_id"
        const val OPEN_FROM_PUSH = "openFromPush"

        const val authKey = "auth_key"

        const val Yes = "Y"
        const val No = "N"
        const val Space = " "
        const val DeviceTypeAndroid = "A"

        const val user_id = "user_id"
        const val user_name = "user_name"
        const val language = "language"
        const val user_type = "user_type"
        const val fullname = "fullname"
        const val username = "username"
        const val name = "name"
        const val phone = "phone"
        const val email = "email"
        const val game_id = "game_id"
        const val PARAM_APP_VERSION = "app_version"
        const val spinId = "spin_id"

        const val PARAM_NAME = "name"
        const val PARAM_NICKNAME = "nick_name"
        const val PARAM_BANK_NAME = "bank_name"
        const val PARAM_ACC_NUMBER = "ac_number"
        const val PARAM_IFSC = "ifsc"
        const val PARAM_ADDRESS = "address"
        const val PARAM_CITY = "city"
        const val PARAM_STATE = "state"
        const val PARAM_PINCODE = "pincode"
        const val PARAM_VPA = "vpa"
        const val PARAM_BENEFICIARY_ID = "beneficiary_id"
        const val PARAM_COUPON_CODE = "coupon_code"
        const val PARAM_TRANSFER_MODE = "transfer_mode"
        const val PARAM_IS_DEFAULT = "is_default"

        const val message = "message"
        const val ip_address = "ip_address"
        const val is_root = "is_root"
        const val version = "version"
        const val hash_key = "hash_key"
        const val state = "state"
        const val bundleid = "bundleid"
        const val password = "password"
        const val oldPassword = "old_password"
        const val newPassword = "new_password"
        const val login_type = "login_type"
        const val timezone_detail = "timezone_detail"
        const val app_version = "app_version"
        const val device_type = "device_type"
        const val device_token = "device_token"
        const val device_name = "device_name"
        const val device_unique_id = "device_unique_id"
        const val latitude = "latitude"
        const val longitude = "longitude"
        const val profile_pic = "profile_pic"
        const val social_id = "social_id"
        const val amount = "amount"
        const val couponCode = "coupon_code"
        const val orderId = "orderId"
        const val order_Id = "order_id"
        const val orderAmount = "orderAmount"
        const val orderCurrency = "orderCurrency"
        const val cfResponse = "cf_response"
        const val enable_push_notifications = "enable_push_notifications"
        const val email_verification_status = "email_verification_status"
        const val phone_verification_status = "phone_verification_status"
        const val is_online = "is_online"
        const val is_active = "is_active"
        const val socket_id = "socket_id"
        const val vTimezone = "vTimezone"
        const val referral_code = "referral_code"
        const val accept_terms_conditions = "accept_terms_conditions"
        const val state_id = "state_id"
        const val otp = "otp"
        const val upi_id = "upi_id"
        const val iPhoneVerificationCode = "iPhoneVerificationCode"
        const val search_term = "search_term"
        const val page_code = "page_code"
        const val cmsType = "cmsType"
        const val aboutUs = "ABOUT_US"
        const val termsAndConditions = "TERMS_CONDITION"
        const val privacyPolicy = "PRIVACY_POLICY"
        const val vDeviceUniqueId = "vDeviceUniqueId"
        const val feedback = "feedback"
        const val tournament_id = "tournament_id"
        const val type = "type"
        const val roomCode = "room_code"
        const val kyc_status = "kyc_status"
        const val kyc_type = "kyc_type"
        const val document_number = "document_number"
        const val document_type = "document_type"
        const val kyc_number = "kyc_number"
        const val kyc_image = "kyc_image"


        const val MULTIPART_FORM_DATA = "multipart/form-data"

        const val ABOUT_US = "ABOUT_US"
        const val PRIVACY_POLICY = "PRIVACY_POLICY"
        const val TERMS_AND_CONDITION = "TERMS_AND_CONDITION"

        const val BENEFICIARY_TYPE = "beneficiary_type"
        const val BENEFICIARY_ADD = "beneficiary_add"
        const val BENEFICIARY_EDIT = "beneficiary_edit"
        const val TRANSFER_MODE = "transfer_Mode"
        const val TRANSFER_AMOUNT = "amountt"
        const val TRANSFER_PROMOCODE = "promocode"
        const val OPEN_FROM_SIGNUP = "open_from_signup"
        const val OPEN_ADDMONEY = "open_addmoney_from_profile"
        const val OPEN_WALLET_FROM_SPIN = "open_from_spin"
        const val OPEN_WALLET_FROM_PROFILE = "open_wallet_from_profile"
        const val OPEN_FROM_SPLASH = "open_from_splash"

        const val checkWithdrawTransaction = "checkWithdrawTransaction"
        const val CHARGE = "CHARGE"

        const val GAME = "game_"
        const val GAME_NAME = "game_NAME"
        const val GAME_ID = "game_id_"
        const val BATTLE = "battle_"
        const val TOURNAMENT_ID = "tournament_id_"
        const val CARD = "card"
        const val UPI = "upi"

        const val SHARED_PREF = "myPrefs"

        const val authId = "authId"
        const val patronUserId = "patronUserId"
        const val WEB_AADHAR_URL = "web_aadhaar_url"
        const val WEB_REQUEST_ID = "web_request_id"
        const val FROM_PROFILE ="fromProfile"

        const val GAME_MODE = "game_mode"
        const val URL_LINK = "url_link"
        const val VIDEO_URL = "video_url"
        const val TEXT_HOW_TO_PLAY = "text_how_to_play"

        const val isDownloadComplete = "isDownloadComplete"

        const val GAME_ORIENTATION_PORTRAIT = "Portrait"
        const val GAME_ORIENTATION_LANDSCAPE = "Landscape"




    }
}
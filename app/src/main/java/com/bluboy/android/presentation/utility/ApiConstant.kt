package com.bluboy.android.presentation.utility

/**
 * Created by Parth Patibandha on 01,November,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

class ApiConstant {
    companion object {
        const val ApiGetUserList = "users"

        const val ApiBeforeAuth = "api/v1"
        const val ApiAfterAuth = "api/v1"
        const val GAME_API = "api/v1"

        //Before Auth
        const val ApiLogin = "$ApiBeforeAuth/login"
        const val ApiFaqsList = "$ApiBeforeAuth/get_faqs"

        const val ApiForgotPassword = "$ApiBeforeAuth/forgot_password"
        const val ApiResetPassword = "$ApiBeforeAuth/reset_password"
        const val ApiGetPages = "$ApiBeforeAuth/get_cms_page"
        const val ApiChangePassword = "$ApiAfterAuth/change_password"

        const val ApiForceUpdate = "$ApiBeforeAuth/check_version_update"
        const val ApiSettingVar = "$ApiBeforeAuth/get_setting_var"
        const val ApiCheckAppHAshKey = "$ApiBeforeAuth/check_app_hash_key"
        const val ApiCheckStateStatus = "$ApiBeforeAuth/check_state_status"
        const val ApiContactUs = "$ApiBeforeAuth/contact_request"


        //After Auth
        const val ApiOtpVerification = "$ApiAfterAuth/verify_otp"
        const val ApiResendOtp = "$ApiAfterAuth/resend_otp"
        const val ApiCompleteProfile = "$ApiAfterAuth/complete_profile"
        const val ApiPushNotification = "$ApiAfterAuth/update_push_notification_status"

        const val ApiPromoCodeList = "$ApiAfterAuth/get_promo_codes"
        const val ApiLogout = "$ApiAfterAuth/logout"
        const val ApiGetProfile = "$ApiAfterAuth/get_profile"
        const val ApiGetGames = "$ApiAfterAuth/get_games"
        const val ApiGetFeaturedGame = "$ApiAfterAuth/get_featured_games"
        const val ApiNotificationList = "$ApiAfterAuth/user_notifications"
        const val ApiGetTournaments = "$ApiAfterAuth/get_tournaments"
        const val ApiGetBattles = "$ApiAfterAuth/get_battles"
        const val ApiLeaderboard = "$ApiAfterAuth/leader_borad"
        const val ApiBattleHistory = "$ApiAfterAuth/get_users_tournaments"
        const val ApiAllGameHistory = "$ApiAfterAuth/get_users_tournaments_history"
        const val ApiApplyPromocode = "$ApiAfterAuth/apply_promo_code"
        const val ApiHowToPlay = "$ApiAfterAuth/game_how_to_play"
        const val ApiWalletTransaction = "$ApiAfterAuth/user_wallet_transaction"
        const val ApiTournamentBeforeJoinCheck = "$ApiAfterAuth/tournament_join_check"
        const val ApiTournamentPriceBreakDown = "$ApiAfterAuth/join_tournament_price_breakdown"
        const val ApiGameLeaderBoard = "$ApiAfterAuth/game_leaderborad"
        const val ApiGenerateToken = "$ApiAfterAuth/generate_payment_token"
        const val ApiGenerateHyptoToken = "$ApiAfterAuth/generate_hypto_payment_token"
        const val ApiGeneratePaytmToken = "$ApiAfterAuth/generate_paytm_checksum"
        const val ApiAddMoneyToWallet = "$ApiAfterAuth/add_money_to_wallet"
        const val ApiBeneficiaryAdd = "$ApiAfterAuth/add_user_beneficiary"
        const val ApiBeneficiaryList = "$ApiAfterAuth/get_user_beneficiary"
        const val ApiWithdraw = "$ApiAfterAuth/withdraw"
        const val ApiGetUserRoom = "$ApiAfterAuth/get_user_room_id"
        const val ApiUserLeaveGame = "$GAME_API/user_leave_game"
        const val ApiBeneficiaryDelete = "$ApiAfterAuth/remove_user_beneficiary"
        const val ApiCheckUserNameAvailabel = "$ApiAfterAuth/check_username_available"
        const val ApiReferralHistory = "$ApiAfterAuth/refer_history"
        const val ApiBanner = "$ApiAfterAuth/get_banners"
        const val ApiTrendingGames = "$ApiAfterAuth/get_trending_games"
        const val ApiSpinList = "$ApiAfterAuth/spins"
        const val ApiSpinWin = "$ApiAfterAuth/user_spin"

        const val ApiGameCategory = "$ApiAfterAuth/game_category"

        const val ApiEndTournament = "$ApiAfterAuth/user_end_tournament"
        const val ApiCheckRootDevice = "$ApiAfterAuth/check_is_root_device"
        const val ApiCheckWithdrawalTransaction = "$ApiAfterAuth/check_withdrawal_transaction"
        const val ApiPaymentGatewayIncidents = "$ApiAfterAuth/payment_gateway_incidents"
        const val ApiUpdateUserState = "$ApiAfterAuth/update_user_state"
        const val ApiKycUpdate = "$ApiAfterAuth/kyc_update"

        const val addKYCDetails = "$ApiAfterAuth/add-update-kyc"
        const val getKYCDetails = "$ApiAfterAuth/get-kyc-details"
    }
}
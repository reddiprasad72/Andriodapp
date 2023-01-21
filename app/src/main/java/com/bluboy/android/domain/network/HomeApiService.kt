package com.bluboy.android.domain.network

import com.bluboy.android.data.models.*
import com.bluboy.android.presentation.utility.ApiConstant
import com.bluboy.android.presentation.utility.AppConstant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface HomeApiService {

    @FormUrlEncoded
    @POST(ApiConstant.ApiLogout)
    suspend fun performLogout(
        @Field(AppConstant.authKey) vAuthKey: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiGetPages)
    suspend fun getCMSPages(
        @Field(AppConstant.page_code) page_code: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
//        @Field(AppConstant.language) language: String = PrefKeys.getLanguageSelectedCapital()
    ): CMSPageRS

    @FormUrlEncoded
    @POST(ApiConstant.ApiFaqsList)
    suspend fun getFaqs(
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
//        @Field(AppConstant.language) language: String = PrefKeys.getLanguageSelectedCapital()
    ): FAQDataListRS

    @FormUrlEncoded
    @POST(ApiConstant.ApiPushNotification)
    suspend fun getPushNotification(
        @Field(AppConstant.authKey) vAuthKey: String,
        @Field(AppConstant.enable_push_notifications) status: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiContactUs)
    suspend fun performContactUs(
        @Field(AppConstant.authKey) vAuthKey: String? = "",
        @Field(AppConstant.email) contact_email: String,
        @Field(AppConstant.feedback) feedback: String,
        @Field(AppConstant.app_version) app_version: String,
        @Field(AppConstant.device_type) device_type: String,
        @Field(AppConstant.device_name) device_name: String
    ): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiContactUs)
    suspend fun getContactUs(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.email) email: String,
        @Field(AppConstant.message) message: String,
        @Field(AppConstant.ip_address) ipAddress: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiPromoCodeList)
    suspend fun getPromocodeList(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): PromocodeRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiGetProfile)
    suspend fun getProfile(
        @Field(AppConstant.authKey) vAuthKey: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): RegisterUserRS

    @FormUrlEncoded
    @POST(ApiConstant.getKYCDetails)
    suspend fun getKYCDetails(
        @Field(AppConstant.authKey) vAuthKey: String,
    ): RegisterUserRS

    @FormUrlEncoded
    @POST(ApiConstant.ApiGetGames)
    suspend fun getGames(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): GamesListResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiGetFeaturedGame)
    suspend fun getFeaturedGames(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): GamesListResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiNotificationList)
    suspend fun getNotificationList(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): NotificationRs

    //
//    @FormUrlEncoded
//    @POST(ApiConstant.ApiGetTournaments)
//    suspend fun getTournaments(
//        @Field(AppConstant.authKey) authKey: String,
//        @Field(AppConstant.game_id) gameId: Int,
//        @Field(AppConstant.page) page: Int,
//        @Field(AppConstant.page_size) pageSize: Int
//    ): TournamentsRs
//
    @FormUrlEncoded
    @POST(ApiConstant.ApiGetBattles)
    suspend fun getBattles(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.game_id) gameId: Int,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BattlesRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiLeaderboard)
    suspend fun getLeaderboard(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.type) type: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): LeaderboardRs


    @FormUrlEncoded
    @POST(ApiConstant.getKYCDetails)
    suspend fun getSubmittedKYC(@Field(AppConstant.authKey) authKey: String): SubmittedKYC

    @FormUrlEncoded
    @POST(ApiConstant.ApiGameLeaderBoard)
    suspend fun getGameLeaderboard(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.game_id) gameId: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): LeaderboardRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiBattleHistory)
    suspend fun getBattleHistory(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.game_id) gameID: String,
        @Field(AppConstant.page) pageCode: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BattaleHistoryRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiAllGameHistory)
    suspend fun getAllGameHistory(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.page) pageCode: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BattaleHistoryRs


    @FormUrlEncoded
    @POST(ApiConstant.ApiApplyPromocode)
    suspend fun getApplyPromocode(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.amount) amount: String,
        @Field(AppConstant.couponCode) couponCode: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): ApplyPromoCodeRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiHowToPlay)
    suspend fun getHowToPlay(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): HowToPlayRs


    @FormUrlEncoded
    @POST(ApiConstant.ApiTournamentBeforeJoinCheck)
    suspend fun getTournamentJoinCheck(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.tournament_id) tournamentId: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): TournamentRs


    @FormUrlEncoded
    @POST(ApiConstant.ApiTournamentPriceBreakDown)
    suspend fun getTournamentPriceBreakDown(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.tournament_id) tournamentId: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): TournamentPriceBrakDownRs


    @FormUrlEncoded
    @POST(ApiConstant.ApiEndTournament)
    suspend fun getEndTournament(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.tournament_id) tournamentId: String,
        @Field(AppConstant.roomCode) roomCode: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
//                                 @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): RegisterUserRS

    @FormUrlEncoded
    @POST(ApiConstant.ApiCheckUserNameAvailabel)
    suspend fun checkUserNameAvailabel(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.user_name) userName: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse

    @GET(ApiConstant.ApiSettingVar)
    suspend fun getSettingVar(): VarSettingRS

    @FormUrlEncoded
    @POST(ApiConstant.ApiForceUpdate)
    suspend fun forceUpdateApp(@Field(AppConstant.app_version) version: String,
                               @Field(AppConstant.device_type) deviceType: String): ForceUpdateRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiCheckAppHAshKey)
    suspend fun checkAppHashKey(
        @Field(AppConstant.hash_key) hash_key: String,
        @Field(AppConstant.bundleid) bundleId: String
    ): ForceUpdateRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiCheckStateStatus)
    suspend fun checkStateStatus(@Field(AppConstant.state) state: String): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiReferralHistory)
    suspend fun ApiReferralHistory(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): ReferralHistoryRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiBanner)
    suspend fun ApiBanner(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BannerRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiTrendingGames)
    suspend fun ApiTrendingGames(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): TrendingGamesRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiGenerateToken)
    suspend fun getGenerateToken(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.orderId) pageCode: String,
        @Field(AppConstant.orderAmount) orderAmount: String,
        @Field(AppConstant.orderCurrency) orderCurrency: String,
        @Field(AppConstant.couponCode) couponCode: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): PaymentTokenRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiGenerateHyptoToken)
    suspend fun generateHyptoToken(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.orderId) pageCode: String,
        @Field(AppConstant.orderAmount) orderAmount: String,
        @Field(AppConstant.orderCurrency) orderCurrency: String,
        @Field(AppConstant.couponCode) couponCode: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): HyptoTokenRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiGeneratePaytmToken)
    suspend fun generatePaytmToken(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.orderId) pageCode: String,
        @Field(AppConstant.orderAmount) orderAmount: String,
        @Field(AppConstant.couponCode) couponCode: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
//                                   @Field(AppConstant.PARAM_CALL_BACK_URL) callBackUrl: String
    ): PaytmTokenRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiAddMoneyToWallet)
    suspend fun addMoneyToWallet(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.order_Id) orderId: String,
        @Field(AppConstant.amount) amount: String,
        @Field(AppConstant.cfResponse) cfResponse: String,
        @Field(AppConstant.upi_id) upi_id: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiWalletTransaction)
    suspend fun walletTransaction(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.type) type: String,
        @Field(AppConstant.page) page: Int,
        @Field(AppConstant.page_size) pageSize: Int,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): WalletHistoryRs


    @FormUrlEncoded
    @POST(ApiConstant.ApiBeneficiaryAdd)
    suspend fun getBeneficiaryAdd(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.PARAM_NAME) name: String,
        @Field(AppConstant.PARAM_NICKNAME) nickName: String,
        @Field(AppConstant.email) email: String,
        @Field(AppConstant.phone) phone: String,
        @Field(AppConstant.PARAM_BANK_NAME) bank_name: String,
        @Field(AppConstant.PARAM_ACC_NUMBER) acc_number: String,
        @Field(AppConstant.PARAM_IFSC) IFSC: String,
        @Field(AppConstant.PARAM_ADDRESS) address: String,
        @Field(AppConstant.PARAM_CITY) city: String,
        @Field(AppConstant.PARAM_STATE) state: String,
        @Field(AppConstant.PARAM_PINCODE) pincode: String,
        @Field(AppConstant.PARAM_VPA) vpa: String,
        @Field(AppConstant.PARAM_TRANSFER_MODE) transferMode: String,
        @Field(AppConstant.PARAM_IS_DEFAULT) isDefault: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): WithDrawRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiBeneficiaryList)
    suspend fun getBeneficiaryList(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.PARAM_TRANSFER_MODE) transferMode: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String

    ): BeneficiaryRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiBeneficiaryDelete)
    suspend fun getBeneficiaryDelete(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.PARAM_BENEFICIARY_ID) bebeficiaryId: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
//                                     @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse


    @FormUrlEncoded
    @POST(ApiConstant.ApiWithdraw)
    suspend fun withdraw(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.amount) amount: String,
        @Field(AppConstant.PARAM_BENEFICIARY_ID) name: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): WithDrawRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiSpinList)
    suspend fun getSpinList(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): SpinListRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiSpinWin)
    suspend fun getSpinWin(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.spinId) spinId: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): SpinListRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiGameCategory)
    suspend fun getGameCategory(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): GameCategoryResponse


    @FormUrlEncoded
    @POST(ApiConstant.ApiCheckRootDevice)
    suspend fun getCheckRootDevice(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.ip_address) ipAddress: String,
        @Field(AppConstant.is_root) isRoot: String,
        @Field(AppConstant.PARAM_APP_VERSION) appVersion: String
    ): BaseResponse

    @FormUrlEncoded
    @POST(ApiConstant.ApiCheckWithdrawalTransaction)
    suspend fun getCheckWithdrawalTransaction(@Field(AppConstant.authKey) authKey: String): CheckWithdrawTransactionRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiPaymentGatewayIncidents)
    suspend fun getPaymentGatewayIncidents(@Field(AppConstant.authKey) authKey: String): SuccessRateIncidentRs

    @FormUrlEncoded
    @POST(ApiConstant.ApiUpdateUserState)
    suspend fun getUpdateUserState(
        @Field(AppConstant.authKey) authKey: String,
        @Field(AppConstant.state) state: String
    ): BaseResponse

    @Multipart
    @POST(ApiConstant.addKYCDetails)
    suspend fun submitKYC(
        @Part(AppConstant.authKey) authKey: RequestBody,
        @Part(AppConstant.document_type) document_type: RequestBody,
        @Part(AppConstant.document_number) kyc_number: RequestBody,
        @Part front_pic: MultipartBody.Part?,
        @Part back_pic: MultipartBody.Part?,
    ): BaseResponse


}
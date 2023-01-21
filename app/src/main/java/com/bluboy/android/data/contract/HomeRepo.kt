package com.bluboy.android.data.contract

import com.bluboy.android.data.Either
import com.bluboy.android.data.models.*
import com.bluboy.android.domain.exceptions.MyException

interface HomeRepo {
    suspend fun getCMSPageData(cmsPagePRQ: CMSPagePRQ): Either<MyException, CMSPageRS>
    suspend fun getGamesList(page: Int): Either<MyException, GamesListResponse>
    suspend fun getFeaturedGames(page: Int): Either<MyException, GamesListResponse>
    suspend fun getBattles(gameId: Int, page: Int): Either<MyException, BattlesRs>
    suspend fun getProfile(): Either<MyException, RegisterUserRS>
    suspend fun getPromocodeList(): Either<MyException, PromocodeRs>
    suspend fun getTournamentJoinCheck(tournamentId: String): Either<MyException, TournamentRs>
    suspend fun getTournamentPriceBreakDown(tournamentId: String): Either<MyException, TournamentPriceBrakDownRs>
    suspend fun performLogout(logoutPRQ: LogoutPRQ): Either<MyException, BaseResponse>
    suspend fun getBattleHistory(gameId: String, page: Int): Either<MyException, BattaleHistoryRs>
    suspend fun getAllGameHistory(page: Int): Either<MyException, BattaleHistoryRs>
    suspend fun getGameLeaderboard(gameId: String, page: Int): Either<MyException, LeaderboardRs>
    suspend fun getFaqs(page: Int): Either<MyException, FAQDataListRS>
    suspend fun getContactUs(
        email: String,
        message: String,
        ipAddress: String
    ): Either<MyException, BaseResponse>

    suspend fun getPushNotification(status: String): Either<MyException, BaseResponse>
    suspend fun getHowToPlay(page: Int): Either<MyException, HowToPlayRs>
    suspend fun getApplyPromocode(
        amount: String,
        couponCode: String
    ): Either<MyException, ApplyPromoCodeRs>

    suspend fun getNotificationList(page: Int): Either<MyException, NotificationRs>
    suspend fun getLeaderboard(type: String, page: Int): Either<MyException, LeaderboardRs>
    suspend fun checkUserNameAvailabel(username: String): Either<MyException, BaseResponse>
    suspend fun getEndTournament(
        authKey: String,
        tournamentId: String,
        roomCode: String
    ): Either<MyException, BaseResponse>

    suspend fun getSettingVar(): Either<MyException, VarSettingRS>
    suspend fun forceUpdateApp(version: String): Either<MyException, ForceUpdateRs>
    suspend fun checkAppHashKey(
        hashKey: String,
        bundleId: String
    ): Either<MyException, ForceUpdateRs>

    suspend fun checkStateStatus(state: String): Either<MyException, BaseResponse>
    suspend fun ApiReferralHistory(page: Int): Either<MyException, ReferralHistoryRs>
    suspend fun ApiBanner(page: Int): Either<MyException, BannerRs>
    suspend fun ApiTrendingGames(page: Int): Either<MyException, TrendingGamesRs>
    suspend fun getPaymentToken(
        orderId: String,
        orderAmount: String,
        orderCurrency: String,
        promocode: String
    ): Either<MyException, PaymentTokenRs>

    suspend fun generateHyptoToken(
        orderId: String,
        orderAmount: String,
        orderCurrency: String,
        promocode: String
    ): Either<MyException, HyptoTokenRs>

    suspend fun generatePaytmToken(
        orderId: String,
        orderAmount: String,
        promocode: String
    ): Either<MyException, PaytmTokenRs>

    suspend fun getAddMoneyToWallet(
        amount: String,
        orderId: String,
        jsonResponse: String,
        upiId: String
    ): Either<MyException, BaseResponse>

    suspend fun walletTransaction(type: String, page: Int): Either<MyException, WalletHistoryRs>
    suspend fun getBeneficiaryAdd(
        holder_name: String,
        nickname: String,
        email: String,
        phone: String,
        bank_name: String,
        acc_number: String,
        ifsc: String,
        address: String,
        city: String,
        state: String,
        pincode: String,
        upi: String,
        transferMode: String
    ): Either<MyException, BaseResponse>

    suspend fun getBeneficiaryList(transferMode: String): Either<MyException, BeneficiaryRs>
    suspend fun getBeneficiaryDelete(beneficiaryId: String): Either<MyException, BaseResponse>
    suspend fun withdraw(amount: String, beneficiaryId: String): Either<MyException, WithDrawRs>
    suspend fun getSpinList(): Either<MyException, SpinListRs>
    suspend fun getSpinWin(spinId: String): Either<MyException, SpinListRs>
    suspend fun getGameCategory(): Either<MyException, GameCategoryResponse>
    suspend fun getCheckRootDevice(
        ipAddress: String,
        isRoot: String
    ): Either<MyException, BaseResponse>

    suspend fun getCheckWithdrawalTransaction(): Either<MyException, CheckWithdrawTransactionRs>
    suspend fun getPaymentGatewayIncidents(): Either<MyException, SuccessRateIncidentRs>
    suspend fun getUpdateUserState(state: String): Either<MyException, BaseResponse>
    suspend fun submitKYC(updateKYCPRQ: UpdateKYCPRQ): Either<MyException, BaseResponse>
    suspend fun getSubmittedKYC(): Either<MyException, SubmittedKYC>
}
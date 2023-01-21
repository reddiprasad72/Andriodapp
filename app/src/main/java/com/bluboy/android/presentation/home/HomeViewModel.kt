package com.bluboy.android.presentation.home


import androidx.lifecycle.MutableLiveData
import com.bluboy.android.data.contract.HomeRepo
import com.bluboy.android.data.models.*
import com.bluboy.android.presentation.core.BaseViewModel
import com.bluboy.android.presentation.utility.PrefKeys
import kotlinx.coroutines.launch

class HomeViewModel constructor(private val homeRepo: HomeRepo) : BaseViewModel() {

    val cmsPageRSLiveData: MutableLiveData<CMSPageRS> = MutableLiveData()
    val gamesListObserver: MutableLiveData<GamesListResponse> = MutableLiveData()
    val featuredGamesListObserver: MutableLiveData<GamesListResponse> = MutableLiveData()
    val BattlesObserver: MutableLiveData<BattlesRs> = MutableLiveData()
    val profileObserver: MutableLiveData<RegisterUserRS> = MutableLiveData()
    val tournamentJoinCheckObserver: MutableLiveData<TournamentRs> = MutableLiveData()
    val tournamentPriceBrakDownObserver: MutableLiveData<TournamentPriceBrakDownRs> =
        MutableLiveData()
    val logoutRSLiveData: MutableLiveData<BaseResponse> = MutableLiveData()
    val battleHistoryObserver: MutableLiveData<BattaleHistoryRs> = MutableLiveData()
    val GameLeaderboardObserver: MutableLiveData<LeaderboardRs> = MutableLiveData()
    val faqDataListRSLiveData: MutableLiveData<FAQDataListRS> = MutableLiveData()
    val contactUsRSLiveData: MutableLiveData<BaseResponse> = MutableLiveData()
    val pushNotificatioObserver: MutableLiveData<BaseResponse> = MutableLiveData()
    val howtoPlayObserver: MutableLiveData<HowToPlayRs> = MutableLiveData()
    val NotificationObserver: MutableLiveData<NotificationRs> = MutableLiveData()

    val LeaderboardObserverDaily: MutableLiveData<LeaderboardRs> = MutableLiveData()
    val LeaderboardObserverWeekly: MutableLiveData<LeaderboardRs> = MutableLiveData()
    val LeaderboardObserverMonthly: MutableLiveData<LeaderboardRs> = MutableLiveData()

    val checkUserNameAvailableObserver: MutableLiveData<BaseResponse> = MutableLiveData()
    val endGameResponse: MutableLiveData<BaseResponse> = MutableLiveData()
    val promocodeResponse: MutableLiveData<PromocodeRs> = MutableLiveData()
    val applyPromocodeObserver: MutableLiveData<ApplyPromoCodeRs> = MutableLiveData()
    val referralHistoryObserver: MutableLiveData<ReferralHistoryRs> = MutableLiveData()
    val bannerObserver: MutableLiveData<BannerRs> = MutableLiveData()
    val trendingGamesObserver: MutableLiveData<TrendingGamesRs> = MutableLiveData()
    val paymentTokenObserver: MutableLiveData<PaymentTokenRs> = MutableLiveData()
    val hyptoTokenObserver: MutableLiveData<HyptoTokenRs> = MutableLiveData()

    val paymentPaytmTokenObserver: MutableLiveData<PaytmTokenRs> = MutableLiveData()
    val addMoneyToWalletObserver: MutableLiveData<BaseResponse> = MutableLiveData()

    val walletHistoryAllObserver: MutableLiveData<WalletHistoryRs> = MutableLiveData()
    val walletHistoryWinObserver: MutableLiveData<WalletHistoryRs> = MutableLiveData()
    val walletHistoryCashObserver: MutableLiveData<WalletHistoryRs> = MutableLiveData()
    val walletHistoryWithdrawalObserver: MutableLiveData<WalletHistoryRs> = MutableLiveData()
    val walletHistoryBonusObserver: MutableLiveData<WalletHistoryRs> = MutableLiveData()

    val beneficiaryAddObserver: MutableLiveData<BaseResponse> = MutableLiveData()
    val beneficiaryListObserver: MutableLiveData<BeneficiaryRs> = MutableLiveData()
    val beneficiaryDeleteObserver: MutableLiveData<BaseResponse> = MutableLiveData()
    val withDrawObserver: MutableLiveData<WithDrawRs> = MutableLiveData()
    val spinListObserver: MutableLiveData<SpinListRs> = MutableLiveData()
    val spinWinObserver: MutableLiveData<SpinListRs> = MutableLiveData()
    val gameCategoryObserver: MutableLiveData<GameCategoryResponse> = MutableLiveData()
    val checkRootDeviceObserver: MutableLiveData<BaseResponse> = MutableLiveData()
    val checkWithdrawalTransactionObserver: MutableLiveData<CheckWithdrawTransactionRs> =
        MutableLiveData()
    val paymentGatewayIncidentsObserver: MutableLiveData<SuccessRateIncidentRs> = MutableLiveData()
    val checkStateStausResponse: MutableLiveData<BaseResponse> = MutableLiveData()
    val updateUserStateResponse: MutableLiveData<BaseResponse> = MutableLiveData()
    val updateKYCObserver: MutableLiveData<BaseResponse> = MutableLiveData()
    val detailsKYCObserver: MutableLiveData<SubmittedKYC> = MutableLiveData()

    //    val varSettingRS: MutableLiveData<VarSettingRS> = MutableLiveData()
    val varSettingRS by lazy { MutableLiveData<VarSettingRS>() }


    fun getSettingVar() {
        launch {
            postValue(homeRepo.getSettingVar(), varSettingRS)
        }
    }

    fun getCMSPageData(page_code: String) {
        val cmsPagePRQ = CMSPagePRQ(
            page_code
        )
        launch {
            postValue(homeRepo.getCMSPageData(cmsPagePRQ), cmsPageRSLiveData)
        }
    }

    fun getGameList(page: Int) {
        launch {
            postValue(homeRepo.getGamesList(page), gamesListObserver)
        }
    }

    fun getFeaturedGames(page: Int) {
        launch {
            postValue(homeRepo.getFeaturedGames(page), featuredGamesListObserver)
        }
    }

    fun getBattles(gameId: Int, page: Int) {
        launch {
            postValue(homeRepo.getBattles(gameId, page), BattlesObserver)
        }
    }

    fun getPromocodeList() {
        launch {
            postValue(homeRepo.getPromocodeList(), promocodeResponse)
        }
    }

    fun getProfile() {
        launch {
            postValue(homeRepo.getProfile(), profileObserver)
        }
    }

    fun getTournamentJoinCheck(tournamentId: String) {
        launch {
            postValue(
                homeRepo.getTournamentJoinCheck(tournamentId),
                tournamentJoinCheckObserver
            )
        }
    }

    fun getTournamentPriceBreakDown(tournamentId: String) {
        launch {
            postValue(
                homeRepo.getTournamentPriceBreakDown(tournamentId),
                tournamentPriceBrakDownObserver
            )
        }
    }

    fun performLogout() {
        val logoutPRQ = LogoutPRQ(
            PrefKeys.getAuthKey()
        )
        launch {
            postValue(homeRepo.performLogout(logoutPRQ), logoutRSLiveData)
        }
    }

    fun getBattleHistory(gameId: String, page: Int) {
        launch {
            postValue(homeRepo.getBattleHistory(gameId, page), battleHistoryObserver)
        }
    }

    fun getAllGameHistory(page: Int) {
        launch {
            postValue(homeRepo.getAllGameHistory(page), battleHistoryObserver)
        }
    }

    fun getGameLeaderboard(gameId: String, page: Int) {
        launch {
            postValue(homeRepo.getGameLeaderboard(gameId, page), GameLeaderboardObserver)
        }
    }

    fun getFAQs(page: Int) {
        launch {
            postValue(homeRepo.getFaqs(page), faqDataListRSLiveData)
        }
    }

    fun getContactUs(
        email: String,
        message: String,
        ipAddress: String
    ) {
        launch {
            postValue(homeRepo.getContactUs(email, message, ipAddress), contactUsRSLiveData)
        }
    }

    fun getPushNotification(status: String) {
        launch {
            postValue(homeRepo.getPushNotification(status), pushNotificatioObserver)
        }
    }

    fun getApplyPromocode(amount: String, couponCode: String) {
        launch {
            postValue(homeRepo.getApplyPromocode(amount, couponCode), applyPromocodeObserver)
        }
    }

    fun getHowToPlay(page: Int) {
        launch {
            postValue(homeRepo.getHowToPlay(page), howtoPlayObserver)
        }
    }

    fun getNotificationList(page: Int) {
        launch {
            postValue(homeRepo.getNotificationList(page), NotificationObserver)
        }
    }

    fun getLeaderboardDaily(page: Int) {
        launch {
            postValue(homeRepo.getLeaderboard("daily", page), LeaderboardObserverDaily)
        }
    }

    fun getLeaderboardWeekly(page: Int) {
        launch {
            postValue(homeRepo.getLeaderboard("weekly", page), LeaderboardObserverWeekly)
        }
    }

    fun getLeaderboardMonthly(page: Int) {
        launch {
            postValue(homeRepo.getLeaderboard("Monthly", page), LeaderboardObserverMonthly)
        }
    }

    fun getEndTournament(authKey: String, tournamentId: String, roomCode: String) {
        launch {
            postValue(
                homeRepo.getEndTournament(authKey, tournamentId, roomCode),
                endGameResponse
            )
        }
    }

    fun checkUserNameAvailabel(username: String) {
        launch {
            postValue(homeRepo.checkUserNameAvailabel(username), checkUserNameAvailableObserver)
        }
    }

    fun checkStateStatus(state: String) {
        launch {
            postValue(homeRepo.checkStateStatus(state), checkStateStausResponse)

        }
    }

    fun ApiReferralHistory(page: Int) {
        launch {
            postValue(homeRepo.ApiReferralHistory(page), referralHistoryObserver)
        }
    }

    fun ApiBanner(page: Int) {
        launch {
            postValue(homeRepo.ApiBanner(page), bannerObserver)
        }
    }

    fun ApiTrendingGames(page: Int) {
        launch {
            postValue(homeRepo.ApiTrendingGames(page), trendingGamesObserver)
        }
    }

    fun generateToken(
        orderId: String,
        orderAmount: String,
        orderCurrency: String, promocode: String
    ) {
        launch {
            postValue(
                homeRepo.getPaymentToken(orderId, orderAmount, orderCurrency, promocode),
                paymentTokenObserver
            )
        }
    }

    fun generateHyptoToken(
        orderId: String,
        orderAmount: String,
        orderCurrency: String, promocode: String
    ) {
        launch {
            postValue(
                homeRepo.generateHyptoToken(orderId, orderAmount, orderCurrency, promocode),
                hyptoTokenObserver
            )
        }
    }

    fun generatePaytmToken(
        orderId: String,
        orderAmount: String,
        promocode: String
    ) {
        launch {
            postValue(
                homeRepo.generatePaytmToken(orderId, orderAmount, promocode),
                paymentPaytmTokenObserver
            )
        }
    }


    fun addMoneyToWallet(
        amount: String,
        orderId: String,
        cfResponse: String, upiId: String
    ) {
        launch {
            postValue(
                homeRepo.getAddMoneyToWallet(amount, orderId, cfResponse, upiId),
                addMoneyToWalletObserver
            )
        }
    }

    fun walletAllTransaction(page: Int) {
        launch {
            postValue(homeRepo.walletTransaction("A", page), walletHistoryAllObserver)
        }
    }

    fun walletWinningTransaction(page: Int) {
        launch {
            postValue(homeRepo.walletTransaction("W", page), walletHistoryWinObserver)
        }
    }

    fun walletCashTransaction(page: Int) {
        launch {
            postValue(homeRepo.walletTransaction("D", page), walletHistoryCashObserver)
        }
    }

    fun walletWithdrawalTransaction(page: Int) {
        launch {
            postValue(homeRepo.walletTransaction("WD", page), walletHistoryWithdrawalObserver)
        }
    }

    fun walletBonusTransaction(page: Int) {
        launch {
            postValue(homeRepo.walletTransaction("B", page), walletHistoryBonusObserver)
        }
    }

    fun getBeneficiaryAdd(
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
        pincode: String, upi: String, transferMode: String
    ) {
        launch {
            postValue(
                homeRepo.getBeneficiaryAdd(
                    holder_name,
                    nickname,
                    email,
                    phone,
                    bank_name,
                    acc_number,
                    ifsc,
                    address,
                    city,
                    state,
                    pincode,
                    upi,
                    transferMode
                ), beneficiaryAddObserver
            )
        }
    }

    fun getBeneficiaryList(transferMode: String) {
        launch {
            postValue(homeRepo.getBeneficiaryList(transferMode), beneficiaryListObserver)
        }
    }

    fun getBeneficiaryDelete(beneficiaryId: String) {
        launch {
            postValue(homeRepo.getBeneficiaryDelete(beneficiaryId), beneficiaryDeleteObserver)
        }
    }

    fun withdraw(
        amount: String,
        beneficiaryId: String
    ) {
        launch {
            postValue(homeRepo.withdraw(amount, beneficiaryId), withDrawObserver)
        }
    }

    fun getSpinList() {
        launch {
            postValue(homeRepo.getSpinList(), spinListObserver)
        }
    }

    fun getSpinWin(spinId: String) {
        launch {
            postValue(homeRepo.getSpinWin(spinId), spinWinObserver)
        }
    }

    fun getGameCategory() {
        launch {
            postValue(homeRepo.getGameCategory(), gameCategoryObserver)
        }
    }

    fun getCheckRootDevice(ipAddress: String, isRoot: String) {
        launch {
            postValue(homeRepo.getCheckRootDevice(ipAddress, isRoot), checkRootDeviceObserver)
        }
    }

    fun getCheckWithdrawalTransaction() {
        launch {
            postValue(homeRepo.getCheckWithdrawalTransaction(), checkWithdrawalTransactionObserver)
        }
    }

    fun getPaymentGatewayIncidents() {
        launch {
            postValue(homeRepo.getPaymentGatewayIncidents(), paymentGatewayIncidentsObserver)
        }
    }

    fun getUpdateUserState(state: String) {
        launch {
            postValue(homeRepo.getUpdateUserState(state), updateUserStateResponse)
        }
    }

    fun submitKYC(updateKYCPRQ: UpdateKYCPRQ) {
        launch {
            postValue(homeRepo.submitKYC(updateKYCPRQ), updateKYCObserver)
        }
    }

    fun getSubmittedKYC() {
        launch {
            postValue(homeRepo.getSubmittedKYC(), detailsKYCObserver)
        }
    }

}




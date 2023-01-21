package com.bluboy.android.data.repository

import android.util.Log
import com.bluboy.android.BuildConfig
import com.bluboy.android.data.BaseRepository
import com.bluboy.android.data.Either
import com.bluboy.android.data.contract.HomeRepo
import com.bluboy.android.data.models.*
import com.bluboy.android.domain.exceptions.MyException
import com.bluboy.android.domain.network.HomeApiService
import com.bluboy.android.presentation.utility.AppConstant
import com.bluboy.android.presentation.utility.PrefKeys

class HomeRepository constructor(
    private val homeApiService: HomeApiService
//    private val appDao: AppDao
) : HomeRepo, BaseRepository() {


    override suspend fun getCMSPageData(cmsPagePRQ: CMSPagePRQ): Either<MyException, CMSPageRS> {
        return either {
            homeApiService.getCMSPages(
                cmsPagePRQ.page_code.orEmpty(), BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getGamesList(page: Int): Either<MyException, GamesListResponse> {
        return either {
            homeApiService.getGames(
                PrefKeys.getAuthKey(),
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getFeaturedGames(
        page: Int
    ): Either<MyException, GamesListResponse> {
        return either {
            homeApiService.getFeaturedGames(
                PrefKeys.getAuthKey(),
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }


    override suspend fun getBattles(
        gameId: Int,
        page: Int
    ): Either<MyException, BattlesRs> {
        return either {
            homeApiService.getBattles(
                PrefKeys.getAuthKey(),
                gameId,
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getProfile(
    ): Either<MyException, RegisterUserRS> {
        return either {
            homeApiService.getProfile(PrefKeys.getAuthKey(), BuildConfig.VERSION_NAME)
        }
    }


    override suspend fun getPromocodeList(
    ): Either<MyException, PromocodeRs> {
        return either {
            homeApiService.getPromocodeList(PrefKeys.getAuthKey(), BuildConfig.VERSION_NAME)
        }
    }

    override suspend fun getTournamentJoinCheck(
        tournamentId: String
    ): Either<MyException, TournamentRs> {
        return either {
            homeApiService.getTournamentJoinCheck(
                PrefKeys.getAuthKey(),
                tournamentId,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getTournamentPriceBreakDown(
        tournamentId: String
    ): Either<MyException, TournamentPriceBrakDownRs> {
        return either {
            homeApiService.getTournamentPriceBreakDown(
                PrefKeys.getAuthKey(),
                tournamentId,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun performLogout(logoutPRQ: LogoutPRQ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.performLogout(
                logoutPRQ.authKey, BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getBattleHistory(
        gameId: String,
        page: Int
    ): Either<MyException, BattaleHistoryRs> {
        return either {
            homeApiService.getBattleHistory(
                PrefKeys.getAuthKey(),
                gameId,
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getAllGameHistory(
        page: Int
    ): Either<MyException, BattaleHistoryRs> {
        return either {
            homeApiService.getAllGameHistory(
                PrefKeys.getAuthKey(),
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getGameLeaderboard(
        gameId: String,
        page: Int
    ): Either<MyException, LeaderboardRs> {
        return either {
            homeApiService.getGameLeaderboard(
                PrefKeys.getAuthKey(),
                gameId,
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getFaqs(page: Int): Either<MyException, FAQDataListRS> {
        return either {
            homeApiService.getFaqs(page, AppConstant.pageSizeData, BuildConfig.VERSION_NAME)
        }
    }

    override suspend fun getContactUs(
        email: String,
        message: String,
        ipAddress: String
    ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.getContactUs(
                PrefKeys.getAuthKey(),
                email,
                message,
                ipAddress,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getPushNotification(status: String): Either<MyException, BaseResponse> {
        return either {
            homeApiService.getPushNotification(
                PrefKeys.getAuthKey(),
                status, BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getApplyPromocode(
        amount: String, couponCode: String
    ): Either<MyException, ApplyPromoCodeRs> {
        return either {
            homeApiService.getApplyPromocode(
                PrefKeys.getAuthKey(),
                amount,
                couponCode,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getHowToPlay(
        page: Int
    ): Either<MyException, HowToPlayRs> {
        return either {
            homeApiService.getHowToPlay(
                PrefKeys.getAuthKey(),
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getNotificationList(
        page: Int
    ): Either<MyException, NotificationRs> {
        return either {
            homeApiService.getNotificationList(
                PrefKeys.getAuthKey(),
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getLeaderboard(
        type: String,
        page: Int
    ): Either<MyException, LeaderboardRs> {
        return either {
            homeApiService.getLeaderboard(
                PrefKeys.getAuthKey(),
                type,
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun checkUserNameAvailabel(
        username: String,
    ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.checkUserNameAvailabel(
                PrefKeys.getAuthKey(),
                username,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getEndTournament(
        authKey: String,
        tournamentId: String,
        roomCode: String
    ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.getEndTournament(
                PrefKeys.getAuthKey(),
                tournamentId,
                roomCode,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun forceUpdateApp(version: String): Either<MyException, ForceUpdateRs> {
        return either {
            homeApiService.forceUpdateApp(version, AppConstant.DeviceTypeAndroid)
        }
    }

    override suspend fun checkAppHashKey(
        hashKey: String,
        bundleId: String
    ): Either<MyException, ForceUpdateRs> {
        return either {
            homeApiService.checkAppHashKey(hashKey, bundleId)
        }
    }

    override suspend fun checkStateStatus(state: String): Either<MyException, BaseResponse> {
        return either {
            homeApiService.checkStateStatus(state)
        }
    }

    override suspend fun getSettingVar(): Either<MyException, VarSettingRS> {
        return either {
            homeApiService.getSettingVar()
        }
    }

    override suspend fun ApiReferralHistory(
        page: Int
    ): Either<MyException, ReferralHistoryRs> {
        return either {
            homeApiService.ApiReferralHistory(
                PrefKeys.getAuthKey(),
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun ApiBanner(
        page: Int
    ): Either<MyException, BannerRs> {
        return either {
            homeApiService.ApiBanner(
                PrefKeys.getAuthKey(),
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun ApiTrendingGames(
        page: Int
    ): Either<MyException, TrendingGamesRs> {
        return either {
            homeApiService.ApiTrendingGames(
                PrefKeys.getAuthKey(),
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getPaymentToken(
        orderId: String,
        orderAmount: String,
        orderCurrency: String, promocode: String
    ): Either<MyException, PaymentTokenRs> {
        return either {
            homeApiService.getGenerateToken(
                PrefKeys.getAuthKey(),
                orderId,
                orderAmount,
                orderCurrency,
                promocode,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun generateHyptoToken(
        orderId: String,
        orderAmount: String,
        orderCurrency: String, promocode: String
    ): Either<MyException, HyptoTokenRs> {
        return either {
            homeApiService.generateHyptoToken(
                PrefKeys.getAuthKey(),
                orderId,
                orderAmount,
                orderCurrency,
                promocode,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun generatePaytmToken(
        orderId: String,
        orderAmount: String,
        promocode: String
    ): Either<MyException, PaytmTokenRs> {
        return either {
            homeApiService.generatePaytmToken(
                PrefKeys.getAuthKey(),
                orderId,
                orderAmount,
                promocode,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getAddMoneyToWallet(
        amount: String,
        orderId: String,
        jsonResponse: String, upiId: String
    ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.addMoneyToWallet(
                PrefKeys.getAuthKey(),
                orderId,
                amount,
                jsonResponse,
                upiId,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun walletTransaction(
        type: String,
        page: Int
    ): Either<MyException, WalletHistoryRs> {
        return either {
            homeApiService.walletTransaction(
                PrefKeys.getAuthKey(),
                type,
                page,
                AppConstant.pageSizeData,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getBeneficiaryAdd(
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
    ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.getBeneficiaryAdd(
                PrefKeys.getAuthKey(),
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
                transferMode,
                "Y",
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getBeneficiaryList(
        transferMode: String
    ): Either<MyException, BeneficiaryRs> {
        return either {
            homeApiService.getBeneficiaryList(
                PrefKeys.getAuthKey(),
                transferMode,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getBeneficiaryDelete(
        beneficiaryId: String
    ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.getBeneficiaryDelete(
                PrefKeys.getAuthKey(),
                beneficiaryId,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun withdraw(
        amount: String,
        beneficiaryId: String

    ): Either<MyException, WithDrawRs> {
        return either {
            homeApiService.withdraw(
                PrefKeys.getAuthKey(),
                amount, beneficiaryId, BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getSpinList(): Either<MyException, SpinListRs> {
        return either {
            homeApiService.getSpinList(PrefKeys.getAuthKey(), BuildConfig.VERSION_NAME)
        }
    }

    override suspend fun getSpinWin(spinId: String): Either<MyException, SpinListRs> {
        return either {
            homeApiService.getSpinWin(PrefKeys.getAuthKey(), spinId, BuildConfig.VERSION_NAME)
        }
    }

    override suspend fun getGameCategory(): Either<MyException, GameCategoryResponse> {
        return either {
            homeApiService.getGameCategory(PrefKeys.getAuthKey(), BuildConfig.VERSION_NAME)
        }
    }

    override suspend fun getCheckRootDevice(
        ipAddress: String,
        isRoot: String
    ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.getCheckRootDevice(
                PrefKeys.getAuthKey(),
                ipAddress,
                isRoot,
                BuildConfig.VERSION_NAME
            )
        }
    }

    override suspend fun getCheckWithdrawalTransaction(): Either<MyException, CheckWithdrawTransactionRs> {
        return either {
            homeApiService.getCheckWithdrawalTransaction(PrefKeys.getAuthKey())
        }
    }

    override suspend fun getPaymentGatewayIncidents(): Either<MyException, SuccessRateIncidentRs> {
        return either {
            homeApiService.getPaymentGatewayIncidents(PrefKeys.getAuthKey())
        }
    }

    override suspend fun getUpdateUserState(state: String): Either<MyException, BaseResponse> {
        return either {
            homeApiService.getUpdateUserState(PrefKeys.getAuthKey(), state)
        }
    }

    override suspend fun submitKYC(updateKYCPRQ: UpdateKYCPRQ): Either<MyException, BaseResponse> {
        return either {
            homeApiService.submitKYC(
                updateKYCPRQ.auth_key,
                updateKYCPRQ.kyc_type,
                updateKYCPRQ.kyc_number,
                updateKYCPRQ.kyc_image,
                updateKYCPRQ.kyc_image2,
            )
        }
    }

    override suspend fun getSubmittedKYC(): Either<MyException, SubmittedKYC> {
        return either {
            homeApiService.getSubmittedKYC(PrefKeys.getAuthKey())
        }
    }
}
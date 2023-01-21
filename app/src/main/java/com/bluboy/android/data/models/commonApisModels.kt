package com.bluboy.android.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


// Promocode
data class PromocodeRs(
//    @SerializedName("data")
//    var `data`: List<PromocodeData>? = listOf()
    @SerializedName("data")
    val `data`: PromocodeData? = null
) : BaseResponse()


@Parcelize
data class PromocodeData(
    @SerializedName("coupons")
    val coupons: List<Coupon>
) : Parcelable

@Parcelize
public data class Coupon(
    @SerializedName("coupon_code")
    var couponCode: String = "",
    @SerializedName("coupon_description")
    var couponDescription: String = "",
    @SerializedName("coupon_discount")
    var couponDiscount: String = "",
    @SerializedName("coupon_discount_on_min_amount")
    var couponDiscountOnMinAmount: String = "",
    @SerializedName("coupon_discount_type")
    var couponDiscountType: String = "",
    @SerializedName("coupon_end_date")
    var couponEndDate: String = "",
    @SerializedName("coupon_id")
    var couponId: String = "",
    @SerializedName("coupon_max_discount")
    var couponMaxDiscount: String = "",
    @SerializedName("coupon_name")
    var couponName: String = "",
    @SerializedName("coupon_start_date")
    var couponStartDate: String = "",
    @SerializedName("coupon_use_count")
    var couponUseCount: String = "",
    @SerializedName("coupon_use_limit")
    var couponUseLimit: String = ""
) : Parcelable


// Category Wise GAME
data class GameCategoryResponse(
    @SerializedName("data")
    val `data`: List<CategoryData>,
) : BaseResponse()

@Parcelize
data class CategoryData(
    @SerializedName("category_icon")
    val categoryIcon: String,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_order")
    val categoryOrder: String,
    @SerializedName("category_status")
    val categoryStatus: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("games")
    val games: List<GamesData>
) : Parcelable

// home Game List

data class GamesListResponse(
    @SerializedName("data")
    val `data`: GamesDatum
) : BaseResponse()

data class GamesDatum(
    @SerializedName("games")
    val games: List<GamesData>
) : GeneralPagination()

open class GeneralPagination(
    @SerializedName("is_have_more_records")
    val isHaveMoreRecords: String = "",
    @SerializedName("max_page_num")
    val maxPageNum: Int = 1,
    @SerializedName("page")
    val page: Int = 1,
    @SerializedName("page_size")
    val pageSize: Int = 10,
    @SerializedName("skip_records")
    val skipRecords: Int = 0,
    @SerializedName("total_records")
    val totalRecords: Int = 0
)

@Parcelize
data class GamesData(
    @SerializedName("category_name")
    val categoryName: String? = "",
    @SerializedName("game_banner")
    val gameBanner: String,
    @SerializedName("game_description")
    val gameDescription: String,
    @SerializedName("game_featured")
    val gameFeatured: String,
    @SerializedName("game_id")
    val gameId: String,

    @SerializedName("game_key")
    val game_key: String,
    @SerializedName("game_image")
    val gameImage: String,
    @SerializedName("game_name")
    val gameName: String,
    @SerializedName("game_orientation")
    val gameOrientation: String,
    @SerializedName("game_status")
    val gameStatus: String,
    @SerializedName("game_type")
    val gameType: String,
    @SerializedName("game_url")
    val gameUrl: String,
    @SerializedName("game_version")
    val gameVersion: String,
    @SerializedName("total_online_users")
    val totalOnlineUsers: String? = "0",
    @SerializedName("game_comming_soon")
    val gameCommingSoon: String,
    @SerializedName("game_tile")
    val gameTile: String,
    @SerializedName("game_tag")
    val gameTag: String? = null,
) : Parcelable


//Notification List
data class NotificationRs(
    @SerializedName("data")
    var `data`: NotificationData? = null
) : BaseResponse()

data class NotificationData(
    @SerializedName("notifications")
    var notifications: List<Notification> = listOf(),
) : GeneralPagination()

data class Notification(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("notification_id")
    var notificationId: String = "",
    @SerializedName("notification_json")
    var notificationJson: String = "",
    @SerializedName("notification_message")
    var notificationMessage: String = "",
    @SerializedName("notification_title")
    var notificationTitle: String = "",
    @SerializedName("notification_type")
    var notificationType: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)


// Tournamnets
data class TournamentsRs(
    @SerializedName("data")
    var `data`: TournamentsData? = null
) : BaseResponse()


data class TournamentsData(
    @SerializedName("tournaments")
    val tournaments: List<Tournament> = listOf()
) : GeneralPagination()

//@Parcelize
//data class Tournament(
//    @SerializedName("game_id")
//    val gameId: String,
//    @SerializedName("game_name")
//    val gameName: String,
//    @SerializedName("tournament_bonus_limit")
//    val tournamentBonusLimit: String,
//    @SerializedName("tournament_code")
//    val tournamentCode: String,
//    @SerializedName("tournament_detail")
//    val tournamentDetail: String,
//    @SerializedName("tournament_end")
//    val tournamentEnd: String,
//    @SerializedName("tournament_entry_fees")
//    val tournamentEntryFees: Int,
//    @SerializedName("tournament_id")
//    val tournamentId: String,
//    @SerializedName("tournament_max_player_count")
//    val tournamentMaxPlayerCount: String,
//    @SerializedName("tournament_min_player_count")
//    val tournamentMinPlayerCount: String,
//    @SerializedName("tournament_name")
//    val tournamentName: String,
//    @SerializedName("tournament_start")
//    val tournamentStart: String,
//    @SerializedName("tournament_status")
//    val tournamentStatus: String,
//    @SerializedName("tournament_total_joined_players")
//    val tournamentTotalJoinedPlayers: Int,
//    @SerializedName("tournament_total_winners")
//    val tournamentTotalWinners: String,
//    @SerializedName("tournament_total_winning_amount")
//    val tournamentTotalWinningAmount: Float,
//    @SerializedName("tournament_type")
//    val tournamentType: String,
//    @SerializedName("type_name")
//    val typeName: String
//) : Parcelable


// Tournamnets
data class BattlesRs(
    @SerializedName("data")
    var `data`: BattlesData? = null
) : BaseResponse()


data class BattlesData(
    @SerializedName("battles")
    val battle: List<Battle> = listOf()
) : GeneralPagination()

@Parcelize
data class Battle(
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("game_name")
    val gameName: String,
    @SerializedName("tournament_bonus_limit")
    val tournamentBonusLimit: String,
    @SerializedName("tournament_code")
    val tournamentCode: String,
    @SerializedName("tournament_detail")
    val tournamentDetail: String,
    @SerializedName("tournament_end")
    val tournamentEnd: String,
    @SerializedName("tournament_entry_fees")
    val tournamentEntryFees: String,
    @SerializedName("tournament_id")
    val tournamentId: String,
    @SerializedName("tournament_max_player_count")
    val tournamentMaxPlayerCount: String,
    @SerializedName("tournament_min_player_count")
    val tournamentMinPlayerCount: String,
    @SerializedName("tournament_name")
    val tournamentName: String,
    @SerializedName("tournament_start")
    val tournamentStart: String,
    @SerializedName("tournament_status")
    val tournamentStatus: String,
    @SerializedName("tournament_total_joined_players")
    val tournamentTotalJoinedPlayers: Int,
    @SerializedName("tournament_total_winners")
    val tournamentTotalWinners: String,
    @SerializedName("tournament_total_winning_amount")
    val tournamentTotalWinningAmount: Float,
    @SerializedName("tournament_type")
    val tournamentType: String,
    @SerializedName("type_name")
    val typeName: String,
    @SerializedName("total_online_users")
    val totalOnlineUsers: String,
    @SerializedName("tournament_featured")
    val tournamentFeatured: String
) : Parcelable

// Leaderboard
data class LeaderboardRs(
    @SerializedName("data")
    var `data`: LeaderboardData? = null
) : BaseResponse()


data class LeaderboardData(
    @SerializedName("user_rank")
    val ownRank: OwnRank? = null,
    @SerializedName("leader_board")
    val leaderBoard: List<LeaderBoard> = listOf()
) : GeneralPagination()


@Parcelize
data class OwnRank(
    @SerializedName("profile_pic")
    val profilePic: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("score")
    val score: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("total_winning_amount")
    var totalWinningAmount: String = ""
) : Parcelable

// KYC Submitted Details
data class SubmittedKYC(
    @SerializedName("data")
    var `data`: KYCData? = null
) : BaseResponse()

data class KYCData(
    @SerializedName("kyc_details")
    val kycDetails: KYCDetails? = null,
)

@Parcelize
data class KYCDetails(
    @SerializedName("document_type")
    val documentType: String,
    @SerializedName("document_number")
    val documentNumber: String,
    @SerializedName("front_pic")
    val frontPic: String,
    @SerializedName("back_pic")
    val backPic: String,
    @SerializedName("status")
    val status: String
) : Parcelable

//@Parcelize
//data class LeaderBoard(
//    @SerializedName("profile_pic")
//    val profilePic: String,
//    @SerializedName("rank")
//    val rank: Int,
//    @SerializedName("score")
//    val score: String,
//    @SerializedName("user_id")
//    val userId: String,
//    @SerializedName("user_name")
//    val userName: String
//) : Parcelable


// Battle History
data class BattaleHistoryRs(
    @SerializedName("data")
    var `data`: BattleHistoryData? = null
) : BaseResponse()

data class BattleHistoryData(
    @SerializedName("battle_history")
    var battleHistory: List<BattleHistory> = listOf(),
    @SerializedName("is_have_more_records")
    var isHaveMoreRecords: String = "",
    @SerializedName("max_page_num")
    var maxPageNum: Int = 0,
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("page_size")
    var pageSize: Int = 0,
    @SerializedName("skip_records")
    var skipRecords: Int = 0,
    @SerializedName("total_records")
    var totalRecords: Int = 0
)

data class BattleHistory(
    @SerializedName("game_image")
    var gameImage: String = "",
    @SerializedName("players")
    var players: List<Player> = listOf(),
    @SerializedName("room")
    var room: String = "",
    @SerializedName("tournament_entry_fees")
    var tournamentEntryFees: String = "",
    @SerializedName("tournament_id")
    var tournamentId: String = "",
    @SerializedName("tournament_name")
    var tournamentName: String = "",
    @SerializedName("tournament_player_per_room")
    var tournamentPlayerPerRoom: String = "",
    @SerializedName("tournament_start")
    var tournamentStart: String = "",
    @SerializedName("game_starts_on")
    var gameStart: String = "",
    @SerializedName("win_status")
    var winStatus: String = "",
    @SerializedName("winning_amount")
    var winAmount: String = ""
)

data class Player(
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("profile_pic")
    var profilePic: String = "",
    @SerializedName("score")
    var score: String = "",
    @SerializedName("uname")
    var uname: String = "",
    @SerializedName("user_name")
    var userName: String = "",
    @SerializedName("win_status")
    var winStatus: String = ""
)

//Apply PromoCode
data class ApplyPromoCodeRs(
    val `data`: ApplyPromocodeData
) : BaseResponse()

data class ApplyPromocodeData(
    val coupon_amount: String,
    val total_amount: String
)

// Wallet History
data class WalletHistoryRs(
    @SerializedName("data")
    var `data`: WalletHistoryData? = null
) : BaseResponse()

data class WalletHistoryData(
    @SerializedName("transaction_history")
    var transactionHistory: List<TransactionHistory> = listOf()
) : GeneralPagination()

data class TransactionHistory(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("history_type")
    var historyType: String = "",
    @SerializedName("histroy_message")
    var histroyMessage: String = "",
    @SerializedName("tournament_id")
    var tournamentId: String = "",
    @SerializedName("transaction_id")
    var transactionId: String = "",
    @SerializedName("transaction_type")
    var transactionType: String = "",
    @SerializedName("transaction_title")
    var transactionTitle: String = "",
    @SerializedName("transaction_by")
    var transactionBy: String = "",
    @SerializedName("internal_order_id")
    var orderId: String,
    @SerializedName("withdraw_reason")
    var withdrawReason: String
)


//How to Play
data class HowToPlayRs(
    val `data`: HowToPlayData? = null
) : BaseResponse()

data class HowToPlayData(
    val content: List<HowToPlayContent>
) : GeneralPagination()

data class HowToPlayContent(
    val content_description: String,
    val content_title: String,
    val content_type: String,
    val content_video_url: String,
    val content_image_url: String,
    val game_name: String,
    val game_image: String,
    val game_banner: String,
    val status: String,
    val game_orientation: String,
    var isExpanded: Boolean = false
)


//All Variable

data class VarSettingRS(
    @SerializedName("data")
    val `data`: VariableData? = null
) : BaseResponse()


@Parcelize
data class VariableData(
    @SerializedName("WHATSAPP")
    val whatsAppContact: String,
    @SerializedName("CASHFREE_APPID")
    val cASHFREEAPPID: String,
    @SerializedName("CASHFREE_SECRETKEY")
    val cASHFREESECRETKEY: String,
    @SerializedName("DOWNLOAD_URL")
    val dOWNLOADURL: String,
    @SerializedName("FORCE_APP_JSON_UPDATE_URL")
    val forceAppJsonUpdateUrl: String,
    @SerializedName("FCM_KEY")
    val fCMKEY: String,
    @SerializedName("MIN_ADD_AMOUNT")
    val mINADDAMOUNT: String,
    @SerializedName("MIN_WITHDRAW_AMOUNT")
    val mINWITHDRAWAMOUNT: String,
    @SerializedName("PAYTM_MID")
    val paytmMID: String,
    @SerializedName("PAYTM_MERCHANTID")
    val paytmMerchantKey: String,
    @SerializedName("REFERRED_BONUS")
    val referredBonus: String,
    @SerializedName("REFERE_FRIEND_MESSAGE")
    val refer_friend_message: String,

    @SerializedName("PAYTM_TEST_MODE_STATUS")
    val PAYTM_TEST_MODE: String,
    @SerializedName("CASHFREE_TEST_MODE_STATUS")
    val CASHFREE_TEST_MODE: String,
    @SerializedName("CASHFREE_NOTIFY_URL")
    val CASHFREE_NOTIFY_URL: String,


    @SerializedName("CASHFREE")
    val cashFree: Boolean,
    @SerializedName("PAYTM")
    val paytm: Boolean,
    @SerializedName("HYPTO")
    val hypto: Boolean,

    @SerializedName("PAYOUT_BANK_TRANSFER_CHARGE")
    val bankCharge: String,
    @SerializedName("PAYOUT_PAYTM_CHARGE")
    val paytmCharge: String,
    @SerializedName("PAYOUT_UPI_CHARGE")
    val upiCharge: String,

    @SerializedName("BANK_UPI_TYPE")
    val bankUpiType: String,
//    @SerializedName("PAYOUT_HYPTO_CHARGE")
//    val hyptoCharge: String,
    @SerializedName("PAYOUT_HYPTO_UPI_CHARGE")
    val payoutHyptoUpiCharge: String,
    @SerializedName("PAYOUT_HYPTO_BANK_CHARGE")
    val payoutHyptoBankCharge: String,


    @SerializedName("PAYTM_PAYOUT")
    val paytmPayout: String,
    @SerializedName("BANK_UPI_PAYOUT")
    val bankUpiPayout: String,


    @SerializedName("MAX_ADD_AMOUNT_DIGIT")
    val MAX_ADD_AMOUNT_DIGIT: String,


    @SerializedName("CASHFREE_ENVIRONMENT")
    val cASHFREE_ENVIRONMENT: String = "PROD",

    @SerializedName("POPUP_NOTIFICATION_SHOW")
    val POPUP_NOTIFICATION_SHOW: String,
    @SerializedName("POPUP_NOTIFICATION_CONTENT")
    val POPUP_NOTIFICATION_CONTENT: String,
    @SerializedName("POPUP_NOTIFICATION_TYPE")
    val POPUP_NOTIFICATION_TYPE: String,
    @SerializedName("POPUP_NOTIFICATION_GAME_PROMOTION")
    val POPUP_NOTIFICATION_GAME_PROMOTION: String,
    @SerializedName("POPUP_NOTIFICATION_IMAGE")
    val POPUP_NOTIFICATION_IMAGE: String,
    @SerializedName("POPUP_NOTIFICATION_URL")
    val POPUP_NOTIFICATION_URL: String,
    @SerializedName("POPUP_NOTIFICATION_GAME_PROMOTION_ARRAY")
    val POPUP_NOTIFICATION_GAME_PROMOTION_ARRAY: GamesData? = null,


    @SerializedName("STATES")
    val stateList: ArrayList<StateList>


) : Parcelable


@Parcelize
data class StateList(
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("state_id")
    val stateId: String,
    @SerializedName("state_name")
    val stateName: String
) : Parcelable


// Force update
data class ForceUpdateRs(
    val `data`: ForceUpdateDate? = null,
    val `url`: String? = null
) : BaseResponse()

data class ForceUpdateDate(
    val version: String
)


// Tournament Join Before Check
data class TournamentRs(
    @SerializedName("data")
    val `data`: TournamentList? = null
) : BaseResponse()

data class TournamentList(
    @SerializedName("tournaments")
    val tournaments: List<TournamentData>
) : GeneralPagination()

@Parcelize
data class TournamentData(
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("game_name")
    val gameName: String,
    @SerializedName("tournament_code")
    val tournamentCode: String,
    @SerializedName("tournament_detail")
    val tournamentDetail: String,
//    @SerializedName("tournament_end_date")
//    val tournamentEndDate: String,
    @SerializedName("tournament_end")
    val tournamentEndTime: String,
    @SerializedName("tournament_entry_fees")
    val tournamentEntryFees: String,
    @SerializedName("tournament_id")
    val tournamentId: String,
    @SerializedName("tournament_max_player_count")
    val tournamentMaxPlayerCount: String,
    @SerializedName("tournament_min_player_count")
    val tournamentMinPlayerCount: String,
    @SerializedName("tournament_name")
    val tournamentName: String,
//    @SerializedName("tournament_start_date")
//    val tournamentStartDate: String,
//    @SerializedName("tournament_start_time")
//    val tournamentStartTime: String,
    @SerializedName("tournament_status")
    val tournamentStatus: String,
    @SerializedName("tournament_total_winners")
    val tournamentTotalWinners: String,
    @SerializedName("tournament_total_winning_amount")
    val tournamentTotalWinningAmount: String,
    @SerializedName("tournament_type")
    val tournamentType: String,
    @SerializedName("type_name")
    val typeName: String,
    @SerializedName("tournament_total_joined_players")
    val toalPlayerJoinTournament: String
//    @SerializedName("tournament_start_datetime")
//    val tournamentStartTimestamp: String,
//    @SerializedName("tournament_end_datetime")
//    val tournamentEndTimestamp: String
) : Parcelable


//Tournament Price Breakdown
data class TournamentPriceBrakDownRs(
    val `data`: TournamentPriceBrakDownData
) : BaseResponse()

data class TournamentPriceBrakDownData(
    val deduction_from_bonus_amount: String,
    val deduction_from_wallet_win_amount: String,
    val total_payable: String
)

//Payment Token
data class PaymentTokenRs(
    @SerializedName("data")
    val `data`: TokenData
) : BaseResponse()

data class TokenData(
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("token")
    val token: String
)

// Hypto
data class HyptoTokenRs(
    @SerializedName("data")
    val `data`: HyptoTokenData,

    ) : BaseResponse()

@Parcelize
data class HyptoTokenData(
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("token")
    val token: String
) : Parcelable

// PAYTM Token
data class PaytmTokenRs(
    @SerializedName("data")
    val `data`: TokenPaytmData
) : BaseResponse()

data class TokenPaytmData(
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("customer_id")
    val customerId: String,
    @SerializedName("call_back_url")
    val callBackUrl: String
)


//Withdraw
data class WithDrawRs(
    @SerializedName("data")
    var `data`: WithdrawData? = null
) : BaseResponse()

data class WithdrawData(
    @SerializedName("type")
    var type: String = ""
)

//Beneficiary Add
data class BeneficiaryRs(
    @SerializedName("data")
    var `data`: BeneficiaryDatum? = null
) : BaseResponse()

@Parcelize
data class BeneficiaryDatum(
    @SerializedName("records")
    val beneficiaryData: List<BeneficiaryData>
) : Parcelable

@Parcelize
data class BeneficiaryData(
    var amount: String = "",
    @SerializedName("beneficiary_account_number")
    var beneficiaryAccountNumber: String = "",
    @SerializedName("beneficiary_address")
    var beneficiaryAddress: String = "",
    @SerializedName("beneficiary_bank_name")
    var beneficiaryBankName: String = "",
    @SerializedName("beneficiary_city")
    var beneficiaryCity: String = "",
    @SerializedName("beneficiary_email")
    var beneficiaryEmail: String = "",
    @SerializedName("beneficiary_id")
    var beneficiaryId: String = "",
    @SerializedName("beneficiary_ifsc_code")
    var beneficiaryIfscCode: String = "",
    @SerializedName("beneficiary_name")
    var beneficiaryName: String = "",
    @SerializedName("beneficiary_nick_name")
    var beneficiaryNickName: String = "",
    @SerializedName("beneficiary_phone")
    var beneficiaryPhone: String = "",
    @SerializedName("beneficiary_pincode")
    var beneficiaryPincode: String = "",
    @SerializedName("beneficiary_state")
    var beneficiaryState: String = "",
    @SerializedName("beneficiary_vpa")
    var beneficiaryVpa: String = "",
    @SerializedName("is_deleted")
    var isDeleted: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("transfer_mode")
    var transferMode: String = ""
) : Parcelable

// User End Game
data class UserEndGameRs(
    val `data`: UserEndGameData,
    val message: String,
    val status: Int
)

class UserEndGameData(
)

// room code
data class RoomRs(
    @SerializedName("data")
    var `data`: RoomData
) : BaseResponse()

data class RoomData(
    @SerializedName("room_id")
    var roomId: String? = null
)


//Referral History
data class ReferralHistoryRs(
    @SerializedName("data")
    val `data`: ReferralData,
) : BaseResponse()

data class ReferralData(
    @SerializedName("refer_data")
    val referData: List<Refer>,
) : GeneralPagination()

@Parcelize
data class Refer(

//    @SerializedName("created_at")
//    val createdAt: String,
//    @SerializedName("profile_pic")
//    val profilePic: String,
//    @SerializedName("refer_code")
//    val referCode: String,
//    @SerializedName("refer_id")
//    val referId: String,
//    @SerializedName("referred_user_id")
//    val referredUserId: String,
//    @SerializedName("referring_user_id")
//    val referringUserId: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("profile_pic")
    val profilePic: String,
    @SerializedName("amount")
    val amount: String
) : Parcelable

// Banner
data class BannerRs(
    @SerializedName("data")
    val `data`: BannerData,
) : BaseResponse()

data class BannerData(
    @SerializedName("banners")
    val banners: List<Banner>,
) : GeneralPagination()

@Parcelize
data class Banner(
    @SerializedName("banner_enddate")
    val bannerEnddate: String,
    @SerializedName("banner_id")
    val bannerId: String,
    @SerializedName("banner_image")
    val bannerImage: String,
    @SerializedName("banner_startdate")
    val bannerStartdate: String,
    @SerializedName("banner_status")
    val bannerStatus: String,
    @SerializedName("banner_target_url")
    val bannerTargetUrl: String,
    @SerializedName("banner_title")
    val bannerTitle: String
) : Parcelable


// Trending Games
data class TrendingGamesRs(
    @SerializedName("data")
    val `data`: TrendingGamesData,
) : BaseResponse()

data class TrendingGamesData(
    @SerializedName("games")
    val games: List<TrendingGame>,
) : GeneralPagination()

@Parcelize
data class TrendingGame(
    @SerializedName("category_name")
    val categoryName: String? = "",
    @SerializedName("game_banner")
    val gameBanner: String,
    @SerializedName("game_description")
    val gameDescription: String,
    @SerializedName("game_featured")
    val gameFeatured: String,
    @SerializedName("game_id")
    val gameId: String,

    @SerializedName("game_key")
    val game_key: String,
    @SerializedName("game_image")
    val gameImage: String,
    @SerializedName("game_name")
    val gameName: String,
    @SerializedName("game_orientation")
    val gameOrientation: String,
    @SerializedName("game_status")
    val gameStatus: String,
    @SerializedName("game_trending")
    val gameTrending: String,
    @SerializedName("game_type")
    val gameType: String,
    @SerializedName("game_url")
    val gameUrl: String,
    @SerializedName("game_version")
    val gameVersion: String,
    @SerializedName("total_online_users")
    val totalOnlineUsers: String? = "0",
    @SerializedName("game_comming_soon")
    val gameCommingSoon: String,
    @SerializedName("game_tile")
    val gameTile: String,
    @SerializedName("game_tag")
    val gameTag: String
) : Parcelable

// Spin List
data class SpinListRs(
    @SerializedName("data")
    val `data`: SpinListData,
) : BaseResponse()

@Parcelize
data class SpinListData(
    @SerializedName("spins")
    val spins: List<SpinData>,
    @SerializedName("user_spin")
    val userSpin: String,
    @SerializedName("last_spin_time")
    val lastSpinTime: String
) : Parcelable

@Parcelize
data class SpinData(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("spin_amount")
    val spinAmount: String,
    @SerializedName("spin_id")
    val spinId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable

// Ip Response
data class IpResponse(
    @SerializedName("ip")
    val ip: String
)

//Check Withdrawal Transaction
// Tournament Join Before Check
data class CheckWithdrawTransactionRs(
    @SerializedName("data")
    val `data`: CheckWithdrawalTransactionData
) : BaseResponse()

@Parcelize
data class CheckWithdrawalTransactionData(
    @SerializedName("allowed_total_withdraw_amounts")
    val allowedTotalWithdrawAmounts: String,
    @SerializedName("allowed_total_withdraw_transactions")
    val allowedTotalWithdrawTransactions: String,
    @SerializedName("remain_total_withdraw_amount")
    val remainTotalWithdrawAmount: Int,
    @SerializedName("remain_total_withdraw_transactions")
    val remainTotalWithdrawTransactions: Int
) : Parcelable


// Success rate incident
data class SuccessRateIncidentRs(
    @SerializedName("data")
//    val `data`: SuccessRateIncidentData,
    val cASHFREE: List<SuccessRateCASHFREE>?
) : BaseResponse()

/*@Parcelize
data class SuccessRateIncidentData(
    @SerializedName("data")
    val cASHFREE: List<SuccessRateCASHFREE>?
) : Parcelable*/

@Parcelize
data class SuccessRateCASHFREE(
    @SerializedName("incident_from")
    val incidentFrom: String,
    @SerializedName("incident_message")
    val incidentMessage: String,
    @SerializedName("incident_status")
    val incidentStatus: String,
    @SerializedName("instruments_issuer")
    val instrumentsIssuer: String,
    @SerializedName("instruments_type")
    val instrumentsType: String
) : Parcelable





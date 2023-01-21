package com.bluboy.android.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DummyGames(
        var id: Int,
        var image: Int,
        var title: String,
        var linkUrl: String = "",
        var mode: String = ""

): Parcelable

data class DummyNotification(
        var id: Int,
        var title: String,
        var time: String,
        var label_header: String

        )


//data class BattleHistory(
//        @SerializedName("game_image")
//        var gameImage: String = "",
//        @SerializedName("room")
//        var room: String = "",
//        @SerializedName("tournament_entry_fees")
//        var tournamentEntryFees: String = "",
//        @SerializedName("tournament_id")
//        var tournamentId: String = "",
//        @SerializedName("tournament_name")
//        var tournamentName: String = "",
//        @SerializedName("tournament_player_per_room")
//        var tournamentPlayerPerRoom: String = "",
//        @SerializedName("tournament_start")
//        var tournamentStart: String = "",
//        @SerializedName("win_status")
//        var winStatus: String = ""
//)


data class LeaderBoard(
        @SerializedName("profile_pic")
        var profilePic: String = "",
        @SerializedName("rank")
        var rank: Int = 0,
        @SerializedName("score")
        var score: String = "",
        @SerializedName("user_name")
        var userName: String = "",
        @SerializedName("total_winning_amount")
        var totalWinningAmount: String = ""
)

@Parcelize
data class DummyExtraImage(
        @SerializedName("vExtraImage")
        var vExtraImage: Int? = null,
        var gameName: String = "",
        var linkUrl: String = "",
        var mode: String = ""
) : Parcelable



@Parcelize
data class DummyWithdrawData(
        var transferMode: String = ""): Parcelable

@Parcelize
data class DummyBanner(
        var image: Int? = null): Parcelable

@Parcelize
data class DummyFeaturedGames(
        var gameName: String = "",
        var featureImage: Int? = null,
        var featureBanner:Int? = null
        ): Parcelable

data class DummyMyRefer(
        var name: String = "",
        var point: String = "",

)

//data class FAQDetail(
//        @SerializedName("faq_answer")
//        var faqAnswer: String = "",
//        @SerializedName("faq_id")
//        var faqId: String = "",
//        @SerializedName("faq_question")
//        var faqQuestion: String = "",
//        var isExpanded: Boolean = false
//)

//data class HowToPlayContent(
//        val content_description: String,
//        val content_title: String,
//        val content_type: String,
//        val content_video_url: String,
//        val game_name: String,
//        val game_image: String,
//        val game_banner: String,
//        val status: String,
//        val game_orientation: String,
//        var isExpanded: Boolean = false
//)

//data class TransactionHistory(
//        @SerializedName("amount")
//        var amount: String = "",
//        @SerializedName("created_at")
//        var createdAt: String = "",
//        @SerializedName("history_type")
//        var historyType: String = "",
//        @SerializedName("histroy_message")
//        var histroyMessage: String = "",
//        @SerializedName("tournament_id")
//        var tournamentId: String = "",
//        @SerializedName("transaction_id")
//        var transactionId: String = "",
//        @SerializedName("transaction_type")
//        var transactionType: String = "",
//        @SerializedName("transaction_title")
//        var transactionTitle: String = ""
//)

//@Parcelize
//public data class Coupon(
//        @SerializedName("coupon_code")
//        var couponCode: String = "",
//        @SerializedName("coupon_name")
//        var couponName: String = "",
//        @SerializedName("coupon_description")
//        var couponDescription: String = ""
//): Parcelable



@Parcelize
data class BattleDymmy(
        @SerializedName("game_id")
        val gameId: String,
        @SerializedName("game_name")
        val gameName: String,
        @SerializedName("tournament_id")
        val tournamentId: String,

) : Parcelable

@Parcelize
data class Tournament(
        @SerializedName("game_id")
        val gameId: String,
        @SerializedName("game_name")
        val gameName: String,
        @SerializedName("tournament_id")
        val tournamentId: String,

) : Parcelable


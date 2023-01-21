package com.bluboy.android.presentation.utility

/**
 * Created by Parth Patibandha on 01,November,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

import android.content.Context
import android.content.Intent
import com.bluboy.android.data.models.Battle
import com.bluboy.android.data.models.CheckWithdrawalTransactionData
import com.bluboy.android.data.models.GamesData
import com.bluboy.android.presentation.game.activity.*
import com.bluboy.android.presentation.home.HomeActivity
import com.bluboy.android.presentation.home.NotificationActivity
import com.bluboy.android.presentation.home.activities.CMSActivity
import com.bluboy.android.presentation.home.activities.LeaderboardActivity
import com.bluboy.android.presentation.home.activities.ReferAndEarnActivity
import com.bluboy.android.presentation.loginsignup.*
import com.bluboy.android.presentation.loginsignup.activities.OtpVerificationActivity
import com.bluboy.android.presentation.profile.*
import com.bluboy.android.presentation.settings.ContactUsActivity
import com.bluboy.android.presentation.settings.FaqActivity
import com.bluboy.android.presentation.settings.HowToPlayActivity
import com.bluboy.android.presentation.settings.SettingActivity
import com.bluboy.android.presentation.transaction.TransactionActivity
import com.bluboy.android.presentation.wallet.*

class IntentHelper {

    companion object {

        fun getHomeScreenIntent(
            context: Context, isClearFlag: Boolean = false,
            openFromSignup: Boolean = false,
            wallet: Boolean = false,
            addMoney: Boolean = false,
            openFromSplash: Boolean = false,
            openFromSpin: Boolean = false
        ): Intent {
            return Intent(context, HomeActivity::class.java).also {
                it.putExtra(AppConstant.OPEN_FROM_SIGNUP, openFromSignup)
                it.putExtra(AppConstant.OPEN_WALLET_FROM_SPIN, openFromSpin)
                it.putExtra(AppConstant.OPEN_WALLET_FROM_PROFILE, wallet)
                it.putExtra(AppConstant.OPEN_ADDMONEY, addMoney)
                it.putExtra(AppConstant.OPEN_FROM_SPLASH, openFromSplash)
                if (isClearFlag) {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }

        fun getLoginScreenIntent(context: Context, isClearFlag: Boolean? = true): Intent {
            return Intent(context, LoginActivity::class.java).also {
                if (isClearFlag != null && isClearFlag) {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }

        fun getSignupScreenIntent(context: Context, isClearFlag: Boolean? = true): Intent {
            return Intent(context, SignupActivity::class.java).also {
                if (isClearFlag != null && isClearFlag) {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }

        /*fun getSpinScreenIntent(context: Context): Intent {
            return Intent(context, SpinWheelActivity::class.java)
        }*/

        fun getOtpScreenIntent(context: Context): Intent {
            return Intent(context, OtpVerificationActivity::class.java)
        }


        fun getNotificationScreenIntent(context: Context): Intent {
            return Intent(context, NotificationActivity::class.java)
        }


        fun getProfileScreenIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }

        fun getEditProfileScreenIntent(context: Context): Intent {
            return Intent(context, EditProfileActivity::class.java)
        }

        fun getSettingsScreenIntent(context: Context): Intent {
            return Intent(context, SettingActivity::class.java)
        }

        fun getContactUsScreenIntent(context: Context): Intent {
            return Intent(context, ContactUsActivity::class.java)
        }

        fun getFaqScreenIntent(context: Context): Intent {
            return Intent(context, FaqActivity::class.java)
        }

        fun getHowToPlayScreenIntent(context: Context): Intent {
            return Intent(context, HowToPlayActivity::class.java)
        }

        fun getTutorialScreenIntent(context: Context, gameName: String): Intent {
            return Intent(context, TutorialActivity::class.java).apply {
                this.putExtra(AppConstant.GAME_NAME, gameName)
            }
        }

        /*fun getWalletScreenIntent(context: Context): Intent {
            return Intent(context, WalletActivity::class.java)
        }*/

        fun getTransactionScreenIntent(context: Context): Intent {
            return Intent(context, TransactionActivity::class.java)
        }

        /*fun getAddCashScreenIntent(context: Context): Intent {
            return Intent(context, AddCashActivity::class.java)
        }*/

        fun getWithdrawScreenIntent(
            context: Context,
            checkWithdrawalTransactionData: CheckWithdrawalTransactionData
        ): Intent {
            return Intent(context, WithdrawActivity::class.java).apply {
                this.putExtra(AppConstant.checkWithdrawTransaction, checkWithdrawalTransactionData)
            }
        }

        fun getKYCIntent(context: Context, openFromProfile: Boolean? = false): Intent {
            return Intent(context, KycActivity::class.java).apply {
                this.putExtra(AppConstant.FROM_PROFILE, openFromProfile)
            }
        }


        fun getKYCDetailIntent(context: Context): Intent {
            return Intent(context, KycDetailActivity::class.java)
        }

        fun getKYCPANIntent(
            context: Context,
            openFromProfile: Boolean? = false
        ): Intent {
            return Intent(context, KycPANActivity::class.java).apply {
                this.putExtra(AppConstant.FROM_PROFILE, openFromProfile)
            }
        }

        fun getKYCAadhaarIntent(
            context: Context,
            openFromProfile: Boolean? = false
        ): Intent {
            return Intent(context, KycAadhaarActivity::class.java).apply {
                this.putExtra(AppConstant.FROM_PROFILE, openFromProfile)
            }
        }


        fun getWithdrawDetailScreenIntent(
            context: Context, transferMode: String, amount: String,
            checkWithdrawalTransactionData: CheckWithdrawalTransactionData, charge: String
        ): Intent {
            return Intent(context, WithdrawDetailActivity::class.java).apply {
                this.putExtra(AppConstant.TRANSFER_MODE, transferMode)
                this.putExtra(AppConstant.TRANSFER_AMOUNT, amount)
                this.putExtra(AppConstant.checkWithdrawTransaction, checkWithdrawalTransactionData)
                this.putExtra(AppConstant.CHARGE, charge)
            }
        }

        fun getBeneficiaryManageScreenIntent(context: Context): Intent {
            return Intent(context, MyBeneficiaryActivity::class.java)
        }

        fun getPAymetOptionScreenIntent(
            context: Context,
            amount: String,
            promocode: String
        ): Intent {
            return Intent(context, PaymentOptionsActivity::class.java).apply {
                this.putExtra(AppConstant.TRANSFER_AMOUNT, amount)
                this.putExtra(AppConstant.TRANSFER_PROMOCODE, promocode)

            }
        }

        fun getAddBeneficiaryScreenIntent(
            context: Context,
            type: String,
            transferMode: String
        ): Intent {
            return Intent(context, BeneficiaryAddActivity::class.java).apply {
                this.putExtra(AppConstant.BENEFICIARY_TYPE, type)
                this.putExtra(AppConstant.TRANSFER_MODE, transferMode)
            }
        }

        fun getGameScreenIntent(context: Context, game: GamesData): Intent {
            return Intent(context, GameActivity::class.java).apply {
                this.putExtra(AppConstant.GAME, game)
            }
        }

        fun getGameHistoryScreenIntent(context: Context, game: GamesData): Intent {
            return Intent(context, GameHistoryActivity::class.java).apply {
                this.putExtra(AppConstant.GAME, game)
            }
        }

        fun getGameHistoryFromHomeScreenIntent(context: Context): Intent {
            return Intent(context, GameHistoryFromHomeActivity::class.java).apply {
//                this.putExtra(AppConstant.GAME,game)
            }
        }

        fun getLeaderboardFromHomeScreenIntent(context: Context): Intent {
            return Intent(context, LeaderboardActivity::class.java).apply {
            }
        }

        fun getReferAndEarnFromHomeScreenIntent(context: Context): Intent {
            return Intent(context, ReferAndEarnActivity::class.java).apply {
            }
        }

        fun getGameDetailsScreenIntent(context: Context, game: GamesData, battle: Battle): Intent {
            return Intent(context, GameDetailActivity::class.java).apply {
                this.putExtra(AppConstant.GAME, game)
                this.putExtra(AppConstant.BATTLE, battle)
            }
        }

        fun getCMSScreenIntent(context: Context, data: String): Intent {
            return Intent(context, CMSActivity::class.java).apply {
                this.putExtra(AppConstant.cmsType, data)
            }
        }

    }
}
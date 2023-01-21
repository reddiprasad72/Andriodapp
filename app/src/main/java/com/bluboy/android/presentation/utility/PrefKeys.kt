package com.bluboy.android.presentation.utility

/**
 * Created by Parth Patibandha on 01,November,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

import com.bluboy.android.data.models.User
import com.bluboy.android.data.models.VariableData
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs

class PrefKeys {
    companion object {
        const val PermissionLocation = "permission_location"
        const val UserProfile = "userprofile"
        const val VarSetting = "varSettings"
        const val IsLogin = "IsLogin"
        const val fruitTutorial = "fruitTutorial"
        const val runnerTutorial = "runnerTutorial"
        const val candyCrushTutorial = "candyCrushTutorial"
        const val AuthKey = "AuthKey"
        const val DeviceToken = "DeviceToken"
        const val PushTokenKey = "push_token_key"

        const val Latitude = "latitude"
        const val Longitude = "longitude"
        const val AndroidId = AppConstant.vDeviceUniqueId

        fun isUserLoggedIn(): Boolean {
            return getAuthKey().isNotEmpty()
        }

        fun clearPref() {
            Prefs.clear()
        }

        var candyCrushTutorialFlag: String
            get() = if (Prefs.contains(candyCrushTutorial)) Prefs.getString(
                candyCrushTutorial,
                "true"
            ) else "true"
            set(toShow) {
                val editor = Prefs.edit()
                editor.putString(candyCrushTutorial, toShow)
                editor.apply()
            }

        var runnerTutorialFlag: String
            get() = if (Prefs.contains(runnerTutorial)) Prefs.getString(
                runnerTutorial,
                "true"
            ) else "true"
            set(toShow) {
                val editor = Prefs.edit()
                editor.putString(runnerTutorial, toShow)
                editor.apply()
            }

        var fruitTutorialFlag: String
            get() = if (Prefs.contains(fruitTutorial)) Prefs.getString(
                fruitTutorial,
                "true"
            ) else "true"
            set(toShow) {
                val editor = Prefs.edit()
                editor.putString(fruitTutorial, toShow)
                editor.apply()
            }

        var touchEnable: Boolean
            get() = if (Prefs.contains("touchEnable")) Prefs.getBoolean(
                "touchEnable",true
            ) else true
            set(flag) {
                val editor = Prefs.edit()
                editor.putBoolean("touchEnable", flag)
                editor.apply()
            }


        fun getAuthKey(): String {
            return Prefs.getString(AuthKey, "")
        }

        fun setUser(user: User) {
            Prefs.putString(PrefKeys.UserProfile, Gson().toJson(user))
            Prefs.putString(PrefKeys.AuthKey, user?.authKey)
        }

        fun getUserCommon(): User? {
            val userJson = Prefs.getString(UserProfile, "")
            return if (null != userJson) Gson().fromJson<User>(userJson, User::class.java) else null
        }

        fun getVariableData(): VariableData? {
            var userJson = Prefs.getString(VarSetting, "")
            return if (null != userJson) Gson().fromJson<VariableData>(
                userJson,
                VariableData::class.java
            ) else null
        }

        fun setVariableData(customer: VariableData) {
            val userJson = Gson().toJson(customer)
            Prefs.putString(VarSetting, userJson)
        }
    }
}
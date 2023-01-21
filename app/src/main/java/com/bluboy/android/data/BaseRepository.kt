package com.bluboy.android.data

import android.content.Intent
import android.util.Log
import com.bluboy.android.MyApplication
import com.bluboy.android.data.models.BaseResponse
import com.bluboy.android.domain.exceptions.MyException
import com.bluboy.android.presentation.loginsignup.LoginActivity
import com.bluboy.android.presentation.utility.PrefKeys
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepository {

    suspend fun <R> either(service: suspend () -> R): Either<MyException, R> {

        return try {
            val response = service.invoke()
            if (response is BaseResponse) {
                if (response.status == 3 &&
                    Prefs.getString(PrefKeys.AuthKey, "").isNotBlank()
                ) {
                    //Logger.d("Api Unauthorized >>>>>")
                    if (MyApplication.context != null) {
                        Prefs.putString(PrefKeys.AuthKey, "")
                        Prefs.putString(PrefKeys.UserProfile, "")
                        val i = Intent(MyApplication.context, LoginActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        MyApplication.context!!.startActivity(i)
                    } else {
                    }
                }
            }
            Either.Right(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Either.Left(transformException(e))
        }
    }

    private fun transformException(e: Exception): MyException {
        var errorBd = ""
        if (e is HttpException) {
            Log.w("HttpException","HttpException  ${e.code()}")
            when (e.code()) {
                422,
                502 -> return MyException.JsonException(e)
                500 -> return MyException.InternetConnectionException(e)
                404 -> return MyException.ServerNotAvailableException(e)
                429 -> return MyException.TooManyAttemptException(e)
                426 -> {
                    return MyException.AppUpgradeVersionException(e)
                }

                else -> return MyException.UnknownException(e)
            }
        } else {
            if (e is CancellationException) {
                return MyException.NetworkCallCancelException(e)
            } else if (e is ConnectException || e is UnknownHostException || e is SocketTimeoutException || e is SocketException) {
                return MyException.InternetConnectionException(e)
            } else if (e is IllegalStateException || e is JsonSyntaxException || e is MalformedJsonException) {
                return MyException.JsonException(e)
            }
        }
        return MyException.UnknownException(e)
    }


}
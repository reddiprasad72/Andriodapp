package com.bluboy.android.domain.exceptions

/**
 * Created by Parth Patibandha on 01,November,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

import kotlin.Exception

/**
 * Created Kotlin Sealed class for convert kotlin exception to custom exception
 *
 * @constructor
 * t: Throwable
 *
 * @param t - Kotlin Throwable
 */
sealed class MyException(t: Throwable) : Exception() {
    class InternetConnectionException(e: Exception) : MyException(e)
    class JsonException(e: Exception) : MyException(e)
//    class UnauthorizedException(e : Exception) : MyException(e)
    class NetworkCallCancelException(e: Exception) : MyException(e)
    class ServerNotAvailableException(e: Exception) : MyException(e)
    class AppUpgradeVersionException(e: Exception) : MyException(e)
    class TooManyAttemptException(e: Exception) : MyException(e)
    class DatabaseException(e : Exception) : MyException(e)
    class UnknownException(e: Exception) : MyException(e)
}
package com.bluboy.android.presentation.utility

import android.text.TextUtils
import android.util.Patterns
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

open class StringUtils {

    companion object{

        val EMPTY_STRING = ""
        val SPACE_STRING = " "
        val COLON = " : "
        val DATE_SEPARATOR = "/"
        val TIME_SEPARATOR = ":"
        val COMMA = ","
        val PERCENTAGE = "%"
        val NULL_STRING = "null"
        val COUNTRY_CODE_INDIA = "+91"
        val CURRENCY = "$"
        private val MAX_VALID_DIGITS_PHONE = 10
        private val MIN_VALID_DIGITS_PHONE = 10
        private val MAX_VALID_AGE = 90
        private val MIN_VALID_AGE = 21
        /**
         * Checks if string is empty or not
         *
         * @param value string to check
         * @return true if empty or null, false otherwise
         */
        fun isEmpty(value: String?): Boolean {
            var value = value
            try {
                value = value!!.trim { it <= ' ' }
            } catch (e: Exception) {
            }

            return value == null || value.length == 0
        }

        /**
         * Checks if string is valid and having at least minimum number of characters or not
         *
         * @param value string to check
         * @param min   minimum length of validity
         * @return true if valid, false otherwise
         */
        fun isValid(value: String?, min: Int): Boolean {
            var value = value
            try {
                value = value!!.trim { it <= ' ' }
            } catch (e: Exception) {
            }

            return value != null && value.length >= min
        }

        /**
         * Checks if string is valid or not
         *
         * @param value string to check
         * @return true if valid, false otherwise
         */
        open fun isValid(value: String): Boolean {
            return !isEmpty(value) && !value.equals(EMPTY_STRING, ignoreCase = true) && !value.equals(
                NULL_STRING,
                ignoreCase = true
            )
        }

        fun isValidFilter(value: String): Boolean {
            return !isEmpty(value) && !value.equals(EMPTY_STRING, ignoreCase = true) && !value.equals(
                NULL_STRING,
                ignoreCase = true
            ) && value != "0"
        }

        /**
         * Checks if email id is in valid format or not
         *
         * @param emailId email id to check
         * @return true if valid, false otherwise
         */
        fun isValidEmail(emailId: String): Boolean {
            if (!isValid(emailId)) {
                return false
            }
            /*
            String emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")";
    */
            val pattern = Patterns.EMAIL_ADDRESS

            return pattern.matcher(emailId).matches()
        }

        /**
         * Checks if phone number is in valid format and not having max digits
         *
         * @param phone phone number to check
         * @return true if valid, false otherwise
         */
        fun isValidPhone(phone: String?): Boolean {
            var phone = phone
            try {
                phone = phone!!.trim { it <= ' ' }
            } catch (e: Exception) {
            }

            return phone != null && isValid(phone) && phone.length <= MAX_VALID_DIGITS_PHONE && phone.length >= MIN_VALID_DIGITS_PHONE
        }




        fun equals(s1: String, s2: String): Boolean {
            return isValid(s1) && isValid(s2) && s1.equals(s2, ignoreCase = true)
        }

        fun formatDate(day: Int, month: Int, year: Int): String {
            return String.format(
                "%02d$DATE_SEPARATOR%02d$DATE_SEPARATOR%02d",
                day, month, year
            )
            /*
            return
                    String.valueOf(month) + DATE_SEPARATOR
                            + String.valueOf(day) + DATE_SEPARATOR
                            + String.valueOf(year)
                    ;
    */
        }

        fun formatTime(hour: Int, minute: Int, second: Int): String {
            return String.format("%02d$TIME_SEPARATOR%02d$TIME_SEPARATOR%02d", hour, minute, second)
        }

        fun trimLastComma(str: String?): String? {
            var str = str
            if (str != null && str.length >= 2 && str[str.length - 1] == ',') {
                str = str.substring(0, str.length - 1)
            }
            return str
        }


        fun trimAddress(address: CharSequence): String? {
            val csAdd = address.toString().split(COMMA.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var shortenedAddress = EMPTY_STRING
            for (i in csAdd.indices) {
                if (i < 3) {
                    shortenedAddress += csAdd[i] + COMMA
                }
            }
            return removeLastCharacter(shortenedAddress.trim { it <= ' ' })
        }

        fun removeLastCharacter(str: String?): String? {
            var str = str
            if (str != null && str.length > 0) {
                str = str.substring(0, str.length - 1)
            }
            return str
        }

        fun isEnteredPhoneNumber(input: String): Boolean {
            return TextUtils.isDigitsOnly(input)
        }

        fun formattedTimeFromMillis(difference: Long): String {
            val totalSeconds = difference / 1000
            val seconds = (totalSeconds % 60).toInt()
            val minutes = (totalSeconds / 60 % 60).toInt()
            val hours = (totalSeconds / (60 * 60)).toInt()

            return String.format("%sh %sm %ss", hours, minutes, seconds)
        }

        fun isValidAge(age: String?): Boolean {
            var age = age
            try {
                age = age!!.trim { it <= ' ' }
            } catch (e: Exception) {
            }

            return age != null && isValid(age) && Integer.parseInt(age) <= MAX_VALID_AGE && Integer.parseInt(age) >= MIN_VALID_AGE
        }

        fun getAge(year: Int, month: Int, day: Int): String {
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()

            dob.set(year, month, day)

            var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            val ageInt = Integer.valueOf(age)

            return ageInt.toString()
        }


        fun isValidIFSCode(str: String?): Boolean {
            val regex = "^[A-Z]{4}0[A-Z0-9]{6}$"
            val p: Pattern = Pattern.compile(regex)
            if (str == null) { return false }
            val m: Matcher = p.matcher(str)
            return m.matches()
        }
    }

}
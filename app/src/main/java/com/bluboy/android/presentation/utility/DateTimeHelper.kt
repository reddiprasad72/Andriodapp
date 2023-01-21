package com.bluboy.android.presentation.utility

import android.content.Context
import com.bluboy.android.R
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeHelper {

//    companion object {
private val SECOND_MILLIS = 1000

    private val SECOND: Long = 1000
    private val MINUTE = SECOND * 60
    private val HOUR = MINUTE * 60
    private val DAY = HOUR * 24
    private val WEEK = DAY * 7
    private val MONTH = DAY * 30
    private val YEAR = MONTH * 12


    const val YYYYMMDD = "yyyy-MM-dd"
        const val DDMMYYYY = "dd-MM-yyyy"
        const val HHMMA = "hh:mm a"
        const val DDMMMYYYYHHMMA = "dd/MM/yy hh:mm a"


        /**
         * For Datetime convertor
         *
         * @param time in milles
         */
        fun String.getFormattedDatetime(inputFormat: String, outputFormat: String): String {
            var outputString = this
            try {
                val inputFormatter = DateTimeFormat.forPattern(inputFormat)
                val inputDatetime = inputFormatter.parseDateTime(this)
                val outputFormatter: DateTimeFormatter = DateTimeFormat.forPattern(outputFormat)
                outputString = outputFormatter.print(inputDatetime)
            } catch (e: Exception) {
                e.printStackTrace()
                outputString = this
            }
            return outputString
        }

        fun String.getFormattedDatetime(outputFormat: String): String {
            var longtime: Long = 0L
            if (this.isNotEmpty()) {
                this.toLong().apply {
                    longtime = this
                }
            }
            if (longtime < 1000000000000L) {
                longtime *= 1000
            }
            val dateTime = DateTime(longtime)
            return dateTime.toString(outputFormat)
        }


        fun convertLongToTime(time: Long): String {
            val date = Date(time * 1000)
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            return format.format(date)
        }



    fun getPrettyTimeBeforeEnd(context: Context,datetime: String?): String {
        try {
//            val date = Formatter.getMyCalendar(datetime!!, inFormat, isUtc)!!.time

            //Initialize both calendar with set time
            val current = Calendar.getInstance()

            //DebugLog.i("InsertDate::::"+ Formatter.format(calendarDate, DD_MMMM_YYYY_hh_mm_aa))
            //DebugLog.i("CurrentDate::::"+ Formatter.format(current, DD_MMMM_YYYY_hh_mm_aa))

            val difference = (datetime!!.toLong()*1000) - current.timeInMillis

            val year = difference / YEAR
            val month = difference / MONTH
            val weeks = difference / WEEK
            val day = difference / DAY
            val hour = difference / HOUR
            val minute = difference / MINUTE
            val second = difference / SECOND

            var time: String = ""
//            if (year > 0)
//                time = String.format("%d %s", year, if (year > 1) context.getString(R.string.years_ago) else context.getString(R.string.year_ago))
//            if (month > 0)
//                time += String.format("%d %s", month, if (month > 1) context.getString(R.string.months_ago) else context.getString(R.string.month_ago))
//            if (weeks > 0)
//                time += String.format("%d %s", weeks, if (weeks > 1) context.getString(R.string.weeks_ago) else context.getString(R.string.week_ago))
            if (day > 0)
                time = String.format("%d%s", day, if (day > 1) context.getString(R.string.days_ago) else context.getString(R.string.day_ago))
            if (hour > 0)
                if (day > 0) {
                    if (minute > 60){
//                        hour = minute/60
                    }
                    time += String.format(" : %d%s", hour, if (hour > 1) context.getString(R.string.hours_ago) else context.getString(R.string.hour_ago))
                }else{
                    time += String.format("%d%s", hour, if (hour > 1) context.getString(R.string.hours_ago) else context.getString(R.string.hour_ago))
                }
            if (minute > 0) {
                if (hour > 0) {
                    time += String.format(" : %d%s", minute, if (minute > 1) context.getString(R.string.minutes_ago) else context.getString(R.string.minute_ago))
                }else{
                    time += String.format(" 00:%d", minute)
                }
            }
//            if (second > 0)
//                time += String.format("%d %s", second, if (second > 1) context.getString(R.string.seconds_ago) else context.getString(R.string.second_ago))

            return if (time != "") String.format("%s", time) else context.getString(R.string.just_now)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getPrettyTimeAfterStart(context: Context,datetime: String?): String {
        try {
//            val date = Formatter.getMyCalendar(datetime!!, inFormat, isUtc)!!.time

            //Initialize both calendar with set time
            val current = Calendar.getInstance()

            //DebugLog.i("InsertDate::::"+ Formatter.format(calendarDate, DD_MMMM_YYYY_hh_mm_aa))
            //DebugLog.i("CurrentDate::::"+ Formatter.format(current, DD_MMMM_YYYY_hh_mm_aa))

            val difference =  current.timeInMillis - (datetime!!.toLong()*1000)

            val year = difference / YEAR
            val month = difference / MONTH
            val weeks = difference / WEEK
            val day = difference / DAY
            var hour = difference / HOUR
            var minute = difference / MINUTE
            val second = difference / SECOND

            var time: String = ""
//            if (year > 0)
//                time = String.format("%d %s", year, if (year > 1) context.getString(R.string.years_ago) else context.getString(R.string.year_ago))
//            if (month > 0)
//                time += String.format("%d %s", month, if (month > 1) context.getString(R.string.months_ago) else context.getString(R.string.month_ago))
//            if (weeks > 0)
//                time += String.format("%d %s", weeks, if (weeks > 1) context.getString(R.string.weeks_ago) else context.getString(R.string.week_ago))
            if (day > 0)
                time = String.format("%d%s", day, if (day > 1) context.getString(R.string.days_ago) else context.getString(R.string.day_ago))
            if (hour > 0)
//                if (minute > 60){
//                    hour += minute/60
//                }
                if (day > 0) {
                    time += String.format(" : %d%s", hour, if (hour > 1) context.getString(R.string.hours_ago) else context.getString(R.string.hour_ago))
                }else{
                    time += String.format("%d%s", hour, if (hour > 1) context.getString(R.string.hours_ago) else context.getString(R.string.hour_ago))
                }
            if (minute > 0) {
//                if (minute > 60){
//                    minute += minute%60
//                }
                if (hour > 0) {
                    time += String.format(" : %d%s", minute, if (minute > 1) context.getString(R.string.minutes_ago) else context.getString(R.string.minute_ago))
                }else{
                    time += String.format(" 00:%d", minute)
                }
            }
//            if (second > 0)
//                time += String.format("%d %s", second, if (second > 1) context.getString(R.string.seconds_ago) else context.getString(R.string.second_ago))

            return if (time != "") String.format("%s", time) else context.getString(R.string.just_now)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


        fun dateTimeConvertServertToLocal(timeConvert: String, input: String, output: String): String {
            val timeFormater = SimpleDateFormat(input, Locale.ENGLISH)
            try {
//            timeFormater.timeZone = TimeZone.getTimeZone(TIME_ZONE)
                val time: Date = timeFormater.parse(timeConvert)
                val timeFormaterSecond =
                    SimpleDateFormat(output) //HH for hour of the day (0 - 23)
                timeFormaterSecond.timeZone = TimeZone.getDefault()
                return timeFormaterSecond.format(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        fun convertDate(
            dateTimeStr: String,
            input: String = "yyyy-MM-dd'T'HH:mm:ss",
            output: String = "dd/MM/yy HH:mm"
        ): String {
            val timeFormatter = SimpleDateFormat(input)

            return try {
                val time = timeFormatter.parse(dateTimeStr)
                SimpleDateFormat(output).format(time)

            } catch (e: ParseException) {
                e.printStackTrace()
                ""
            }
        }
//    }
}
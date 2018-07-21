package com.github.oryanmat.trellowidget.util

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {
    internal val DATE_PARSE_ERROR = "Bad date"

    internal val apiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    internal val widgetFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    internal val activityFormat = SimpleDateFormat("MMM dd 'at' HH:mm a", Locale.getDefault())

    init {
        // it appears that trello returns dates in UTC time zone
        apiFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun parseDate(date: String) = parse(date, widgetFormat)

    fun parseDateTime(date: String) = parse(date, activityFormat)

    private fun parse(date: String, format: DateFormat) : String = try {
        format.format(apiFormat.parse(date))
    } catch (e: ParseException) {
        DATE_PARSE_ERROR
    }
}
package com.example.studyplannerapp.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimeUtils {

    companion object {

        /**
         * Parses a date string into a timestamp (in milliseconds).
         * If parsing fails, returns a default value representing tomorrow's date.
         */
        fun parseStringToDate(date: String): Long {

            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

            val defaultDate = getDefaultTomorrowDateInString()
            val formattedDefaultDate = dateFormat.parse(defaultDate)!!.time

            return try {
                dateFormat.parse(date)?.time ?: formattedDefaultDate
            } catch (e: ParseException) {
                formattedDefaultDate
            }
        }

        // Converts a timestamp (in milliseconds) to a date string in "MMM dd, yyyy" format.
        private fun formatDateToString(deadline: Long): String {
            val deadlineFormat = SimpleDateFormat("MMM dd, yyyy" , Locale.getDefault())
            return deadlineFormat.format(deadline)
        }

        // Returns tomorrow's date as a string in "MMM dd, yyyy" format
        private fun getDefaultTomorrowDateInString(): String {

            val tomorrowDateLong = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR,1)
            }.timeInMillis

            val tomorrowDateString = formatDateToString(tomorrowDateLong)
            return tomorrowDateString
        }

        // Returns tomorrow's date as a timestamp (in milliseconds)
        private fun getDefaultTomorrowDateInMillis(): Long {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            return calendar.timeInMillis
        }
    }
}
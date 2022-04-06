package com.webkul.mobikul.odoo.helper

class CalendarUtil {

    companion object {
        private val daysInEnglish =
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        private val monthsInEnglish = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        private val daysInBahasa =
            listOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
        private val monthsInBahasa = listOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        )


        fun getDateInBahasa(date: String): String {
            val daysTranslated: String = translateDaysToBahasa(date)
            val monthsTranslated: String = translateMonthsToBahasa(daysTranslated)
            return monthsTranslated
        }

        private fun translateMonthsToBahasa(date: String): String {
            var result = ""
            for (index in monthsInEnglish.indices) {
                val month = monthsInEnglish[index]
                if (date.contains(month)) {
                    result = date.replace(monthsInEnglish[index], monthsInBahasa[index])
                }
            }
            return result
        }

        private fun translateDaysToBahasa(date: String): String {
            var result = ""
            for (index in daysInEnglish.indices) {
                val day = daysInEnglish[index]
                if (date.contains(day)) {
                    result = date.replace(daysInEnglish[index], daysInBahasa[index])
                }
            }
            return result
        }
    }

}
package com.example.todoapp.data.util

import java.text.SimpleDateFormat
import java.util.*

class IDGenerator {
    fun generateTodoId(length: Int = 8): String {
        val uuid = generateRandomUUID(length)
        val currentDateTime = getCurrentDateTime()

        return replaceNonAlphaNumChars(uuid).plus("-")
            .plus(replaceNonAlphaNumChars(currentDateTime))
    }

    private fun generateRandomUUID(length: Int) : String {
        return UUID.randomUUID().toString().substring(0, length)
    }

    private fun getCurrentDateTime() : String {
        val format = SimpleDateFormat(
            "yy-M-d h:m:s",
            Locale.getDefault()
        )
        return format.format(Date())
    }

    private fun replaceNonAlphaNumChars(string: String) : String {
        val nonAlphaNumChars = "[^a-zA-Z0-9]".toRegex()
        return string.replace(nonAlphaNumChars, "")
    }

}
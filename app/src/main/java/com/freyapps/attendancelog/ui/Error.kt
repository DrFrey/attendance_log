package com.freyapps.attendancelog.ui

sealed class Error(message: String): Exception(message) {
    object LastGroupError : Error("Can't delete last group")
    data class UnknownError(val description: String) : Error(description)
}

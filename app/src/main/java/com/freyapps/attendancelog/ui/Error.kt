package com.freyapps.attendancelog.ui

sealed class Error {
    object LastGroupError : Error()
    data class UnknownError(val description: String) : Error()
}

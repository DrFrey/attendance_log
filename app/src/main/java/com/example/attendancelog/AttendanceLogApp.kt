package com.example.attendancelog

import android.app.Application
import com.example.attendancelog.data.AppDatabase
import com.example.attendancelog.data.StudentRepository

class AttendanceLogApp: Application() {

    companion object {
        lateinit var instance: AttendanceLogApp

        private val database by lazy {
            AppDatabase.getDatabase(instance)
        }

        val repository by  lazy {
            StudentRepository(database.studentDao())
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
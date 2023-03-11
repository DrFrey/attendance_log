package com.freyapps.attendancelog

import android.app.Application
import com.freyapps.attendancelog.data.AppDatabase
import com.freyapps.attendancelog.data.GroupRepository
import com.freyapps.attendancelog.data.StudentRepository

class AttendanceLogApp: Application() {

    companion object {
        lateinit var instance: AttendanceLogApp

        private val database by lazy {
            AppDatabase.getDatabase(instance)
        }

        val studentRepository by  lazy {
            StudentRepository(database.studentDao())
        }
        val groupRepository by lazy {
            GroupRepository(database.groupDao())
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
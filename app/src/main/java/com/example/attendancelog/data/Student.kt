package com.example.attendancelog.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "status") val status: Int = 1
) {
    enum class Status(val value: Int) {
        PRESENT(1),
        ILL(2),
        ABSCENT(3)
    }
}

package com.freyapps.attendancelog.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "groups", indices = [Index(value = ["name"],
    unique = true)])
data class Group(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String = ""
    ) {
    override fun toString(): String {
        return name
    }
}

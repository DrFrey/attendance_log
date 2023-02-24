package com.freyapps.attendancelog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface GroupDao {
    @Insert
    suspend fun addGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)
}
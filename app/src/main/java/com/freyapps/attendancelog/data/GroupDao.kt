package com.freyapps.attendancelog.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroupDao {
    @Insert
    suspend fun addGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)

    @Query("SELECT * FROM groups ORDER BY name")
    fun getAllGroups(): LiveData<List<Group>>
}
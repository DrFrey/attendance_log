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

    @Query("DELETE FROM groups WHERE id = :id")
    suspend fun deleteGroupById(id: Int)

    @Query("SELECT * FROM groups ORDER BY name")
    fun getAllGroups(): LiveData<List<Group>>

    @Query("SELECT * FROM groups WHERE name = :name")
    suspend fun getGroupByName(name: String): Group
}
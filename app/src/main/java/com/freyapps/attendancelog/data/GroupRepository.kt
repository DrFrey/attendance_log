package com.freyapps.attendancelog.data

class GroupRepository(
    private val groupDao: GroupDao
) {
    fun getAllGroups() = groupDao.getAllGroups()

    suspend fun addGroup(group: Group) = groupDao.addGroup(group)
    suspend fun updateGroup(group: Group) = groupDao.updateGroup(group)
    suspend fun deleteGroup(group: Group) = groupDao.deleteGroup(group)
}
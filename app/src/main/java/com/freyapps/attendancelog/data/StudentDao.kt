package com.freyapps.attendancelog.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert
    suspend fun addStudent(student: Student)

    @Insert
    suspend fun addAll(students: List<Student>)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("DELETE FROM students")
    suspend fun deleteAll()

    @Query("SELECT * FROM students ORDER BY last_name ASC")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE status = 2 AND group_id = :groupId ORDER BY last_name ASC")
    fun getAllSickByGroup(groupId: Int): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE status = 3 AND group_id = :groupId ORDER BY last_name ASC")
    fun getAllAbsentByGroup(groupId: Int): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE group_id = :groupId ORDER BY last_name ASC")
    fun getAllStudentsByGroup(groupId: Int): Flow<List<Student>>
}
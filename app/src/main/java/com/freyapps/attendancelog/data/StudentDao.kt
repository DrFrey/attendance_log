package com.freyapps.attendancelog.data

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Query("DELETE FROM students WHERE group_id = :groupId")
    suspend fun deleteStudentsByGroup(groupId: Int)

    @Query("SELECT * FROM students ORDER BY last_name ASC")
    fun getAllStudents(): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE status = 2 AND group_id = :groupId ORDER BY last_name ASC")
    fun getAllSickByGroup(groupId: Int): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE status = 3 AND group_id = :groupId ORDER BY last_name ASC")
    fun getAllAbsentByGroup(groupId: Int): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE group_id = :groupId ORDER BY last_name ASC")
    fun getAllStudentsByGroup(groupId: Int): LiveData<List<Student>>
}
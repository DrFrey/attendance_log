package com.example.attendancelog.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.attendancelog.data.Student

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
    fun getAllStudents(): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE status = 2 ORDER BY last_name ASC")
    fun getAllSick(): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE status = 3 ORDER BY last_name ASC")
    fun getAllAbsent(): LiveData<List<Student>>
}
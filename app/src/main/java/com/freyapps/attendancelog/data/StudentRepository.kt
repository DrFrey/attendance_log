package com.freyapps.attendancelog.data

class StudentRepository(
    private val studentDao: StudentDao
) {

    fun getAllSickByGroup(groupId: Int) = studentDao.getAllSickByGroup(groupId)

    fun getAllAbsentByGroup(groupId: Int) = studentDao.getAllAbsentByGroup(groupId)

    fun getAllStudentsByGroup(groupId: Int) = studentDao.getAllStudentsByGroup(groupId)

    suspend fun updateStudent(student: Student) = studentDao.updateStudent(student)

    suspend fun deleteStudent(student: Student) = studentDao.deleteStudent(student)

    suspend fun addStudent(student: Student) = studentDao.addStudent(student)

}
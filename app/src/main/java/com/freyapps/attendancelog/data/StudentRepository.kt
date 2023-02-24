package com.freyapps.attendancelog.data

class StudentRepository(
    private val studentDao: StudentDao
) {

    fun getAllStudents() = studentDao.getAllStudents()
    fun getAllSick() = studentDao.getAllSick()
    fun getAllAbsent() = studentDao.getAllAbsent()

    fun getAllStudentsByGroup(groupId: Int) = studentDao.getAllStudentsByGroup(groupId)

    suspend fun updateStudent(student: Student) = studentDao.updateStudent(student)

    suspend fun deleteStudent(student: Student) = studentDao.deleteStudent(student)

    suspend fun addStudent(student: Student) = studentDao.addStudent(student)

}
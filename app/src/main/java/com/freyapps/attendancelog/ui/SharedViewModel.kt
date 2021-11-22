package com.freyapps.attendancelog.ui

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.data.StudentRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SharedViewModel(private val studentRepository: StudentRepository) :
    ViewModel() {

    val allStudents: LiveData<List<Student>> = studentRepository.getAllStudents()
    val allSick: LiveData<List<Student>> = studentRepository.getAllSick()
    val allAbsent: LiveData<List<Student>> = studentRepository.getAllAbsent()

    @SuppressLint("NewApi")
    val today: String = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

    fun addStudent(student: Student) = viewModelScope.launch {
        studentRepository.addStudent(student)
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        studentRepository.updateStudent(student)
    }

    fun deleteStudent(student: Student) = viewModelScope.launch {
        studentRepository.deleteStudent(student)
    }

    class SharedViewModelFactory(
        private val studentRepository: StudentRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
                return SharedViewModel(studentRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
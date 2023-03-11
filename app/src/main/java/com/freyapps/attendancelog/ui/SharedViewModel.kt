package com.freyapps.attendancelog.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.freyapps.attendancelog.data.Group
import com.freyapps.attendancelog.data.GroupRepository
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.data.StudentRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SharedViewModel(
     private val studentRepository: StudentRepository,
     private val groupRepository: GroupRepository
) :
    ViewModel() {

    var currentGroup = 1

    val studentsInCurrentGroup: LiveData<List<Student>>
        get() {
            Log.d("TAG", "studentsInCurrentGroup get triggered")
            return studentRepository.getAllStudentsByGroup(currentGroup)
        }

    val allSick: LiveData<List<Student>> = studentRepository.getAllSickByGroup(currentGroup)
    val allAbsent: LiveData<List<Student>> = studentRepository.getAllAbsentByGroup(currentGroup)

    val allGroups: LiveData<List<Group>> = groupRepository.getAllGroups()

    @SuppressLint("NewApi")
    val today: String = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

    init {
        Log.d("TAG", "viewmodel init triggered")
    }

    fun addStudent(student: Student) = viewModelScope.launch {
        studentRepository.addStudent(student)
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        studentRepository.updateStudent(student)
    }

    fun deleteStudent(student: Student) = viewModelScope.launch {
        studentRepository.deleteStudent(student)
    }

    fun addGroup(group: Group) = viewModelScope.launch {
        groupRepository.addGroup(group)
    }

    fun updateGroup(group: Group) = viewModelScope.launch {
        groupRepository.updateGroup(group)
    }

    fun deleteGroup(group: Group) = viewModelScope.launch {
        groupRepository.deleteGroup(group)
    }

    class SharedViewModelFactory(
        private val studentRepository: StudentRepository,
        private val groupRepository: GroupRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
                return SharedViewModel(studentRepository, groupRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
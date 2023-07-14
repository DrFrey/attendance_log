package com.freyapps.attendancelog.ui

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.freyapps.attendancelog.data.Group
import com.freyapps.attendancelog.data.GroupRepository
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.data.StudentRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SharedViewModel(
     private val studentRepository: StudentRepository,
     private val groupRepository: GroupRepository
) : ViewModel() {

    private val _allGroups = groupRepository.getAllGroups()
    val allGroups: LiveData<List<Group>>
        get() = _allGroups

    private val _currentGroup = MutableLiveData<Group?>()
    val currentGroup: LiveData<Group?>
        get() = _currentGroup

    val studentsInCurrentGroup: LiveData<List<Student>> = currentGroup.switchMap { group ->
        group?.let {
            studentRepository.getAllStudentsByGroup(it.id)
        }
    }

    val allSick: LiveData<List<Student>> = currentGroup.switchMap { group ->
        group?.let {
            studentRepository.getAllSickByGroup(it.id)
        }
    }

    val allAbsent: LiveData<List<Student>> = currentGroup.switchMap { group ->
        group?.let {
            studentRepository.getAllAbsentByGroup(it.id)
        }
    }

    private val _error = MutableLiveData<Error?>()
    val error: LiveData<Error?>
        get() = _error

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

    fun addGroup(group: Group) = viewModelScope.launch {
        groupRepository.addGroup(group)
        setCurrentGroup(groupRepository.getGroupByName(group.name).id)
    }

    fun deleteCurrentGroup() = currentGroup.value?.let { deleteGroup(it) }

    private fun deleteGroup(group: Group) {
        allGroups.value?.let {
            if (it.size > 1) {
                viewModelScope.launch {
                    studentRepository.deleteStudentsByGroup(group.id)
                    groupRepository.deleteGroup(group)
                    allGroups.value?.let { refreshed ->
                        setCurrentGroup(refreshed.first().id)
                    }
                }
            } else {
                _error.value = Error.LastGroupError
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun setCurrentGroup(id: Int) = runBlocking {
        _currentGroup.value = groupRepository.getGroupById(id)
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
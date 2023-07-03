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
) : ViewModel() {

    val allGroups: LiveData<List<Group>> = groupRepository.getAllGroups()

    val currentGroup = MutableLiveData<Int>()

    val studentsInCurrentGroup: LiveData<List<Student>> = currentGroup.switchMap {
        studentRepository.getAllStudentsByGroup(it)
    }

    val allSick: LiveData<List<Student>> = currentGroup.switchMap {
        studentRepository.getAllSickByGroup(it)
    }

    val allAbsent: LiveData<List<Student>> = currentGroup.switchMap {
        studentRepository.getAllAbsentByGroup(it)
    }

    private val _error = MutableLiveData<Error>()
    val error: LiveData<Error>
        get() = _error

    @SuppressLint("NewApi")
    val today: String = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

    init {
        Log.d("TAG", "viewmodel init triggered")
        setCurrentGroup(allGroups.value?.first()?.id ?: 1)
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
        setCurrentGroup(groupRepository.getGroupByName(group.name).id)
        Log.d("--->", "in addGroup: current group is ${currentGroup.value}")
    }

    fun updateGroup(group: Group) = viewModelScope.launch {
        groupRepository.updateGroup(group)
    }

    fun deleteCurrentGroup() = currentGroup.value?.let { deleteGroupById(it) }

    private fun deleteGroupById(id: Int) = viewModelScope.launch {
        allGroups.value?.let {
            if (it.size > 1) {
                studentRepository.deleteStudentsByGroup(id)
                groupRepository.deleteGroupById(id)
                setCurrentGroup(it.first().id)
                Log.d("--->", "in deleteGroupById: current group is ${currentGroup.value}")
            } else {
                _error.postValue(Error.LastGroupError)
            }
        }
    }

    fun setCurrentGroup(id: Int) {
        currentGroup.value = id
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
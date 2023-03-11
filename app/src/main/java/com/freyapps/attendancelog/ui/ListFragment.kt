package com.freyapps.attendancelog.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.freyapps.attendancelog.AttendanceLogApp
import com.freyapps.attendancelog.R
import com.freyapps.attendancelog.data.Group
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.databinding.FragmentListBinding

class ListFragment : Fragment(), StudentAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private val viewModel: SharedViewModel by activityViewModels {
        SharedViewModel.SharedViewModelFactory(
            AttendanceLogApp.studentRepository,
            AttendanceLogApp.groupRepository
        )
    }
    private lateinit var binding: FragmentListBinding
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var groupsAdapter: ArrayAdapter<Group>

    private var sickStudents: List<Student> = listOf()
    private var absentStudents: List<Student> = listOf()
    private var groups: List<Group> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        setUpViews()
        subscribeToViewModel()

        return binding.root
    }

    private fun setUpViews() {
        with(binding) {
            lifecycleOwner = this@ListFragment
            studentAdapter = StudentAdapter(this@ListFragment)
            studentsList.adapter = studentAdapter

            tvDate.text = viewModel.today

            groupsAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item
            )
            groupsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            groupSpinner.adapter = groupsAdapter
            groupSpinner.onItemSelectedListener = this@ListFragment

            btnSend.setOnClickListener {
                val sick = sickStudents.joinToString(prefix = " (", postfix = ")")  {
                    it.firstName + " " + it.lastName
                }

                val absent = absentStudents.joinToString(prefix = " (", postfix = ")") {
                    it.firstName + " " + it.lastName
                }

                val message = getString(
                    R.string.message_template,
                    viewModel.today,
                    sickStudents.size,
                    if (sickStudents.isEmpty()) "" else sick,
                    absentStudents.size,
                    if (absentStudents.isEmpty()) "" else absent
                )

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            btnGroupEdit.setOnClickListener {
                val editGroupsDialog = EditGroupsDialog(viewModel)
                editGroupsDialog.show(childFragmentManager, "editGroups")
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.studentsInCurrentGroup.observe(viewLifecycleOwner) {
            studentAdapter.submitList(it)
        }
        viewModel.allSick.observe(viewLifecycleOwner) {
            sickStudents = it
        }
        viewModel.allAbsent.observe(viewLifecycleOwner) {
            absentStudents = it
        }
        viewModel.allGroups.observe(viewLifecycleOwner) {
            groups = it
            groupsAdapter.clear()
            groups.forEach { group ->
                groupsAdapter.add(group)
            }
            groupsAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(item: Student, newStatus: Int) {
        val newStudent = Student(
            id = item.id,
            firstName = item.firstName,
            lastName = item.lastName,
            status = newStatus,
            groupId = item.groupId
        )
        viewModel.updateStudent(newStudent)
        studentAdapter.notifyItemChanged(studentAdapter.currentList.indexOf(item))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.selectedItem as Group

        viewModel.currentGroup = item.id

        viewModel.studentsInCurrentGroup.removeObservers(this)
        viewModel.studentsInCurrentGroup.observe(viewLifecycleOwner) {
            studentAdapter.submitList(it)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("TAG","onNothingSelected triggered")
    }
}
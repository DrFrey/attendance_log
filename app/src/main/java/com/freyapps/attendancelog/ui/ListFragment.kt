package com.freyapps.attendancelog.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.freyapps.attendancelog.AttendanceLogApp
import com.freyapps.attendancelog.R
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.databinding.FragmentListBinding

class ListFragment : Fragment(), StudentAdapter.OnItemClickListener {

    private val viewModel: SharedViewModel by viewModels {
        SharedViewModel.SharedViewModelFactory(
            AttendanceLogApp.repository
        )
    }
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: StudentAdapter

    private var sickStudents: List<Student> = listOf()
    private var absentStudents: List<Student> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        adapter = StudentAdapter(this)

        subscribeToViewModel()
        setUpViews()

        return binding.root
    }

    private fun setUpViews() {
        with(binding) {
            studentsList.adapter = adapter
            tvDate.text = viewModel.today

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
        }

    }

    private fun subscribeToViewModel() {
        viewModel.allStudents.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.allSick.observe(viewLifecycleOwner, {
            sickStudents = it
        })
        viewModel.allAbsent.observe(viewLifecycleOwner, {
            absentStudents = it
        })
    }

    override fun onItemClick(item: Student, newStatus: Int) {
        val newStudent = Student(
            id = item.id,
            firstName = item.firstName,
            lastName = item.lastName,
            status = newStatus
        )
        viewModel.updateStudent(newStudent)
        adapter.notifyItemChanged(adapter.currentList.indexOf(item))
    }
}
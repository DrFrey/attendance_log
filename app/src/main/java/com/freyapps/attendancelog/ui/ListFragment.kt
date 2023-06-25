package com.freyapps.attendancelog.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.Menu.NONE
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.freyapps.attendancelog.AttendanceLogApp
import com.freyapps.attendancelog.R
import com.freyapps.attendancelog.data.Group
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.databinding.AddGroupBottomSheetDialogBinding
import com.freyapps.attendancelog.databinding.AddStudentBottomSheetDialogBinding
import com.freyapps.attendancelog.databinding.FragmentListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ListFragment : Fragment(), StudentAdapter.OnItemClickListener {

    private val viewModel: SharedViewModel by activityViewModels {
        SharedViewModel.SharedViewModelFactory(
            AttendanceLogApp.studentRepository,
            AttendanceLogApp.groupRepository
        )
    }
    private lateinit var binding: FragmentListBinding
    private lateinit var studentAdapter: StudentAdapter

    private var groupsList: List<Group> = listOf()
    private var sickStudents: List<Student> = listOf()
    private var absentStudents: List<Student> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        subscribeToViewModel()
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                menu.clear()
                menu.add(NONE, R.string.edit, NONE, R.string.edit)
                menu.findItem(R.string.edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                menu.add(NONE, R.string.add_group, NONE, R.string.add_group)
                menu.add(NONE, R.string.delete_group, NONE, R.string.delete_group)
                for (group in groupsList) {
                    Log.d("--->", "adding group : ${group.name}")
                    menu.add(NONE, group.id, NONE, group.name)
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.string.edit -> {
                        requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.action_listFragment_to_editListFragment)
                        true
                    }
                    R.string.add_group -> {
                        Log.d("--->", "add group clicked")
                        onAddClick()
                        true
                    }
                    R.string.delete_group -> {
                        Log.d("--->", "delete group clicked")
                        true
                    }
                    R.string.manage_groups -> {
                        Log.d("--->", "manage group clicked")
                        true
                    }
                    else -> {
                        Log.d("--->", "clicked group with id ${menuItem.itemId}")
                        viewModel.setCurrentGroup(menuItem.itemId)
                        true
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpViews() {
        with(binding) {
            lifecycleOwner = this@ListFragment
            studentAdapter = StudentAdapter(this@ListFragment)
            studentsList.adapter = studentAdapter

            tvDate.text = viewModel.today

            btnSend.setOnClickListener {
                val sick = sickStudents.joinToString(prefix = " (", postfix = ")") {
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
        viewModel.allGroups.observe(viewLifecycleOwner) {
            groupsList = it
        }
        viewModel.studentsInCurrentGroup.observe(viewLifecycleOwner) {
            studentAdapter.submitList(it)
        }
        viewModel.allSick.observe(viewLifecycleOwner) {
            sickStudents = it
        }
        viewModel.allAbsent.observe(viewLifecycleOwner) {
            absentStudents = it
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

    private fun onAddClick() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val dialogBinding =
            AddGroupBottomSheetDialogBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(dialogBinding.root)

        with(dialogBinding) {
            btnSave.setOnClickListener {
                when {
                    etName.text.isNullOrEmpty() -> {
                        etName.error = getString(R.string.error_mandatory_field)
                    }
                    else -> {
                        val newGroup = Group(
                            name = etName.text.toString()
                        )
                        viewModel.addGroup(newGroup)
                        viewModel.setCurrentGroup(newGroup.id)
                        bottomSheetDialog.dismiss()
                    }
                }
            }

            btnCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }

        bottomSheetDialog.show()
    }
}
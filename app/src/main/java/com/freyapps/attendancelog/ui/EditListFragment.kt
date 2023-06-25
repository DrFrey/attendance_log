package com.freyapps.attendancelog.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.freyapps.attendancelog.AttendanceLogApp
import com.freyapps.attendancelog.R
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.databinding.AddStudentBottomSheetDialogBinding
import com.freyapps.attendancelog.databinding.FragmentEditListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class EditListFragment : Fragment(), EditAdapter.OnDeleteClickListener {

    private val viewModel: SharedViewModel by activityViewModels {
        SharedViewModel.SharedViewModelFactory(
            AttendanceLogApp.studentRepository,
            AttendanceLogApp.groupRepository
        )
    }

    private lateinit var editListAdapter: EditAdapter
    private lateinit var binding: FragmentEditListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        subscribeToViewModel()
    }

    private fun setUpViews() {
        with(binding) {
            fabBack.setOnClickListener {
                findNavController().navigate(R.id.action_editListFragment_to_listFragment)
            }

            fabAdd.setOnClickListener {
                onAddClick()
            }

            lifecycleOwner = this@EditListFragment
            editListAdapter = EditAdapter(this@EditListFragment)
            studentsList.adapter = editListAdapter
        }
    }

    private fun subscribeToViewModel() {
        viewModel.studentsInCurrentGroup.observe(viewLifecycleOwner) {
            editListAdapter.submitList(it)
        }

    }
    private fun onAddClick() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val dialogBinding =
            AddStudentBottomSheetDialogBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(dialogBinding.root)

        with(dialogBinding) {
            btnSave.setOnClickListener {
                when {
                    etFirstName.text.isNullOrEmpty() -> {
                        etFirstName.error = getString(R.string.error_mandatory_field)
                    }
                    etLastName.text.isNullOrEmpty() -> {
                        etLastName.error = getString(R.string.error_mandatory_field)
                    }
                    else -> {
                        viewModel.addStudent(
                            Student(
                                firstName = etFirstName.text.toString(),
                                lastName = etLastName.text.toString(),
                                groupId = viewModel.currentGroup.value ?: 1
                            )
                        )
                        editListAdapter.notifyDataSetChanged()
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

    override fun onDeleteClick(item: Student) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.are_you_sure))
            .setContentText(getString(R.string.delete_student))
            .setConfirmText(getString(R.string.yes))
            .setConfirmClickListener { sDialog ->
                viewModel.deleteStudent(item)
                sDialog.dismissWithAnimation()
            }
            .showCancelButton(true)
            .setCancelText(getString(R.string.no))
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }
}
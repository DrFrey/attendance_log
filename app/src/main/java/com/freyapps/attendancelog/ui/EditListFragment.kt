package com.freyapps.attendancelog.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.freyapps.attendancelog.AttendanceLogApp
import com.freyapps.attendancelog.R
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.databinding.AddStudentBottomSheetDialogBinding
import com.freyapps.attendancelog.databinding.FragmentEditListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class EditListFragment : Fragment(), EditAdapter.OnDeleteClickListener {

    private val viewModel: SharedViewModel by viewModels {
        SharedViewModel.SharedViewModelFactory(
            AttendanceLogApp.repository
        )
    }

    private lateinit var adapter: EditAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_editList).isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditListBinding.inflate(inflater, container, false)

        with(binding) {
            fabBack.setOnClickListener {
                findNavController().navigate(R.id.action_editListFragment_to_listFragment)
            }

            fabAdd.setOnClickListener {
                onAddClick()
            }

            lifecycleOwner = this@EditListFragment
            adapter = EditAdapter(this@EditListFragment)
            studentsList.adapter = adapter
        }

        viewModel.allStudents.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        return binding.root
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
                                lastName = etLastName.text.toString()
                            )
                        )
                        bottomSheetDialog.dismiss()
                        adapter.notifyDataSetChanged()
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
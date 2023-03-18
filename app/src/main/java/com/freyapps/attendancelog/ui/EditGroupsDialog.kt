package com.freyapps.attendancelog.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.freyapps.attendancelog.R
import com.freyapps.attendancelog.data.Group
import com.freyapps.attendancelog.databinding.EditGroupsDialogBinding
import kotlinx.coroutines.launch

class EditGroupsDialog(private val viewModel: SharedViewModel) :
    DialogFragment(), EditGroupsAdapter.OnClickListener {

    private lateinit var adapter: EditGroupsAdapter
    private var listSize: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        adapter = EditGroupsAdapter(this@EditGroupsDialog)
        val binding = EditGroupsDialogBinding.inflate(LayoutInflater.from(requireContext()))

        with(binding) {
            groupsList.adapter = adapter
            btnAddGroup.setOnClickListener {
                val newGroup = Group(name = "New Group")
                viewModel.addGroup(newGroup)
            }
        }

        lifecycleScope.launch {
            viewModel.allGroups().collect {
                adapter.submitList(it)
                listSize = it.size
            }
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDeleteClick(item: Group) {
        if (listSize == 1) {
            Toast.makeText(requireContext(), getString(R.string.last_group_delete_error), Toast.LENGTH_SHORT).show()
        } else {
            viewModel.deleteGroup(item)
        }
    }

    override fun onConfirmEditClick(item: Group) {
        viewModel.updateGroup(item)
    }
}
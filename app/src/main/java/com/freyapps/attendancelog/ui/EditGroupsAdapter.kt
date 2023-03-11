package com.freyapps.attendancelog.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.freyapps.attendancelog.data.Group
import com.freyapps.attendancelog.databinding.ItemGroupBinding
import com.freyapps.attendancelog.ui.EditGroupsAdapter.ViewHolder

class EditGroupsAdapter(
    val listener: OnClickListener
    ) : ListAdapter<Group, ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Group) {
                with(binding) {
                    group = item

                    btnDeleteGroup.setOnClickListener {
                        listener.onDeleteClick(item)
                    }

                    btnConfirmEditGroup.setOnClickListener {
                        val newGroup = Group(id = item.id, name = etName.text.toString())
                        listener.onConfirmEditClick(newGroup)
                    }

                    var initialAssignment = true
                    etName.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                            //do nothing
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            //do nothing
                        }

                        override fun afterTextChanged(s: Editable?) {
                            if (!initialAssignment) {
                                btnConfirmEditGroup.visibility = ImageView.VISIBLE
                            }
                            initialAssignment = false
                            if (s?.length == 0) {
                                btnConfirmEditGroup.visibility = ImageView.GONE
                            }
                        }
                    })
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Group>() {
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface OnClickListener {
        fun onDeleteClick(item: Group)
        fun onConfirmEditClick(item: Group)
    }
}
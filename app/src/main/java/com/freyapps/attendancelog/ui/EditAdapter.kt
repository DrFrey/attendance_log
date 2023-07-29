package com.freyapps.attendancelog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.freyapps.attendancelog.data.Student
import com.freyapps.attendancelog.databinding.ItemEditBinding

class EditAdapter(val listener: OnDeleteClickListener) :
    ListAdapter<Student, EditAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemEditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Student) {
            with(binding) {
                student = item
                btnDelete.setOnClickListener {
                    listener.onDeleteClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemEditBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(item: Student)
    }
}
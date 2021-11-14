package com.example.attendancelog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancelog.R
import com.example.attendancelog.data.Student
import com.example.attendancelog.databinding.ItemStudentBinding


class StudentAdapter(val listener: OnItemClickListener) :
    ListAdapter<Student, StudentAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Student) {
            with(binding) {
                student = item
                when (item.status) {
                    1 -> {
                        setToPresent()
                    }
                    2 -> {
                        setToSick()
                    }
                    3 -> {
                        setToAbsent()
                    }
                    else -> {
                        statusPresent.setImageResource(R.drawable.check_gray)
                        statusSick.setImageResource(R.drawable.ill_gray)
                        statusAbsent.setImageResource(R.drawable.wrong_gray)
                    }
                }

                statusPresent.setOnClickListener {
                    listener.onItemClick(item, 1)
                }
                statusSick.setOnClickListener {
                    listener.onItemClick(item, 2)
                }
                statusAbsent.setOnClickListener {
                    listener.onItemClick(item, 3)
                }
            }
        }

        private fun ItemStudentBinding.setToAbsent() {
            statusAbsent.setImageResource(R.drawable.wrong)
            statusSick.setImageResource(R.drawable.ill_gray)
            statusPresent.setImageResource(R.drawable.check_gray)
        }

        private fun ItemStudentBinding.setToSick() {
            statusSick.setImageResource(R.drawable.ill)
            statusPresent.setImageResource(R.drawable.check_gray)
            statusAbsent.setImageResource(R.drawable.wrong_gray)
        }

        private fun ItemStudentBinding.setToPresent() {
            statusPresent.setImageResource(R.drawable.check)
            statusSick.setImageResource(R.drawable.ill_gray)
            statusAbsent.setImageResource(R.drawable.wrong_gray)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

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

    interface OnItemClickListener {
        fun onItemClick(item: Student, newStatus: Int)
    }
}
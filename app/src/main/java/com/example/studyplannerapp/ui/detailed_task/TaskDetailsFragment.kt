package com.example.studyplannerapp.ui.detailed_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentEditTaskBinding
import com.example.studyplannerapp.databinding.FragmentTaskDetailsBinding
import com.example.studyplannerapp.ui.SingleTaskViewModel
import com.example.studyplannerapp.ui.TasksViewModel

class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
    //private val viewModel: TasksViewModel by activityViewModels()
    private val taskViewModel: SingleTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getLong("id")?.let {
            taskViewModel.setId(it)
        }

        taskViewModel.task.observe(viewLifecycleOwner) {
            binding.taskTitle.text = it.title
            binding.taskDescription.text = it.description
            binding.taskType.text = it.type
            binding.taskCourse.text = it.course
            binding.taskDate.text = it.getFormattedDeadline()
            binding.taskSlider.value = it.progressPercentage.toFloat()
            binding.taskSlider.isEnabled = false

            Glide.with(requireContext()).load(it.image).circleCrop()
                .into(binding.taskImage)
        }
    }
}
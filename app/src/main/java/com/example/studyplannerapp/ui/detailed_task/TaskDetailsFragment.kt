package com.example.studyplannerapp.ui.detailed_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentTaskDetailsBinding
import com.example.studyplannerapp.ui.SingleTaskViewModel

class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
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

        taskViewModel.editedTask.observe(viewLifecycleOwner) {
            binding.taskTitle.text = it.title
            binding.taskDescription.text = it.description
            binding.taskType.text = it.type
            binding.taskCourse.text = it.course
            binding.taskDate.text = it.getFormattedDeadline()
            binding.taskSlider.apply {
                isEnabled = false
                thumbWidth = 0
                thumbTrackGapSize = 0
                value = it.progressPercentage.toFloat()
            }

            val progress = it.progressPercentage.toString() + getString(R.string.precentage_sign)
            binding.progressTV.text = progress

            if(it.image == null) {
                binding.taskImage.setImageResource(setImageByType(it.type))
            }
            else {
                val requestOptions = RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(30))

                Glide.with(binding.root)
                    .load(it.image)
                    .apply(requestOptions)
                    .into(binding.taskImage)
            }
        }
    }
    private fun setImageByType(type: String): Int {
        return when (type) {
            getString(R.string.assignment) -> R.drawable.icon_assignment
            getString(R.string.exam) -> R.drawable.icon_exam
            getString(R.string.lesson) -> R.drawable.icon_video
            getString(R.string.project) -> R.drawable.icon_project
            else -> R.drawable.icon_logo
        }
    }
}
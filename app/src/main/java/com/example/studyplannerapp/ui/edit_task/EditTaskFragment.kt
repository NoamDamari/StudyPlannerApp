package com.example.studyplannerapp.ui.edit_task

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentEditTaskBinding
import com.example.studyplannerapp.ui.SingleTaskViewModel
import com.example.studyplannerapp.utils.AlarmUtils
import com.example.studyplannerapp.utils.DateTimeUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar

class EditTaskFragment : Fragment() {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var pickImageClicked: Boolean = false
    private val taskViewModel: SingleTaskViewModel by viewModels()

    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                // Apply transformations
                val requestOptions = RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(30))

                // Load image with Glide if pickImageBtnEdit was clicked
                if (pickImageClicked) {
                    Glide.with(binding.root)
                        .load(uri)
                        .apply(requestOptions)
                        .into(binding.taskImageEdit)

                    // Take persistable URI permission
                    requireActivity().contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    imageUri = it
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getLong("id")?.let {
            if(it != taskViewModel.selectedTask.value?.id){
                taskViewModel.setId(it)
            }
        }

        val datePicker: MaterialDatePicker<Long> by lazy {
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())

            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTheme(R.style.datePickerStyle)
                .build()
        }

        binding.taskDateEditBtn.setOnClickListener {
            datePicker.show(parentFragmentManager, "datePickerDialog")
        }

        // Click listener to set selected date
        datePicker.addOnPositiveButtonClickListener {
            binding.taskDateEditBtn.text = datePicker.headerText
        }

        binding.pickImageBtnEdit.setOnClickListener {
            pickImageClicked = true
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        taskViewModel.editedTask.observe(viewLifecycleOwner) { task ->

            binding.taskTitleEdit.setText(task.title)
            binding.taskDescriptionEdit.setText(task.description)
            binding.taskTypeEdit.setText(task.type , false)
            binding.taskCourseEdit.setText(task.course)
            binding.taskSliderEdit.value = task.progressPercentage.toFloat()
            binding.taskDateEditBtn.text = task.getFormattedDeadline()

            if(task.image == null) {
                binding.taskImageEdit.setImageResource(setImageByType(task.type))
            }
            else {
                val requestOptions = RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(30))

                Glide.with(binding.root)
                    .load(task.image)
                    .apply(requestOptions)
                    .into(binding.taskImageEdit)
            }
        }

        binding.saveChangesBtn.setOnClickListener {

            val currentDeadline = taskViewModel.selectedTask.value?.deadline

            taskViewModel.editedTask.value?.apply {
                title = binding.taskTitleEdit.text.toString()
                description = binding.taskDescriptionEdit.text.toString()
                deadline = DateTimeUtils.parseStringToDate(binding.taskDateEditBtn.text.toString())
                type =  binding.taskTypeEdit.text.toString()
                course = binding.taskCourseEdit.text.toString()
                progressPercentage = binding.taskSliderEdit.value.toInt()
                image = imageUri?.toString() ?: this.image
            }

            taskViewModel.editedTask.value?.let { task ->

                taskViewModel.updateTask(task)

                // Update alarm notification if task deadline has changed
                if(task.deadline != currentDeadline) {
                    AlarmUtils.setAlarmNotification(requireContext(),task)

                    // Displaying notification update message for testing
                        Snackbar.make(
                            requireView(),
                            "notification updated",
                            Snackbar.LENGTH_LONG
                        ).show()
                }
            }
            findNavController().navigate(R.id.action_editTaskFragment_to_taskListFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        taskViewModel.updateTemporaryTaskFromUI(
            title = binding.taskTitleEdit.text.toString(),
            description = binding.taskDescriptionEdit.text.toString(),
            deadline = DateTimeUtils.parseStringToDate(binding.taskDateEditBtn.text.toString()),
            type = binding.taskTypeEdit.text.toString(),
            course = binding.taskCourseEdit.text.toString(),
            progressPercentage = binding.taskSliderEdit.value.toInt(),
            image = imageUri?.toString()
        )
    }

    private fun setImageByType(type: String): Int {
        return when (type) {
            "Assignment" -> R.drawable.icon_assignment
            "Exam" -> R.drawable.icon_exam
            "Lesson" -> R.drawable.icon_video
            "Project" -> R.drawable.icon_project
            else -> R.drawable.icon_logo
        }
    }
}


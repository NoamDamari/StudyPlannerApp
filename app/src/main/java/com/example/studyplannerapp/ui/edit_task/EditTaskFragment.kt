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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentEditTaskBinding
import com.example.studyplannerapp.ui.TasksViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTaskFragment : Fragment() {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var pickImageClicked: Boolean = false
    private val viewModel: TasksViewModel by activityViewModels()

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

        viewModel.selectedTask.observe(viewLifecycleOwner) { task ->

            binding.taskTitleEdit.setText(task.title)
            binding.taskDescriptionEdit.setText(task.description)
            binding.taskTypeEdit.setText(task.type)
            binding.taskCourseEdit.setText(task.course)
            binding.taskSliderEdit.value = task.progressPercentage.toFloat()
            binding.taskDateEditBtn.text = task.getFormattedDeadline()

            // Load image using Glide, fallback to task.image if available
            task.image?.let { imageUrl ->
                // Apply transformations
                val requestOptions = RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(30))

                Glide.with(binding.root)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(binding.taskImageEdit)
            }
        }

        binding.saveChangesBtn.setOnClickListener {

            viewModel.selectedTask.value?.apply {
                title = binding.taskTitleEdit.text.toString()
                description = binding.taskDescriptionEdit.text.toString()
                deadline = parseStringToDate(binding.taskDateEditBtn.text.toString())
                type =  binding.taskTypeEdit.text.toString()
                course = binding.taskCourseEdit.text.toString()
                progressPercentage = binding.taskSliderEdit.value.toInt()
                image = imageUri?.toString() ?: this.image
            }

            viewModel.selectedTask.value?.let { task -> viewModel.updateTask(task) }
            findNavController().navigate(R.id.action_editTaskFragment_to_taskListFragment)
        }
    }

    /**
     * Parses a date string into a timestamp (in milliseconds).
     * If parsing fails, returns a default value representing tomorrow's date.
     */
    private fun parseStringToDate(date: String) : Long {

        val defaultValue = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR,1)
        }.timeInMillis

        return try {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            dateFormat.parse(date)?.time ?: defaultValue
        } catch (e: ParseException) {
            defaultValue
        }
    }
}


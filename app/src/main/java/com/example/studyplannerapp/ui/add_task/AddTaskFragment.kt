package com.example.studyplannerapp.ui.add_task

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.studyplannerapp.R
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.databinding.FragmentAddTaskBinding
import com.example.studyplannerapp.ui.TasksViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private val viewModel: TasksViewModel by activityViewModels()

    // Launcher for picking an image from the device storage
    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                // Apply transformations
                val requestOptions = RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(30))

                // Load image with Glide
                Glide.with(binding.root)
                    .load(uri)
                    .apply(requestOptions)
                    .into(binding.taskImage)

                // Take persistable URI permission
                requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                imageUri = it
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Date picker dialog initialization
        val datePicker: MaterialDatePicker<Long> by lazy {
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())

            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTheme(R.style.datePickerStyle)
                .build()
        }

        // Show date picker dialog when button clicked
        binding.dateDialogBtn.setOnClickListener {
            datePicker.show(parentFragmentManager , "datePickerDialog")
        }

        // Click listener to set selected date
        datePicker.addOnPositiveButtonClickListener {
            binding.dateDialogBtn.text = datePicker.headerText
        }

        // Click listener to launch image picker
        binding.pickImageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        // Finish button click listener
        binding.finishBtn.setOnClickListener {

            val task = Task(
                title = binding.taskTitleET.text.toString(),
                description = binding.taskDescription.text.toString(),
                deadline =  parseStringToDate(binding.dateDialogBtn.text.toString()),
                type = binding.taskTypeTF.text.toString(),
                course = binding.courseET.text.toString(),
                progressPercentage = binding.progressSlider.value.toInt(),
                image = imageUri?.toString(),
            )
            viewModel.addTask(task)

            findNavController().navigate(R.id.action_addTaskFragment_to_taskListFragment)
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
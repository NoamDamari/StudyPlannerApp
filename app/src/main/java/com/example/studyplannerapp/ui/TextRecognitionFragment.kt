package com.example.studyplannerapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentTextRecognitionBinding
import com.example.studyplannerapp.utils.CameraPermissionHandler
import com.example.studyplannerapp.utils.PermissionHandler
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class TextRecognitionFragment : Fragment() {

    private var _binding: FragmentTextRecognitionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SingleTaskViewModel by activityViewModels()

    // Launcher for picking an image from the device storage
    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                // Apply transformations
                val requestOptions = RequestOptions()
                    .transform(CenterInside(), RoundedCorners(30))

                // Load image with Glide
                Glide.with(binding.root)
                    .load(uri)
                    .apply(requestOptions)
                    .into(binding.descriptionImage)

                // Take persistable URI permission
                requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)

                viewModel.setTempTaskImage(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTextRecognitionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener for the upload button
        binding.uploadBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        // Set click listener for the recognize text button
        binding.recognizeTextBtn.setOnClickListener {
             recognizeText()
        }

        // Set the recognized text as description
        binding.setDescriptionBtn.setOnClickListener {
            val description = binding.recognizedDescription.text.toString()
            val bundle = Bundle()
            bundle.putString("description", description)
            findNavController().navigate(R.id.action_textRecognitionFragment_to_addTaskFragment , bundle)
        }

        // Observe changes in temporaryTask LiveData and update UI accordingly
        viewModel.temporaryTask.observe(viewLifecycleOwner) {tempTask->
            binding.recognizedDescription.setText(tempTask.description)
            binding.descriptionImage.setImageURI(tempTask.image?.toUri())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Recognize text from the selected image
    */
    private fun recognizeText() {
        viewModel.temporaryTask.value?.image?.toUri()?.let { uri ->
            val image = InputImage.fromFilePath(requireContext(), uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    extractRecognizedText(visionText.text)
                }
                .addOnFailureListener { _ ->
                    showSnackbar("Text Recognition Failed")
                }
        } ?: run {
            showSnackbar("No image selected")
        }
    }

    /**
     * Display the recognized text
     */
    private fun extractRecognizedText(text: String){
        if (text.isBlank()) {
            showSnackbar("Text is blank")
        } else {
            viewModel.setTempTaskDescription(text)
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
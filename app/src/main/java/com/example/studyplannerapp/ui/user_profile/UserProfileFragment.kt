package com.example.studyplannerapp.ui.user_profile

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
import com.example.studyplannerapp.data.models.User
import com.example.studyplannerapp.databinding.FragmentUserProfileBinding
import com.example.studyplannerapp.ui.UserViewModel
import com.example.studyplannerapp.utils.autoCleared
import com.google.android.material.snackbar.Snackbar


class UserProfileFragment : Fragment() {

    private var binding: FragmentUserProfileBinding by autoCleared()
    private val userViewModel: UserViewModel by viewModels()
    private var imageUri: Uri? = null
    private var pickImageClicked: Boolean = false

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
                        .into(binding.userProfileImageView)

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
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.setProfileImageBtn.setOnClickListener {
            pickImageClicked = true
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        userViewModel.user.observe(viewLifecycleOwner) { user ->

            binding.userNameET.setText(user?.userName)
            binding.userEmailET.setText(user?.email)
            binding.userPhoneET.setText(user?.phone)

            if(user?.profileImage != null) {
                val requestOptions = RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(30))

                Glide.with(binding.root)
                    .load(user.profileImage)
                    .apply(requestOptions)
                    .into(binding.userProfileImageView)
            }
        }

        binding.setProfileBtn.setOnClickListener {
            val updatedUser = userViewModel.user.value?.copy(
                userName = binding.userNameET.text.toString(),
                email = binding.userEmailET.text.toString(),
                phone = binding.userPhoneET.text.toString(),
                profileImage = imageUri?.toString() ?: userViewModel.user.value?.profileImage
            ) ?: User(
                userName = binding.userNameET.text.toString(),
                email = binding.userEmailET.text.toString(),
                phone = binding.userPhoneET.text.toString(),
                profileImage = imageUri?.toString()
            )

            if (updatedUser.userName?.isEmpty() == true) {
                binding.userNameET.error = "User name is required"
            } else {
                userViewModel.updateUser(updatedUser)
                Snackbar.make(requireView(), "User updated", Snackbar.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_userProfileFragment_to_taskListFragment)
            }
        }
    }
}



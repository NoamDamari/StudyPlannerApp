package com.example.studyplannerapp.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentIntroBinding
import com.example.studyplannerapp.ui.UserViewModel
import com.example.studyplannerapp.utils.autoCleared

class IntroFragment : Fragment() {

    private var binding: FragmentIntroBinding by autoCleared()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(R.id.action_introFragment_to_taskListFragment)
            } else {
                val slideInAnimation = AnimationUtils.loadAnimation( requireContext(),R.anim.top_to_buttom)
                binding.introLayout.startAnimation(slideInAnimation)

                binding.getStartedBtn.setOnClickListener {
                    findNavController().navigate(R.id.action_introFragment_to_userProfileFragment)
                }
            }
        }
    }
}
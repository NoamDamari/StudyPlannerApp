package com.example.studyplannerapp.ui.all_tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {

    private var _binding : FragmentTaskListBinding? = null
    private  val binding get() = _binding!!
    private lateinit var tasksAdapter: TasksAdapter
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksAdapter = TasksAdapter()

        binding.tasksRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksAdapter
        }

        viewModel.getTasks().observe(viewLifecycleOwner, Observer { tasks ->
            tasksAdapter.setTasksList(tasks)
        })

        Glide.with(binding.root).load(R.drawable.icon_user).into(binding.userImageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
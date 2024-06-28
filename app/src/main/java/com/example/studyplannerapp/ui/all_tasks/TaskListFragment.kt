package com.example.studyplannerapp.ui.all_tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentTaskListBinding
import com.example.studyplannerapp.ui.TasksViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TaskListFragment : Fragment() , TasksAdapter.TaskItemListener {

    private var _binding : FragmentTaskListBinding? = null
    private  val binding get() = _binding!!
    private lateinit var tasksAdapter: TasksAdapter
    private var positionToDelete: Int = -1
    private val viewModel: TasksViewModel by activityViewModels()
    lateinit var deleteDialogBuilder: MaterialAlertDialogBuilder

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

        // Initialize TasksAdapter
        tasksAdapter = TasksAdapter(this)

        // Configure Tasks RecyclerView
        binding.tasksRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksAdapter
        }

        // Load user profile picture using Glide library and display it in userImageView
        Glide.with(binding.root).load(R.drawable.icon_user).into(binding.userImageView)

        // Observe changes in tasksLiveData using ViewModel and update UI accordingly
        viewModel.tasksLiveData?.observe(viewLifecycleOwner, Observer { tasks ->
            tasksAdapter.setTasksList(tasks) // Update RecyclerView's task list
        })

        // Building MaterialAlertDialogBuilder for deleting tasks
        deleteDialogBuilder = MaterialAlertDialogBuilder(requireContext() , R.style.AlertDialog)
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                // Handle "yes" button click: Delete the task, update RecyclerView, and show a success Toast
                if(positionToDelete != -1) {
                    val task = tasksAdapter.taskAt(positionToDelete)
                    viewModel.deleteTask(task)
                    Toast.makeText(requireContext(), getString(R.string.delete_confirmation_alert), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                // Handle "No" button click: dismiss the dialog
                dialog.dismiss()
            }
            .setOnDismissListener {
                // Update RecyclerView on dialog dismissed
                tasksAdapter.notifyItemChanged(positionToDelete)
            }


        // Initialize ItemTouchHelper for handling actions on RecyclerView items
        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                // Define swipe directions
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                // Disable drag (movement) actions
                return makeMovementFlags(0 , swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            // Handle swipe action: Get the position of swiped item and show delete dialog
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                positionToDelete = viewHolder.adapterPosition
                deleteDialogBuilder.show()
            }
        }).attachToRecyclerView(binding.tasksRV)

        // Handle addTaskButton click: Navigate to AddTaskFragment
        binding.addTaskButton.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteOptionClicked(position: Int) {
        positionToDelete = position
        deleteDialogBuilder.show()
    }
    override fun onEditOptionClicked(position: Int) {
        findNavController().navigate(R.id.action_taskListFragment_to_editTaskFragment)
    }

}
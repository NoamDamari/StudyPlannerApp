package com.example.studyplannerapp.ui.all_tasks

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.studyplannerapp.R
import com.example.studyplannerapp.databinding.FragmentTaskListBinding
import com.example.studyplannerapp.ui.TasksViewModel
import com.example.studyplannerapp.utils.AlarmUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userLiveData?.observe(viewLifecycleOwner){user ->

            binding.userNameTV.text = user?.userName

            if(user?.profileImage != null) {
                val requestOptions = RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(30))

                Glide.with(binding.root)
                    .load(user.profileImage)
                    .apply(requestOptions)
                    .into(binding.userImageView)
            }
        }

        // Initialize TasksAdapter
        tasksAdapter = TasksAdapter(this)

        // Configure Tasks RecyclerView
        binding.tasksRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksAdapter
        }

        // Load user profile picture using Glide library and display it in userImageView
        Glide.with(binding.root).load(R.drawable.icon_user).into(binding.userImageView)

        binding.userProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_userProfileFragment)
        }
        // Observe changes in tasksLiveData using ViewModel and update UI accordingly
        viewModel.tasksLiveData?.observe(viewLifecycleOwner) { tasks ->

            val openTasksText = getString(R.string.open_tasks_count, tasks.size)

            val spannable = SpannableString(openTasksText)

            val numberStart = openTasksText.indexOf(tasks.size.toString())
            val numberEnd = numberStart + tasks.size.toString().length

            spannable.setSpan(ForegroundColorSpan(Color.parseColor("#37A69C")), numberStart, numberEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            tasksAdapter.setTasksList(tasks.reversed()) // Update RecyclerView's task list
            binding.textView4.text = spannable
        }

        // Building MaterialAlertDialogBuilder for deleting tasks
        deleteDialogBuilder = MaterialAlertDialogBuilder(requireContext() , R.style.AlertDialog)
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                // Handle "yes" button click: Delete the task, update RecyclerView, and show a success Toast
                if(positionToDelete != -1) {
                    val task = tasksAdapter.taskAt(positionToDelete)
                    viewModel.deleteTask(task)

                    AlarmUtils.cancelAlarm(requireContext(),task.id.toInt())

                    val snackbar = Snackbar.make(requireView(),getString(R.string.delete_confirmation_alert),Snackbar.LENGTH_SHORT)
                    snackbar.show()
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
        val taskId = tasksAdapter.taskAt(position).id
        findNavController().navigate(R.id.action_taskListFragment_to_editTaskFragment ,
            bundleOf("id" to taskId ))
    }

    override fun onTaskLongClick(position: Int) {
        val taskId = tasksAdapter.taskAt(position).id
        findNavController().navigate(R.id.action_taskListFragment_to_taskDetailsFragment ,
            bundleOf("id" to taskId)
        )
    }
}
package com.example.studyplannerapp.ui.all_tasks

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplannerapp.R
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.databinding.TaskLayoutBinding

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder> () {

    private var tasks : List<Task> = emptyList();

    class TaskViewHolder(private val binding: TaskLayoutBinding)
        : RecyclerView.ViewHolder(binding.root){

            fun bind(task: Task , onMenuButtonClick: (View, Task) -> Unit) {
                binding.taskTitleTV.text = task.title
                binding.taskDeadlineTV.text = task.getFormattedDeadline()

                binding.taskMenuButton.setOnClickListener {
                    onMenuButtonClick(it , task)
                }

                binding.taskSlider.apply {
                    isEnabled = false
                    thumbWidth = 0
                    thumbTrackGapSize = 0
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position]) {view , task ->
            showTaskPopupMenu(view , task)
        }
    }

    private fun showTaskPopupMenu(view: View , task: Task) {

        val taskMenu = PopupMenu(view.context , view)
        taskMenu.menuInflater.inflate(R.menu.task_options_menu , taskMenu.menu)
        taskMenu.setOnMenuItemClickListener {menuOption ->
            when(menuOption.itemId) {

                R.id.action_edit ->
                    // TODO: Call onEditClickListener
                    true

                R.id.action_delete ->
                    // TODO: Call onDeleteClickListener
                    true

                else -> {false}
            }
        }
        taskMenu.show()
    }

    fun setTasksList (tasksList: List<Task>) {
        tasks = tasksList
        //notifyDataSetChanged()
    }
}
package com.example.studyplannerapp.ui.all_tasks


import android.net.Uri
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.studyplannerapp.R
import com.example.studyplannerapp.data.models.Task
import com.example.studyplannerapp.databinding.TaskLayoutBinding

class TasksAdapter(val listener: TaskItemListener) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder> () {

    private var tasks : MutableList<Task> = mutableListOf()

    inner class TaskViewHolder(private val binding: TaskLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) , View.OnLongClickListener{

            init {
                binding.root.setOnLongClickListener(this)
            }

            fun bind(task: Task , onMenuButtonClick: (View, Task) -> Unit) {

                binding.taskTitleTV.text = task.title
                binding.taskDeadlineTV.text = task.getFormattedDeadline()
                binding.taskSlider.apply {
                    isEnabled = false
                    thumbWidth = 0
                    thumbTrackGapSize = 0
                    value = task.progressPercentage.toFloat()
                }

                try {
                    val uri = Uri.parse(task.image)
                    // Setting Image attrs
                    val requestOptions = RequestOptions()
                        .transform(CenterCrop(),RoundedCorners(30))

                    Glide.with(binding.root)
                        .load(uri)
                        .apply(requestOptions)
                        .into(binding.taskImageView)
                } catch (e: Exception) {
                    // Handle the error or leave the ImageView empty
                    binding.taskImageView.setImageResource(getImageByType(task.type))
                }

                binding.taskMenuButton.setOnClickListener {
                    onMenuButtonClick(it , task)
                }
            }

        override fun onLongClick(v: View?): Boolean {
            listener.onTaskLongClick(adapterPosition)
            return true
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
        holder.bind(tasks[position]) { view, _ ->
            showTaskPopupMenu(view , position)
        }
    }

    private fun showTaskPopupMenu(view: View , position: Int) {

        val contextThemeWrapper = ContextThemeWrapper(view.context, R.style.popupMenuStyle)
        val taskMenu = PopupMenu(contextThemeWrapper , view)
        taskMenu.menuInflater.inflate(R.menu.task_options_menu , taskMenu.menu)
        taskMenu.setOnMenuItemClickListener {menuOption ->
            when(menuOption.itemId) {

                R.id.action_edit -> {
                    listener.onEditOptionClicked(position)
                    true
                }

                R.id.action_delete -> {
                    listener.onDeleteOptionClicked(position)
                    true
                }
                else -> {false}
            }
        }
        taskMenu.show()
    }
    fun setTasksList(tasksList: List<Task>) {
        tasks.clear()
        tasks.addAll(tasksList)
        notifyDataSetChanged()
    }

    fun taskAt(position: Int) = tasks[position]

    interface TaskItemListener {
        fun onDeleteOptionClicked(position: Int)
        fun onEditOptionClicked(position: Int)
        fun onTaskLongClick(position: Int)
    }

   fun getImageByType(type: String): Int {
        return when (type) {
            "Assignment" -> R.drawable.icon_assignment
            "Exam" -> R.drawable.icon_exam
            "Lesson" -> R.drawable.icon_video
            "Project" -> R.drawable.icon_project
            else -> R.drawable.icon_logo
        }
    }
}
package com.izaltino.mytasks.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.izaltino.mytasks.R
import com.izaltino.mytasks.databinding.TaskListItemBinding
import com.izaltino.mytasks.entity.Task
import com.izaltino.mytasks.listener.TaskItemClickListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskViewHolder(
    private val context: Context,
    private val binding: TaskListItemBinding,
    private val listener: TaskItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun setValues(task: Task) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val dateFormat = sharedPreferences.getString("date_format", "numeric")

        binding.tvTitle.text = task.title
        binding.tvDescription.text = task.description

        binding.tvDate.text = task.date?.let {
            val formattedDate = if (dateFormat == "numeric") {
                task.date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            } else {
                task.date?.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"))
            }
            formattedDate
        } ?: run {
            "-"
        }

        binding.tvTime.text = task.time?.let {
            task.time.toString()
        } ?: run {
            "-"
        }

        val today = LocalDate.now()
        val taskDate = task.date

        if (task.completed) {
            // Tarefa completa: Verde
            binding.tvLeftColor.setBackgroundResource(R.color.green_700)
        } else {
            if (taskDate == null || taskDate.isAfter(today)) {
                // Sem data ou ainda está no prazo: Azul
                binding.tvLeftColor.setBackgroundResource(R.color.blue_700)
            } else if (taskDate.isEqual(today)) {
                // Tarefa vence hoje: Amarelo
                binding.tvLeftColor.setBackgroundResource(R.color.yellow_700)
            } else if (taskDate.isBefore(today)) {
                // Tarefa vencida (data anterior a anteontem): Vermelho
                binding.tvLeftColor.setBackgroundResource(R.color.red_700)
            }
        }

        binding.root.setOnClickListener {
            listener.onClick(task)
        }

        binding.root.setOnCreateContextMenuListener { menu, _, _ ->
            menu.add(ContextCompat.getString(context, R.string.mark_as_completed)).setOnMenuItemClickListener {
                listener.onMarkAsCompleteClick(adapterPosition, task)
                true
            }
            menu.add(ContextCompat.getString(context, R.string.share)).setOnMenuItemClickListener {
                listener.onShareClick(task)
                true
            }
        }
    }
}
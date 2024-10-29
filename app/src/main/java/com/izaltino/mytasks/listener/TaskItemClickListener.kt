package com.izaltino.mytasks.listener

import com.izaltino.mytasks.entity.Task

interface TaskItemClickListener {

    fun onClick(task: Task)

    fun onMarkAsCompleteClick(position: Int, task: Task)

    fun onShareClick(task: Task)
}
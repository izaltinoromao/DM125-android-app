package com.izaltino.mytasks.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.izaltino.mytasks.databinding.ActivityTaskFormBinding
import com.izaltino.mytasks.entity.Task
import com.izaltino.mytasks.service.TaskService
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding

    private val taskService: TaskService by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("lifecycle", "TaskForm onCreate")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initComponents()
        setValues()
    }

    @Suppress("deprecation")
    private fun initComponents() {
        val calendar = Calendar.getInstance()

        binding.etDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.etDate.setText(formattedDate)
            }, year, month, day).show()
        }

        binding.etTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.etTime.setText(formattedTime)
            }, hour, minute, true).show() // `true` para formato 24 horas
        }

        binding.btSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val dateStr = binding.etDate.text.toString()
            val timeStr = binding.etTime.text.toString()

            if (title.isBlank()) {
                Toast.makeText(this, "Título é obrigatório", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val date: LocalDate? = try {
                LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            } catch (e: DateTimeParseException) {
                Toast.makeText(this, "Data inválida. Use o formato dd/MM/yyyy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val time: LocalTime? = try {
                LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"))
            } catch (e: DateTimeParseException) {
                Toast.makeText(this, "Hora inválida. Use o formato HH:mm", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val taskId = (intent.extras?.getSerializable("taskId") as Long?)

            val task = Task(
                title = title,
                description = binding.etDescription.text.toString(),
                date = date,
                time = time,
                id = taskId
            )

            if (task.id == null) {
                taskService.save(task).observe(this) { responseDto ->
                    if (responseDto.isError) {
                        Toast.makeText(this, "Erro com o servidor", Toast.LENGTH_SHORT).show()
                    } else {
                        finish()
                    }
                }
            } else {
                taskService.update(task).observe(this) { responseDto ->
                    if (responseDto.isError) {
                        Toast.makeText(this, "Erro com o servidor", Toast.LENGTH_SHORT).show()
                    } else {
                        finish()
                    }
                }
            }


        }
    }

    @Suppress("deprecation")
    private fun setValues() {
        (intent.extras?.getSerializable("taskId") as Long?)?.let { taskId ->
            taskService.read(taskId).observe(this) { responseDto ->
                if (responseDto.isError) {
                    Toast.makeText(this, "Erro com o servidor", Toast.LENGTH_SHORT).show()
                } else {
                    responseDto.value?.let { task ->
                        binding.etTitle.setText(task.title)
                        binding.etDescription.setText(task.description)

                        task.date?.let { date ->
                            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            binding.etDate.setText(formattedDate)
                        }

                        task.time?.let { time ->
                            val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"))
                            binding.etTime.setText(formattedTime)
                        }

                        if (task.completed) {
                            binding.btSave.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        Log.e("lifecycle", "TaskForm onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.e("lifecycle", "TaskForm onResume")
    }

    override fun onStop() {
        super.onStop()

        Log.e("lifecycle", "TaskForm onStop")
    }

    override fun onPause() {
        super.onPause()

        Log.e("lifecycle", "TaskForm onPause")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.e("lifecycle", "TaskForm onDestroy")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
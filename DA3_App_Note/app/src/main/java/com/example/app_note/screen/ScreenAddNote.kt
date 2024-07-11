package com.example.app_note.screen

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.app_note.MainActivity
import com.example.app_note.R
import com.example.app_note.database.Note
import com.example.app_note.databinding.ActivityScreenAddNoteBinding
import com.example.app_note.notification.AlarmReceiver
import com.example.app_note.viewmodel.viewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScreenAddNote : AppCompatActivity() {
    private lateinit var binding : ActivityScreenAddNoteBinding
    private lateinit var animation: Animation
    private val CHANNEL_ID = "my_channel_id"
    private val notificationId = 1
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var dateString = "null"
    var timeString = "null"
    var calendar = Calendar.getInstance()
    var date = "null"

    val dataModel:viewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animation = AnimationUtils.loadAnimation(this@ScreenAddNote, R.anim.animation)

        doPickTime_Date()
        doAdd_a_Note()


    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun doPickTime_Date() {
        val calendar = Calendar.getInstance()
        binding.btnPickDate.setOnClickListener {
            binding.btnPickDate.startAnimation(animation)
            DatePickerDialog(this@ScreenAddNote, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                 date = "$i3/${i2}/$i"
                binding.txtDateNotification.text = "$i3/${i2+1}/$i"
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.btnPickTime.setOnClickListener {
            binding.btnPickTime.startAnimation(animation)
            TimePickerDialog(this@ScreenAddNote, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                binding.txtTimeNotification.setText(String.format("%02d:%02d", i, i2))
            },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true).show()
        }
    }

    private fun back_to_lastScreen() {
        val i = Intent(this@ScreenAddNote, MainActivity :: class.java)
        startActivity(i)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun doAdd_a_Note() {  //"dd/MM/yyyy HH:mm"

        binding.btnComfimAddNote.setOnClickListener {
            binding.btnComfimAddNote.startAnimation(animation)
            if (binding.edtInputTitle.text.isEmpty() || binding.edtInputContent.text.isEmpty()) {
                make_notification("Title and content can not be blank!")
            }else if(checked_setNotification()){
                make_notification("Set notification, please!")
            } else if(checkContent()){
                make_notification("Number of characters in title must less than 30")
            }
            else {
                dataModel.addNote(
                    binding.edtInputTitle,
                    binding.edtInputContent,
                    getCurrentDate(),
                    binding.txtDateNotification.text.toString(),
                binding.txtTimeNotification.text.toString())

            }

        }
        dataModel.note.observe(this@ScreenAddNote, Observer {
                  dataModel.addNote(it) // add note
            back_to_lastScreen()
            setAlarm(it)
        })
        }

    @SuppressLint("MissingPermission")
    private fun setAlarm(note: Note) {
        dateString = binding.txtDateNotification.text.toString()
        timeString = binding.txtTimeNotification.text.toString()


        Log.i("check_time", "$dateString + $timeString")
            val dateParts = dateString.split("/")
            val timeParts = timeString.split(":")
            day = dateParts[0].toInt()
            month = dateParts[1].toInt() - 1
            year = dateParts[2].toInt()
            hour = timeParts[0].toInt()
            minute = timeParts[1].toInt()
      ///  Log.i("check_value", "$day - $month - $year - $hour - $minute time = ${calendar.timeInMillis}")
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this@ScreenAddNote, AlarmReceiver :: class.java)
            intent.putExtra("content", note.content)
            val pendingIntent = PendingIntent.getBroadcast(this,note.id, intent,0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }


    private fun checked_setNotification(): Boolean {
        if( binding.txtDateNotification.text.equals("xx/yy/zzzz") || binding.txtTimeNotification.text.equals("xx:yy")){
            return true
        }
        return false
    }


    private fun checkContent(): Boolean {
        if( binding.edtInputTitle.text.length > 30){
            return true
        }
        return false
    }


    fun getCurrentDate(): String { // getDate
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Tháng bắt đầu từ 0
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day - $month - $year"
    }

    fun make_notification(string: String){
        val snack = Snackbar.make(binding.edtInputTitle,string, Toast.LENGTH_SHORT).show()
    }


    fun formatTimeInput(timeInput: String): String {
        val parts = timeInput.split(":")
        return if (parts.size == 2) {
            val hours = parts[0].padStart(2, '0')
            val minutes = parts[1].padStart(2, '0')
            "$hours:$minutes"
        } else {
            timeInput // Trả về chuỗi gốc nếu không hợp lệ
        }
    }


    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Định dạng đầu vào
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Định dạng đầu ra
        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Định dạng ngày không hợp lệ"
        }
    }
    }








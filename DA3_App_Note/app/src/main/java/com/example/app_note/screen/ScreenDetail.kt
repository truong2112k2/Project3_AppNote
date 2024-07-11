package com.example.app_note.screen

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.DialogInterface
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
import com.example.app_note.databinding.ActivityScreenDetailBinding
import com.example.app_note.notification.AlarmReceiver
import com.example.app_note.viewmodel.viewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScreenDetail : AppCompatActivity() {
    private lateinit var binding: ActivityScreenDetailBinding
    private val viewModel: viewModel by viewModels()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animation = AnimationUtils.loadAnimation(this@ScreenDetail, R.anim.animation)
        doPickTime_Date()
        show_data()


      
    }

    @SuppressLint("SetTextI18n")
    private fun show_data() {

        binding.edtInputTitle.setText( intent.getStringExtra("_title").toString())
        binding.edtInputContent.setText(intent.getStringExtra("_content").toString())
        binding.txtDateNotification.setText(intent.getStringExtra("_date_noti").toString())
        binding.txtTimeNotification.setText(intent.getStringExtra("_time_noti").toString())
        binding.btnCheckUpdateNote.setOnClickListener {
            binding.btnCheckUpdateNote.startAnimation(animation)
          val a=   intent.getStringExtra("_id").toString()

            if( binding.edtInputTitle.text.toString() != intent.getStringExtra("_title").toString() || binding.edtInputContent.text.toString() != intent.getStringExtra("_content").toString()
                || binding.txtDateNotification.text.toString() != intent.getStringExtra("_date_noti").toString() || binding.txtTimeNotification.text.toString() != intent.getStringExtra("_time_noti").toString()
                ){
                updateNote()
            }else{
                back_lastScreen()
            }
        }
    }
    private fun doPickTime_Date() {
        val calendar = Calendar.getInstance()
        binding.btnPickDate.setOnClickListener {
            binding.btnPickDate.startAnimation(animation)
            DatePickerDialog(this@ScreenDetail, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->


                binding.txtDateNotification.setText("$i3/${i2+1}/$i")

            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnPickTime.setOnClickListener {
            binding.btnPickTime.startAnimation(animation)
            TimePickerDialog(this@ScreenDetail, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->

                binding.txtTimeNotification.setText(formatTimeInput("$i:$i2"))

            },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true).show()
        }
    }

    private fun updateNote() {
        val alertDialog = AlertDialog.Builder(this@ScreenDetail)
        alertDialog.apply {
            setTitle("Do you want changing your note ?")
            setPositiveButton("[Confirm]"){ dialogInterface: DialogInterface, i: Int ->
                if(binding.edtInputTitle.text.isEmpty() || binding.edtInputContent.text.isEmpty()
                    ){
                    make_notification("You must enter both contents")
                }else if(checkContent()){
                    // ?
                    make_notification("Number of characters in title must less than 30")
                }else{
                    // set date
                    setAlarm()
                    // update item
                    viewModel.updateNote(intent.getStringExtra("_id").toString(),
                        binding.edtInputTitle,
                        binding.edtInputContent,
                        getCurrentDate(),
                        binding.txtDateNotification.text.toString(),
                        binding.txtTimeNotification.text.toString()
                        )
                    viewModel.note.observe(this@ScreenDetail, Observer {note->
                        viewModel.updateNote(note)
                        back_lastScreen()
                    })
                }

            }
            setNegativeButton("[Cancel]"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                back_lastScreen()
            }
        }
            .show()
    }

    private fun back_lastScreen() {
        val i = Intent(this@ScreenDetail, MainActivity :: class.java)
        startActivity(i)
        finish()
    }

    private fun setAlarm() {
        dateString = binding.txtDateNotification.text.toString()
        timeString = binding.txtTimeNotification.text.toString()


        val dateParts = dateString.split("/")
        val timeParts = timeString.split(":")
        day = dateParts[0].toInt()
        month = dateParts[1].toInt() -1
        year = dateParts[2].toInt()
        hour = timeParts[0].toInt()
        minute = timeParts[1].toInt()

        calendar.set(year, month,day, hour, minute,0)
        Log.i("node", "$year $month $day $hour $minute")
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this@ScreenDetail, AlarmReceiver :: class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,0, intent,0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

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

    private fun checkContent(): Boolean {
        if( binding.edtInputTitle.text.length > 30){
            return true
        }
        return false
    }
    fun formatTimeInput(timeInput: String): String {
        val parts = timeInput.split(":")
        return if (parts.size == 2) {
            val hours = parts[0].padStart(2, '0')
            val minutes = parts[1].padStart(2, '0')
            "$hours:$minutes"
        } else {
            timeInput
        }
    }


    override fun onBackPressed() { // check update when user click button back
        if( binding.edtInputTitle.text.toString() != intent.getStringExtra("_title").toString() || binding.edtInputContent.text.toString() != intent.getStringExtra("_content").toString()){
            updateNote()
        }else{
            back_lastScreen()
        }
    }



}
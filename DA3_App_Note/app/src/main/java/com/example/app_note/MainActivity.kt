package com.example.app_note

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_note.adapter.recycleview_adpater
import com.example.app_note.databinding.ActivityMainBinding
import com.example.app_note.notification.AlarmReceiver
import com.example.app_note.screen.ScreenAddNote
import com.example.app_note.viewmodel.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


// #EB891B
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: recycleview_adpater
    private lateinit var animation: Animation



    val viewModels: viewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.animation)
        to_screenAddNote()
        show_Data()

    }





    @SuppressLint("SetTextI18n")
    private fun show_Data() {
        adapter = recycleview_adpater(emptyList(), this@MainActivity, viewModels)
        viewModels.getAllNote.observe(this@MainActivity, Observer { listNote->
            if( listNote.size == 0){
                binding.txtNotification.visibility = View.VISIBLE
                binding.recycleview.visibility = View.GONE
                binding.txtTotalNote.visibility = View.GONE
            }else{
                binding.txtNotification.visibility = View.GONE
                binding.recycleview.visibility = View.VISIBLE
                binding.txtTotalNote.visibility = View.VISIBLE
                binding.txtTotalNote.text = "Total Notes: ${listNote.size}"
                adapter.setListNote(listNote)
                binding.recycleview.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.recycleview.adapter = adapter
            }

        })

    }

    private fun to_screenAddNote() {
        binding.btnAddNote.setOnClickListener {
            binding.btnAddNote.startAnimation(animation)
            val i = Intent(this@MainActivity, ScreenAddNote :: class.java)
            startActivity(i)
        }

    }
}
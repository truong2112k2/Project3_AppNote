package com.example.app_note.screen

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.app_note.MainActivity
import com.example.app_note.databinding.ActivityScreenFirstBinding

class ScreenFirst : AppCompatActivity() {

    private lateinit var binding : ActivityScreenFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toMainActivity()

    }

    private fun toMainActivity() {
        val countDownTime = object : CountDownTimer(5000, 1000){
            override fun onTick(p0: Long) {
                val time = ((5000 - p0)/50).toInt()

            }

            override fun onFinish() {
                val i = Intent(this@ScreenFirst,  MainActivity :: class.java)
                startActivity(i)
                finish()


            }
        }
        countDownTime.start()
    }
}
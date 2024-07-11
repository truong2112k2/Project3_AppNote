package com.example.app_note.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.app_note.MainActivity
import com.example.app_note.R
import com.example.app_note.screen.ScreenFirst

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val nocationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId , channelName, importance)
            channel.lightColor = Color.parseColor("#EB891B")
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
            nocationManager.createNotificationChannel(channel)
        }

        val content = intent!!.getStringExtra("content")

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_picktime)
            .setContentTitle("NOTES")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        nocationManager.notify(0, builder.build())

        val resultIntent = Intent(context, ScreenFirst :: class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)
        nocationManager.notify(0, builder.build())





    }
}
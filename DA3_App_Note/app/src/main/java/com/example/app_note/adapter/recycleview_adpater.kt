package com.example.app_note.adapter

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_note.R
import com.example.app_note.database.Note
import com.example.app_note.databinding.ItemAdapterBinding
import com.example.app_note.notification.AlarmReceiver
import com.example.app_note.screen.ScreenDetail
import com.example.app_note.viewmodel.viewModel

class recycleview_adpater(var list: List<Note>, val context: Context, val viewModel: viewModel): RecyclerView.Adapter<recycleview_adpater.viewHolder>() {
    inner class viewHolder(val binding: ItemAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    val animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.animation)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = ItemAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.txtShowTitle.text = list[position].title
        holder.binding.txtSince.text = "Since: " + list[position].date
        holder.binding.btnDelete.setOnClickListener {
            holder.binding.btnDelete.startAnimation(animation)
            val alertDialog = AlertDialog.Builder(context)
                .apply {
                    setTitle("Do you want delete this note ?")
                    setPositiveButton("[Comfirm]") { dialogInterface: DialogInterface, i: Int ->
                        viewModel.deleteNote(list[position])
                        cancelAlarm(context = context, list[position])
                    }
                    setNegativeButton("[Cancel]") { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }
                }
                .show()
        }

        holder.binding.btnUpdate.setOnClickListener {
            holder.binding.btnUpdate.startAnimation(animation)
            val intent = Intent(context, ScreenDetail::class.java)
            intent.putExtra("_id", list[position].id.toString())
            intent.putExtra("_title", list[position].title)
            intent.putExtra("_content", list[position].content)
            intent.putExtra("_date_noti", list[position].date_notification)
            intent.putExtra("_time_noti", list[position].time_notification)

            context.startActivity(intent)

        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setListNote(noteList: List<Note>) {
        this.list = noteList
        notifyDataSetChanged()
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelAlarm(context: Context, note: Note) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, note.id?: 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Hủy bỏ báo thức
        alarmManager.cancel(pendingIntent)


    }
}
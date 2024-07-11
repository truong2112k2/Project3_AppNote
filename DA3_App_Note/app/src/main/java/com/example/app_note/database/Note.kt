package com.example.app_note.database

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Notes.db")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0 ,
    @ColumnInfo(name = "COLUMN_TITLE") var title: String,
    @ColumnInfo(name = "COLUMN_CONTENT") var content: String,
    @ColumnInfo(name = "COLUMN_DATE") var date: String,
    @ColumnInfo(name = "COLUMN_DATE_NOTIFICATION") var date_notification: String,
    @ColumnInfo(name = "COLUMN_TIME_NOTIFICATION") var time_notification: String,
)

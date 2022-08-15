package com.restart.book

import androidx.room.Database
import androidx.room.RoomDatabase
import com.restart.book.dao.HistoryDao
import com.restart.book.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
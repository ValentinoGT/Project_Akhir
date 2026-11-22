package com.example.projectakhir.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SessionEntity::class], version = 1, exportSchema = false)
abstract class FocusSenseDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: FocusSenseDatabase? = null

        fun getDatabase(context: Context): FocusSenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FocusSenseDatabase::class.java,
                    "focus_sense.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

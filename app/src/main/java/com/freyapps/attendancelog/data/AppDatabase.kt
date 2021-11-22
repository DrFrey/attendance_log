package com.freyapps.attendancelog.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Student::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val INSERT_NAMES =
            "INSERT INTO students (id, first_name, last_name, status) VALUES (1, 'Емельян', 'Кулебякин', 1), " +
                    "(2, 'Авдотья', 'Пушная', 2), (3, 'Семен', 'Голубчиков', 3), (4, 'Васисуалий', 'Пупкевич', 3)"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "StudentsDB"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        db.execSQL(INSERT_NAMES)
                    }
                })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
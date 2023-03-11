package com.freyapps.attendancelog.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Student::class, Group::class],
    version = 2,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun groupDao(): GroupDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val INSERT_NAMES =
            "INSERT INTO students (id, first_name, last_name, status, group_id) VALUES (1, 'Емельян', 'Кулебякин', 1, 1), " +
                    "(2, 'Авдотья', 'Пушная', 2, 1), (3, 'Семен', 'Голубчиков', 3, 1), (4, 'Васисуалий', 'Пупкевич', 3, 1)"
        private const val INSERT_GROUP =
            "INSERT INTO groups (id, name) VALUES (1, 'Group 1')"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "StudentsDB"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        db.execSQL(INSERT_NAMES)
                        db.execSQL(INSERT_GROUP)
                    }
                })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
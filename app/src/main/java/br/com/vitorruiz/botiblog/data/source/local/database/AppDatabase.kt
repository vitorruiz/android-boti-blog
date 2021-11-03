package br.com.vitorruiz.botiblog.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.vitorruiz.botiblog.data.source.local.database.dao.PostDao
import br.com.vitorruiz.botiblog.data.source.local.database.dao.UserDao
import br.com.vitorruiz.botiblog.data.source.local.database.entity.PostEntity
import br.com.vitorruiz.botiblog.data.source.local.database.entity.UserEntity

@Database(entities = [UserEntity::class, PostEntity::class], version = 1, exportSchema = true)
// @TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
}

fun buildAppDatabase(context: Context) =
    Room.databaseBuilder(context, AppDatabase::class.java, "botiblog.db")
        .createFromAsset("database/botiblog.db")
        .fallbackToDestructiveMigration()
        .build()
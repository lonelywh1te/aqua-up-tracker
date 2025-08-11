package ru.lonelywh1te.aquaup.data.source.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.lonelywh1te.aquaup.data.source.db.converters.LocalDateTimeConverter
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogDao
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogEntity

@Database(
    entities = [WaterLogEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [LocalDateTimeConverter::class])
abstract class MainDatabase: RoomDatabase() {

    abstract fun waterLogDao(): WaterLogDao

    companion object {
        private var db: MainDatabase? = null

        fun instance(context: Context): MainDatabase {
            return db ?: synchronized(this) {
                Room
                    .databaseBuilder(context.applicationContext, MainDatabase::class.java, "aqua-up-db")
                    .build()
                    .also { db = it }
            }

        }
    }

}
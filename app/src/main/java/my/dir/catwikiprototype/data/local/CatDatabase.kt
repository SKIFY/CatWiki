package my.dir.catwikiprototype.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CatEntity::class], version = 1)
abstract class CatDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao

    companion object {
        @Volatile private var INSTANCE: CatDatabase? = null

        fun getInstance(ctx: Context): CatDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    CatDatabase::class.java,
                    "catwiki_db"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
    }
}
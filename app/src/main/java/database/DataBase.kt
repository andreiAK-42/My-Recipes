package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecipeEntity::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun getRecipeDao(): RecipesDao

    companion object {
        private var db_instance: RecipesDatabase? = null

        fun getRecipesDatabaseInstance(context: Context): RecipesDatabase {
            if (db_instance == null) {
                db_instance = Room.databaseBuilder(
                    context.applicationContext, RecipesDatabase::class.java, "recipes_db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return db_instance!!
        }
    }
}
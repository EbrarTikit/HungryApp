package com.example.HungryApp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [FavoriteRecipe::class], version = 1, exportSchema = false)
abstract class FavoriteRecipeDatabase : RoomDatabase() {
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRecipeDatabase? = null

        fun getDatabase(context: Context): FavoriteRecipeDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteRecipeDatabase::class.java,
                    "favorite_recipe_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
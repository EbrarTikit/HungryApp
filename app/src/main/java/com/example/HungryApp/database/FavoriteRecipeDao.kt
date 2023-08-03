package com.example.HungryApp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteRecipe(favoriteRecipe: FavoriteRecipe)

    @Delete
    fun deleteFavoriteRecipe(favoriteRecipe: FavoriteRecipe)

    @Query("SELECT * FROM favorite_recipes")
    fun getAllFavoriteRecipes(): Flow<List<FavoriteRecipe>>

    @Query("SELECT COUNT(*) FROM favorite_recipes WHERE id = :recipeId")
    fun getRecipeCount(recipeId: Int): Int
}





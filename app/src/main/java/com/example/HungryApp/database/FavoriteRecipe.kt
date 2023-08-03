package com.example.HungryApp.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipe(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "recipe name") val title: String,
    @NonNull @ColumnInfo(name = "recipe_image") val image: String
)
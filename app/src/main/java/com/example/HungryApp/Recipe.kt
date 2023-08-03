package com.example.HungryApp

import com.example.HungryApp.database.FavoriteRecipe
import com.google.gson.annotations.SerializedName

data class Recipe(
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("image")
    val image: String,
    var isFavorite: Boolean = false
)

data class RecipeResponse(
    val recipes: List<Recipe>
)

fun FavoriteRecipe.toRecipe(): Recipe {
    return Recipe(this.id, this.title, this.image, isFavorite = true)
}




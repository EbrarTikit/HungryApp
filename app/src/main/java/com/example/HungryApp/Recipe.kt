package com.example.HungryApp

import com.google.gson.annotations.SerializedName

data class Recipe(
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("image")
    val image: String
)


data class RecipeResponse(
    val recipes: List<Recipe>
)




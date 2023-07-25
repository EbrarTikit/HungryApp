package com.example.HungryApp

import com.google.gson.annotations.SerializedName

data class RecipeDetails(
    val id: Long,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    @SerializedName("spoonacularScore")
    val spoonacularScore: Double,
    val instructions: String,
    val extendedIngredients: List<ExtendedIngredient>
) {
    // Helper function to format ingredients
    fun getIngredientsFormatted(): String {
        val formattedIngredients = StringBuilder()
        for (ingredient in extendedIngredients) {
            formattedIngredients.append("- ${ingredient.original}\n")
        }
        return formattedIngredients.toString()
    }
}

data class ExtendedIngredient(
    val original: String
)


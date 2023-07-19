package com.example.HungryApp

data class RecipeDetails(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val spoonacularScore: Float,
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


package com.example.HungryApp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApiClient {
    @GET("recipes/random")
    fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int
    ): Call<RecipeResponse>


    @GET("recipes/findByIngredients")
    fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int
    ): Call<List<Recipe>>

    @GET("recipes/{recipeId}/information")
    fun getRecipeDetails(
        @Path("recipeId") recipeId: Int,
        @Query("apiKey") apiKey: String
    ): Call<RecipeDetails>



    companion object {
        private const val BASE_URL = "https://api.spoonacular.com/"
        private val gson: Gson = GsonBuilder().setLenient().create()
        private val httpClient: OkHttpClient = OkHttpClient.Builder().build()

        fun create(): SpoonacularApiClient {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()

            return retrofit.create(SpoonacularApiClient::class.java)
        }
    }
}

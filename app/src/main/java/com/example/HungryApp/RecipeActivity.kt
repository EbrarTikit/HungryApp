package com.example.HungryApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        /**
         * Getting recipe according to click
         */

        val recipeId = intent.getIntExtra("recipeId", -1)
        Log.d("RecipeActivity", "Recipe ID: $recipeId")

        if (recipeId != -1) {
            getRecipeDetails(recipeId.toLong())
        } else {
            Toast.makeText(this, "Recipe ID not provided", Toast.LENGTH_SHORT).show()
            finish()
        }


        /**
         * user profile
         */

        val topAppBar: MaterialToolbar = findViewById(R.id.topAppBar)
        val userIcon: MenuItem = topAppBar.menu.findItem(R.id.user_icon)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.user_icon) {
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }

        /**
         * bottom navigation
         */

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.page_2 -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                R.id.page_3 -> {
                    startActivity(Intent(this, FavouriteActivity::class.java))
                    true
                }
                else -> false
            }
        }


    }

    private fun getRecipeDetails(recipeId: Long) {
        val apiKey = "5670f322efe241719f174f036883fb18" // Replace with your actual API key

        val client = SpoonacularApiClient.create()
        val call = client.getRecipeDetails(recipeId, apiKey)
        call.enqueue(object : Callback<RecipeDetails> {
            override fun onResponse(call: Call<RecipeDetails>, response: Response<RecipeDetails>) {
                if (response.isSuccessful) {
                    val recipeDetails = response.body()
                    Log.d("RecipeActivity", "API Response: ${Gson().toJson(recipeDetails)}")


                    Log.d("RecipeActivity", "spoonacularScore: ${recipeDetails?.spoonacularScore}")

                    updateUIWithRecipeDetails(recipeDetails)
                } else {
                    Toast.makeText(this@RecipeActivity, "Failed to load recipe details: ${response.message()}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<RecipeDetails>, t: Throwable) {
                Toast.makeText(this@RecipeActivity, "Failed to load recipe details: ${t.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun updateUIWithRecipeDetails(recipeDetails: RecipeDetails?) {

        val imageView: ImageView = findViewById(R.id.imageView)

        // Load image using a library with Glide
        Glide.with(this).load(recipeDetails?.image).into(imageView)

        val dishNameTextView: TextView = findViewById(R.id.dish_name)
        val cookTimeTextView: TextView = findViewById(R.id.time_cook)
        val ratingBar: RatingBar = findViewById(R.id.ratingBar)

        dishNameTextView.text = recipeDetails?.title
        cookTimeTextView.text = "${recipeDetails?.readyInMinutes} min"
        Log.d("RecipeActivity", "Cook Time: ${recipeDetails?.readyInMinutes}")
        ratingBar.rating = recipeDetails?.spoonacularScore?.toFloat() ?: 0f
        Log.d("RecipeActivity", "Rating: ${recipeDetails?.spoonacularScore?.toFloat() ?: 0f}")

        val ingredientsTextView: TextView = findViewById(R.id.ingredients_text_view)
        ingredientsTextView.text = recipeDetails?.getIngredientsFormatted()

        val instructionsTextView: TextView = findViewById(R.id.instructions_text_view)
        instructionsTextView.text = recipeDetails?.instructions
    }


}
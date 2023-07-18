package com.example.HungryApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = RecipeAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val ingredients = intent.getStringArrayListExtra("ingredients")
        if (ingredients != null) {
            searchRecipes(ingredients)
        } else {
            Toast.makeText(this, "No ingredients provided", Toast.LENGTH_SHORT).show()
        }

        //user profile
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

        //bottom navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.page_2
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    startActivity(Intent(this, MainActivity::class.java))
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

    private fun searchRecipes(ingredients: List<String>) {
        val apiKey = "5670f322efe241719f174f036883fb18" // Replace with your actual API key

        val client = SpoonacularApiClient.create()
        val query = ingredients.joinToString(",")

        val call = client.searchRecipes(apiKey, query, 10)
        call.enqueue(object : Callback<List<Recipe>> {
            override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                if (response.isSuccessful) {
                    val recipes = response.body()
                    // Handle successful response and update UI
                    adapter.setData(recipes)
                    adapter.notifyDataSetChanged()
                } else {
                    // Handle API error
                    Toast.makeText(this@RecipeListActivity, "Failed to load recipes: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                // Handle network or other failures
                Toast.makeText(this@RecipeListActivity, "Failed to load recipes: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



}
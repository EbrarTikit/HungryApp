package com.example.HungryApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiKey = "5670f322efe241719f174f036883fb18"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecipeAdapter(emptyList())
        recyclerView.adapter = adapter

        val client = SpoonacularApiClient.create()
        val call = client.getRandomRecipes(apiKey, 10) // Get 10 random recipes

        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                if (response.isSuccessful) {
                    val recipeResponse = response.body()
                    val recipes = recipeResponse?.recipes

                    if (recipes != null) {
                        adapter = RecipeAdapter(recipes)
                        adapter.setOnItemClickListener(object : RecipeAdapter.OnItemClickListener {
                            override fun onItemClick(recipeId: Int) {
                                // Handle item click here
                                val intent = Intent(this@MainActivity, RecipeActivity::class.java)
                                intent.putExtra("recipeId", recipeId)
                                startActivity(intent)
                            }
                        })
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                // Handle API call failure
            }
        })



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
        bottomNavigationView.selectedItemId = R.id.page_1
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
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

    fun onItemClick(recipeId: Int) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("recipeId", recipeId)
        startActivity(intent)
    }
}
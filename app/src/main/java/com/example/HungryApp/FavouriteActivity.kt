package com.example.HungryApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HungryApp.database.FavoriteRecipe
import com.example.HungryApp.database.FavoriteRecipeDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecipeAdapter(emptyList())
        recyclerView.adapter = adapter

        // Fetch all favorite recipes from the database and set them in the adapter
        GlobalScope.launch(Dispatchers.IO) {
            val favoriteRecipesFlow = FavoriteRecipeDatabase.getDatabase(this@FavouriteActivity).favoriteRecipeDao().getAllFavoriteRecipes()
            favoriteRecipesFlow.collect { favoriteRecipes ->
                withContext(Dispatchers.Main) {
                    // Convert FavoriteRecipe to Recipe
                    val recipes = favoriteRecipes.map { favoriteRecipe ->
                        Recipe(favoriteRecipe.id, favoriteRecipe.title, favoriteRecipe.image, true)
                    }
                    adapter.setData(recipes)

                    adapter.setOnItemClickListener(object : RecipeAdapter.OnItemClickListener {
                        override fun onItemClick(recipeId: Int) {
                            val intent = Intent(this@FavouriteActivity, RecipeActivity::class.java)
                            intent.putExtra("recipeId", recipeId)
                            startActivity(intent)
                        }

                        override fun onFavoriteButtonClick(recipe: Recipe) {
                            GlobalScope.launch(Dispatchers.IO) {
                                val favoriteRecipeDao = FavoriteRecipeDatabase.getDatabase(this@FavouriteActivity).favoriteRecipeDao()

                                if (recipe.isFavorite) {
                                    // Remove recipe from favorites
                                    val favoriteRecipe = FavoriteRecipe(recipe.id, recipe.title, recipe.image)
                                    favoriteRecipeDao.deleteFavoriteRecipe(favoriteRecipe)
                                } else {
                                    // Add recipe to favorites
                                    val favoriteRecipe = FavoriteRecipe(recipe.id, recipe.title, recipe.image)
                                    favoriteRecipeDao.insertFavoriteRecipe(favoriteRecipe)
                                }

                                // Update the isFavorite flag in the recipe list
                                val updatedFavoriteRecipes = adapter.recipes.map {
                                    if (it.id == recipe.id) it.copy(isFavorite = !it.isFavorite)
                                    else it
                                }

                                // Update the UI with the updated list of favorite recipes
                                withContext(Dispatchers.Main) {
                                    adapter.setData(updatedFavoriteRecipes)
                                }
                            }
                        }
                    })
                }
            }
        }


        //login
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
        bottomNavigationView.selectedItemId = R.id.page_3
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1-> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.page_2 -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}

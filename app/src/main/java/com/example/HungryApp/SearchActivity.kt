package com.example.HungryApp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.HungryApp.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SearchActivity : AppCompatActivity() {
    // ...

    private lateinit var searchView: SearchView
    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //search and chip
        searchView = findViewById(R.id.search_view)
        chipGroup = findViewById(R.id.Ingredients)

        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                addChip(query)
                searchView.setQuery("", false)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        }

        searchView.setOnQueryTextListener(queryTextListener)

        val searchButton: Button = findViewById(R.id.search)
        searchButton.setOnClickListener {
            val selectedIngredients = mutableListOf<String>()
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                selectedIngredients.add(chip.text.toString())
            }
            performSearch(selectedIngredients)
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

    private fun addChip(text: String) {
        val chip = Chip(this)
        chip.text = text
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
            Toast.makeText(this, "Chip Removed", Toast.LENGTH_SHORT).show()
        }
        chipGroup.addView(chip)
    }

    private fun performSearch(ingredients: List<String>) {
        val intent = Intent(this, RecipeListActivity::class.java)
        intent.putStringArrayListExtra("ingredients", ArrayList(ingredients))
        startActivity(intent)
    }

}

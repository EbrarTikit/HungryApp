package com.example.HungryApp

import android.app.Application
import com.example.HungryApp.database.FavoriteRecipeDatabase

class FavouriteApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database: FavoriteRecipeDatabase by lazy {
        FavoriteRecipeDatabase.getDatabase(this)
    }

}
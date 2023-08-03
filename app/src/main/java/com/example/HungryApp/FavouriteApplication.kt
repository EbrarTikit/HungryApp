package com.example.HungryApp

import android.app.Application
import com.example.HungryApp.database.FavoriteRecipeDatabase

class FavouriteApplication : Application() {

    val database: FavoriteRecipeDatabase by lazy {
        FavoriteRecipeDatabase.getDatabase(this)
    }

}
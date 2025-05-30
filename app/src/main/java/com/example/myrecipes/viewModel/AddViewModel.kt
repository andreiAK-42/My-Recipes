package com.example.myrecipes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import database.MyApp
import database.RecipeEntity
import database.RecipesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddViewModel(application: Application): AndroidViewModel(application)  {
    @Inject
    lateinit var recipesDao: RecipesDao

    init {
        (application as MyApp).getAppComponent().inject(this)
    }


    fun insertRecord(recipeEntity: RecipeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.insertRecipe(recipeEntity)
        }
    }
}
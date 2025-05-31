package com.example.myrecipes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import database.MyApp
import database.RecipeEntity
import database.RecipesDao
import javax.inject.Inject

class WeekViewModel (application: Application): AndroidViewModel(application)  {
    @Inject
    lateinit var recipesDao: RecipesDao
    init {
        (application as MyApp).getAppComponent().inject(this)
    }
    fun getRecipesByDay(day: Int): LiveData<List<RecipeEntity>> {
        return recipesDao.getRecipesByDay(day.toString())
    }

}
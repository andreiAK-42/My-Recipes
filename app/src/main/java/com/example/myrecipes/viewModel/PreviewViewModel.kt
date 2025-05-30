package com.example.myrecipes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import database.MyApp
import database.RecipeEntity
import database.RecipesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreviewViewModel(application: Application): AndroidViewModel(application)  {
    @Inject
    lateinit var recipesDao: RecipesDao

    val recipe = MutableLiveData<RecipeEntity>()

    init {
        (application as MyApp).getAppComponent().inject(this)
    }
    fun getRecord(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            recipe.postValue(recipesDao.getRecipesById(id))
        }
    }

    fun updateRecord(recipeEntity: RecipeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.updateRecipe(recipeEntity)
        }
    }

    fun deleteRecord(recipeEntity: RecipeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            recipesDao.deleteRecipe(recipeEntity)
        }
    }
}
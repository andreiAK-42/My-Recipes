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


class RecipesViewModel(application: Application): AndroidViewModel(application) {
    @Inject
    lateinit var recipesDao: RecipesDao

    val allRecipeList = MutableLiveData<MutableList<RecipeEntity>>(mutableListOf())
    init {
        (application as MyApp).getAppComponent().inject(this)
        getAllRecords()
    }

    private fun getAllRecords() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = recipesDao.getAllRecipes() ?: mutableListOf()
            allRecipeList.postValue(list)
        }
    }

    fun deleteRecord(noteEntity: RecipeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
          /*  appDao.deleteRecord(noteEntity)

            val updatedList = appDao.getAllRecordsFromDB() ?: mutableListOf()
            allPictureList.postValue(updatedList)*/
        }

    }

    fun updateRecord(noteEntity: RecipeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
         /*   appDao.updateRecord(noteEntity)
            getAllRecords()*/
        }
    }

}
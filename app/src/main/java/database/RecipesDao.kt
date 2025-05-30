package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipesDao {

    @Query("SELECT * FROM recipes_entity")
    fun getAllRecipes(): MutableList<RecipeEntity>?

    @Query("SELECT * FROM recipes_entity WHERE id = (:id)")
    fun getRecipesById(id: Int): RecipeEntity

    @Insert
    fun insertRecipe(recipe: RecipeEntity)

    @Update
    fun updateRecipe(recipe: RecipeEntity)

    @Delete
    fun deleteRecipe(picture: RecipeEntity)
}
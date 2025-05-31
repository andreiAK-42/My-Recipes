package database

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {
    @Singleton
    @Provides
    fun getRecipesDao(recipeDatabase: RecipesDatabase): RecipesDao {
        return recipeDatabase.getRecipeDao()
    }

    @Singleton
    @Provides
    fun getRoomDbInstance(): RecipesDatabase {
        return RecipesDatabase.getRecipesDatabaseInstance(provideAppContext())
    }

    @Singleton
    @Provides
    fun provideAppContext(): Context {
        return application.applicationContext
    }
}

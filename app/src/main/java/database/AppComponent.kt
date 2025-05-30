package database

import com.example.myrecipes.viewModel.AddViewModel
import com.example.myrecipes.viewModel.PreviewViewModel
import com.example.myrecipes.viewModel.RecipesViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(recipesViewModel: RecipesViewModel)
    fun inject(addViewModel: AddViewModel)
    fun inject(previewViewModel: PreviewViewModel)
}
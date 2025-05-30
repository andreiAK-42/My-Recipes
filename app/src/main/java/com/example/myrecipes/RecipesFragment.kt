package com.example.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipes.ui.RecipesAdapter
import com.example.myrecipes.viewModel.RecipesViewModel
import database.RecipeEntity


class RecipesFragment : Fragment(), RecipesAdapter.OnRecipeAdapterListener {
    lateinit var recipesAdapter: RecipesAdapter
    lateinit var viewModel: RecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)
        viewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)

        view.findViewById<ImageView>(R.id.iv_favorite).setOnClickListener {
            val action = RecipesFragmentDirections.actionFragmentRecipesToFragmentFavorite()
            findNavController().navigate(action)
        }

        view.findViewById<ImageView>(R.id.iv_add).setOnClickListener {
            val action = RecipesFragmentDirections.actionFragmentRecipesToFragmentAdd()
            findNavController().navigate(action)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rview_recipes)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recipesAdapter = RecipesAdapter(mutableListOf(), this@RecipesFragment)
        recyclerView.adapter = recipesAdapter

        viewModel.allRecipeList.observe(viewLifecycleOwner) { recipes ->
            var tagList: MutableList<String> = mutableListOf()

            recipes.forEach {
                tagList.add(it.tag)
            }

            initFilters(view, tagList.distinct())

            recipesAdapter.updateList(recipes)
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewRecipe(recipe: RecipeEntity) {
        val action = RecipesFragmentDirections.actionFragmentRecipesToFragmentPreview(recipe.id)
        findNavController().navigate(action)
    }

    fun initFilters(view: View, tags: List<String>) {
        val spinnerTag = view.findViewById<Spinner>(R.id.spin_filterTag)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tags)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTag.adapter = adapter

        val difList = listOf("Hard", "Medium", "Easy")
        val spinnerTime = requireView().findViewById<Spinner>(R.id.spin_filterDificulty)
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, difList)

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTime.adapter = adapter2
    }
}
package com.example.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipes.ui.RecipesAdapter
import com.example.myrecipes.viewModel.RecipesViewModel
import database.RecipeEntity


class RecipesFragment : Fragment(), RecipesAdapter.OnRecipeAdapterListener {

    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var viewModel: RecipesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recipes, container, false)
        viewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)

        setupUI(view)
        return view
    }

    private fun setupUI(view: View) {
        setupNavigation(view)
        setupSearch(view)
        setupRecyclerView(view)
        setupObservers(view)
        setupFilters(view)
    }

    private fun setupNavigation(view: View) {
        view.findViewById<ImageView>(R.id.iv_add).setOnClickListener {
            navigateTo(RecipesFragmentDirections.actionFragmentRecipesToFragmentAdd())
        }

        view.findViewById<ImageView>(R.id.iv_week).setOnClickListener {
            navigateTo(RecipesFragmentDirections.actionFragmentRecipesToFragmentWeek())
        }
    }

    private fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun setupSearch(view: View) {
        view.findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_searchRecipes).setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) = updateSearchFilter(query)
            override fun onQueryTextChange(newText: String) = updateSearchFilter(newText)
        })
    }

    private fun updateSearchFilter(query: String): Boolean {
        if (query.isNotEmpty()) {
            recipesAdapter.searchFilter(query)
        }
        return true
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rview_recipes)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recipesAdapter = RecipesAdapter(mutableListOf(), this)
        recyclerView.adapter = recipesAdapter
    }

    private fun setupObservers(view: View) {
        viewModel.allRecipeList.observe(viewLifecycleOwner) { recipes ->
            val (tagList, timeList) = extractTagsAndTimes(recipes)
            initFilters(view, tagList.distinct(), timeList.distinct())
            recipesAdapter.updateList(recipes)
            recipesAdapter.initFilterList()
        }
    }

    private fun extractTagsAndTimes(recipes: List<RecipeEntity>): Pair<MutableList<String>, MutableList<String>> {
        val tagList = mutableListOf("All", "Favorite")
        val timeList = mutableListOf("All")

        recipes.forEach { recipe ->
            tagList.add(recipe.tag)
            timeList.add(recipe.time.toString())
        }
        return Pair(tagList, timeList)
    }

    private fun setupFilters(view: View) {
        setupSpinner(view.findViewById(R.id.spin_filterTag)) { selectedTag ->
            recipesAdapter.setTagFilter(selectedTag)
        }

        setupSpinner(view.findViewById(R.id.spin_filterDificulty)) { selectedDif ->
            recipesAdapter.setDif(selectedDif)
        }

        setupSpinner(view.findViewById(R.id.spin_filterTime)) { selectedTime ->
            recipesAdapter.setTime(selectedTime)
        }
    }

    private fun setupSpinner(spinner: Spinner, onItemSelected: (String?) -> Unit) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                onItemSelected(spinner.selectedItem?.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initFilters(view: View, tags: List<String>, time: List<String>) {
        val tagSpinner = view.findViewById<Spinner>(R.id.spin_filterTag)
        setupSpinnerAdapter(tagSpinner, tags)

        val difficultySpinner = view.findViewById<Spinner>(R.id.spin_filterDificulty)
        setupSpinnerAdapter(difficultySpinner, listOf("All", "Hard", "Medium", "Easy"))

        val timeSpinner = view.findViewById<Spinner>(R.id.spin_filterTime)
        setupSpinnerAdapter(timeSpinner, time)
    }

    private fun setupSpinnerAdapter(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onViewRecipe(recipe: RecipeEntity) {
        navigateTo(RecipesFragmentDirections.actionFragmentRecipesToFragmentPreview(recipe.id))
    }
}
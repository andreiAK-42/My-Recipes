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
            var timeList: MutableList<String> = mutableListOf()

            tagList.add("All")
            tagList.add("Favorite")

            recipes.forEach {
                tagList.add(it.tag)
                timeList.add(it.time.toString())
            }

            initFilters(view, tagList.distinct(), timeList.distinct())

            recipesAdapter.updateList(recipes)
            recipesAdapter.initFilterList()
        }

        view.findViewById<Spinner>(R.id.spin_filterTag).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val spinner = parent as? Spinner

                val selectedTag = spinner?.selectedItem?.toString()

                recipesAdapter.setTagFilter(selectedTag)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        view.findViewById<Spinner>(R.id.spin_filterDificulty).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val spinner = parent as? Spinner

                val selectedTag = spinner?.selectedItem?.toString()

                recipesAdapter.setDif(selectedTag)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        return view
    }

    override fun onViewRecipe(recipe: RecipeEntity) {
        val action = RecipesFragmentDirections.actionFragmentRecipesToFragmentPreview(recipe.id)
        findNavController().navigate(action)
    }

    fun initFilters(view: View, tags: List<String>, time: List<String>) {
        val spinnerTag = view.findViewById<Spinner>(R.id.spin_filterTag)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tags)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTag.adapter = adapter

        val difList = listOf("All", "Hard", "Medium", "Easy")
        val spinnerDif = requireView().findViewById<Spinner>(R.id.spin_filterDificulty)
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, difList)

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerDif.adapter = adapter2


        val spinnerTime = requireView().findViewById<Spinner>(R.id.spin_filterTime)
        val adapter3 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, time)

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTime.adapter = adapter3
    }
}
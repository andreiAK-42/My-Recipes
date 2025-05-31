package com.example.myrecipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipes.ui.RecipesAdapter
import com.example.myrecipes.ui.WeekRecipesAdapter
import com.example.myrecipes.viewModel.RecipesViewModel
import com.example.myrecipes.viewModel.WeekViewModel
import database.RecipeEntity


class WeekFragment : Fragment() {

    private lateinit var daysContainer: LinearLayout
    private lateinit var recipesAdapter: WeekRecipesAdapter
    private lateinit var viewModel: WeekViewModel

    private val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_week, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupRecyclerView(view)
        setupViewModel()
        createDayButtons()
    }

    private fun initializeViews(view: View) {
        daysContainer = view.findViewById(R.id.days_container)
    }

    private fun setupRecyclerView(view: View) {
        val recipesRecyclerView = view.findViewById<RecyclerView>(R.id.recipes_recycler_view)
        recipesRecyclerView.layoutManager = GridLayoutManager(context, 2)

        recipesAdapter = WeekRecipesAdapter(mutableListOf(), this)
        recipesRecyclerView.adapter = recipesAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(WeekViewModel::class.java)
    }

    private fun createDayButtons() {
        daysOfWeek.forEach { day ->
            daysContainer.addView(createDayButton(day))
        }
    }

    private fun createDayButton(day: String): Button {
        val button = Button(requireContext()).apply {
            text = day
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 8, 0)
            }
            setOnClickListener { fetchRecipesForDay(day) }
        }
        return button
    }

    private fun fetchRecipesForDay(day: String) {
        val dayIndex = daysOfWeek.indexOf(day)
        viewModel.getRecipesByDay(dayIndex).observe(viewLifecycleOwner, Observer { recipes ->
            recipesAdapter.updateList(recipes)
        })
    }
}
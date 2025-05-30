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
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var recipesAdapter: WeekRecipesAdapter
    private lateinit var viewModel: WeekViewModel

    private val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        daysContainer = view.findViewById(R.id.days_container)


        val recyclerView = view.findViewById<RecyclerView>(R.id.recipes_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recipesAdapter = WeekRecipesAdapter(mutableListOf(), this@WeekFragment)
        recyclerView.adapter = recipesAdapter

        viewModel = ViewModelProvider(this)[WeekViewModel::class.java]

        for (day in daysOfWeek) {
            val button = Button(requireContext())
            button.text = day
            button.setOnClickListener {
                viewModel.getRecipesByDay(daysOfWeek.indexOf(day)).observe(viewLifecycleOwner, Observer { recipes ->
                    recipesAdapter.updateList(recipes)
                })
            }
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8, 0) // Add some margin
            button.layoutParams = layoutParams
            daysContainer.addView(button)
        }
    }
}
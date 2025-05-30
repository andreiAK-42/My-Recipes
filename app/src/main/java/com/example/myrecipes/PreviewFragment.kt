package com.example.myrecipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myrecipes.viewModel.AddViewModel
import com.example.myrecipes.viewModel.PreviewViewModel
import com.example.myrecipes.viewModel.RecipesViewModel
import com.google.android.material.chip.Chip


class PreviewFragment : Fragment() {
    private lateinit var viewModel: PreviewViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        viewModel = ViewModelProvider(this).get(PreviewViewModel::class.java)

        val args: PreviewFragmentArgs by navArgs()
        viewModel.getRecord(args.id)

        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            view.findViewById<EditText>(R.id.et_name).setText(recipe.name)
            view.findViewById<EditText>(R.id.et_ingredients).setText(recipe.ingredients)
            view.findViewById<EditText>(R.id.et_instructions).setText(recipe.instructions)
            view.findViewById<EditText>(R.id.et_time).setText(recipe.time.toString())
            view.findViewById<EditText>(R.id.et_tag).setText(recipe.tag)
            view.findViewById<EditText>(R.id.et_servings).setText(recipe.servings.toString())
        }

        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            val action = PreviewFragmentDirections.actionFragmentPreviewToFragmentRecipes()
            findNavController().navigate(action)
        }

        view.findViewById<Chip>(R.id.cp_deleteRecipe).setOnClickListener {
            viewModel.deleteRecord(viewModel.recipe.value!!)
            val action = PreviewFragmentDirections.actionFragmentPreviewToFragmentRecipes()
            findNavController().navigate(action)
        }

        return view
    }


}
package com.example.myrecipes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myrecipes.viewModel.AddViewModel
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import database.RecipeEntity

class AddFragment : Fragment() {

    private var photoPath: String? = null
    private lateinit var viewModel: AddViewModel
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)

        setupUI(view)

        return view
    }

    private fun setupUI(view: View) {
        setupBackButton(view)
        setupImageUploadButton(view)
        setupDifficultySpinner(view)
        setupAddRecipeButton(view)
    }

    private fun setupBackButton(view: View) {
        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            navigateToRecipes()
        }
    }

    private fun navigateToRecipes() {
        val action = AddFragmentDirections.actionFragmentAddToFragmentRecipes2()
        findNavController().navigate(action)
    }

    private fun setupImageUploadButton(view: View) {
        view.findViewById<Chip>(R.id.cp_addPhoto).setOnClickListener {
            selectImageInAlbum()
        }
    }

    private fun setupDifficultySpinner(view: View) {
        val difficultyList = listOf("Hard", "Medium", "Easy")
        val spinner = view.findViewById<Spinner>(R.id.spin_dificulty)

        val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, difficultyList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.adapter = adapter
    }

    private fun setupAddRecipeButton(view: View) {
        view.findViewById<Chip>(R.id.cp_addRecipe).setOnClickListener {
            val recipe = createRecipeFromInput(view)
            viewModel.insertRecord(recipe)
            navigateToRecipes()
        }
    }

    private fun createRecipeFromInput(view: View): RecipeEntity {
        return RecipeEntity(
            name = view.findViewById<EditText>(R.id.et_name).text.toString(),
            path = photoPath,
            ingredients = view.findViewById<EditText>(R.id.et_ingredients).text.toString(),
            instructions = view.findViewById<EditText>(R.id.et_instructions).text.toString(),
            time = view.findViewById<EditText>(R.id.et_time).text.toString().toInt(),
            tag = view.findViewById<EditText>(R.id.et_tag).text.toString(),
            servings = view.findViewById<EditText>(R.id.et_servings).text.toString().toInt(),
            favorite = false,
            difficulty = (view.findViewById<Spinner>(R.id.spin_dificulty).selectedItem).toString(),
            daysOfWeek = Gson().toJson(arrayOf(String))
        )
    }

    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                handleImageSelection(uri)
            }
        }
    }

    private fun handleImageSelection(uri: Uri) {
        photoPath = uri.toString()
        requireView().findViewById<Chip>(R.id.cp_addPhoto).text = "Photo Uploaded"
    }
}
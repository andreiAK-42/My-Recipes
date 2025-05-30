package com.example.myrecipes

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import database.RecipeEntity

class AddFragment : Fragment() {

    var photoPath: String? = null
    var photoWillBeUpload: Boolean = false
    private lateinit var viewModel: AddViewModel
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)

        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            val action = AddFragmentDirections.actionFragmentAddToFragmentRecipes2()
            findNavController().navigate(action)
        }

        view.findViewById<Chip>(R.id.cp_addPhoto).setOnClickListener {
            selectImageInAlbum()
        }

        val difList = listOf("Hard", "Medium", "Easy")

        val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, difList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = view.findViewById<Spinner>(R.id.spin_dificulty)
        spinner.adapter = adapter

        view.findViewById<Chip>(R.id.cp_addRecipe).setOnClickListener {
            viewModel.insertRecord(RecipeEntity(
                name = view.findViewById<EditText>(R.id.et_name).text.toString(),
                path = photoPath,
                ingredients =  view.findViewById<EditText>(R.id.et_ingredients).text.toString(),
                instructions =  view.findViewById<EditText>(R.id.et_instructions).text.toString(),
                time = view.findViewById<EditText>(R.id.et_time).text.toString().toInt(),
                tag = view.findViewById<EditText>(R.id.et_tag).text.toString(),
                servings = view.findViewById<EditText>(R.id.et_servings).text.toString().toInt(),
                favorite = false,
                difficulty = spinner.getSelectedItem().toString()
            ))

            val action = AddFragmentDirections.actionFragmentAddToFragmentRecipes()
            findNavController().navigate(action)
        }

        return view
    }

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                photoPath = uri.toString()

                requireView().findViewById<Chip>(R.id.cp_addPhoto).text = "Photo Uploaded"
                photoWillBeUpload = true

            }
        }
    }
}
package com.example.myrecipes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myrecipes.viewModel.PreviewViewModel
import com.google.android.material.chip.Chip


class PreviewFragment : Fragment() {
    private lateinit var viewModel: PreviewViewModel
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 2
    var photoPath: String? = null

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

        view.findViewById<Chip>(R.id.cp_addRecipe).setOnClickListener {
            if (photoPath != null) {
                viewModel.recipe.value!!.path = photoPath
            }
            viewModel.updateRecord()
            val action = PreviewFragmentDirections.actionFragmentPreviewToFragmentRecipes()
            findNavController().navigate(action)
        }

        view.findViewById<Chip>(R.id.cp_addPhoto).setOnClickListener {
            selectImageInAlbum()
        }

        view.findViewById<Chip>(R.id.cp_deleteRecipe).setOnClickListener {
            viewModel.deleteRecord(viewModel.recipe.value!!)
            val action = PreviewFragmentDirections.actionFragmentPreviewToFragmentRecipes()
            findNavController().navigate(action)
        }

        view.findViewById<ImageView>(R.id.iv_favorite).setOnClickListener {
            if (viewModel.recipe.value!!.favorite) {
                view.findViewById<ImageView>(R.id.iv_favorite).background = resources.getDrawable(R.drawable.heart_off)
                viewModel.recipe.value!!.favorite = false
                viewModel.updateRecord()
            } else {
                view.findViewById<ImageView>(R.id.iv_favorite).background = resources.getDrawable(R.drawable.heart_on)
                viewModel.recipe.value!!.favorite = true
                viewModel.updateRecord()
            }
        }

        return view
    }

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                photoPath = uri.toString()

                requireView().findViewById<Chip>(R.id.cp_addPhoto).text = "Photo Uploaded"
            }
        }
    }
}
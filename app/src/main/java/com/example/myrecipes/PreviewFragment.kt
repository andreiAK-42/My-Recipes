package com.example.myrecipes

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myrecipes.viewModel.PreviewViewModel
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import java.util.Collections


class PreviewFragment : Fragment() {
    private lateinit var viewModel: PreviewViewModel
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 2
    private var photoPath: String? = null
    private var selectedLanguage: BooleanArray = BooleanArray(7)
    private val langList = ArrayList<Int>()
    private val langArray = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        viewModel = ViewModelProvider(this).get(PreviewViewModel::class.java)

        val args: PreviewFragmentArgs by navArgs()
        viewModel.getRecord(args.id)
        setupUI(view)

        return view
    }

    private fun setupUI(view: View) {
        setupObservers(view)
        setupNavigation(view)
        setupDaysSelection(view)
        setupChipListeners(view)
    }

    private fun setupObservers(view: View) {
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            view.findViewById<EditText>(R.id.et_name).setText(recipe.name)
            view.findViewById<EditText>(R.id.et_ingredients).setText(recipe.ingredients)
            view.findViewById<EditText>(R.id.et_instructions).setText(recipe.instructions)
            view.findViewById<EditText>(R.id.et_time).setText(recipe.time.toString())
            view.findViewById<EditText>(R.id.et_tag).setText(recipe.tag)
            view.findViewById<EditText>(R.id.et_servings).setText(recipe.servings.toString())
        }
    }

    private fun setupNavigation(view: View) {
        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            navigateTo(PreviewFragmentDirections.actionFragmentPreviewToFragmentRecipes())
        }
    }

    private fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun setupDaysSelection(view: View) {
        val textView = view.findViewById<TextView>(R.id.tv_weekDays)
        textView.setOnClickListener {
            showDaysSelectionDialog(textView)
        }
    }

    private fun showDaysSelectionDialog(textView: TextView) {
        AlertDialog.Builder(requireContext())
            .setTitle("Select Days")
            .setCancelable(false)
            .setMultiChoiceItems(langArray, selectedLanguage) { dialog, index, isChecked ->
                if (isChecked) {
                    langList.add(index)
                } else {
                    langList.remove(index)
                }
            }
            .setPositiveButton("OK") { dialog, _ ->
                updateSelectedDays(textView)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("Clear All") { dialog, _ ->
                selectedLanguage.fill(false)
                langList.clear()
                textView.text = ""
                dialog.dismiss()
            }
            .show()
    }

    private fun updateSelectedDays(textView: TextView) {
        textView.text = langList.joinToString(", ") { langArray[it] }
    }

    private fun setupChipListeners(view: View) {
        view.findViewById<Chip>(R.id.cp_addRecipe).setOnClickListener { addRecipe() }
        view.findViewById<Chip>(R.id.cp_addPhoto).setOnClickListener { selectImageInAlbum() }
        view.findViewById<Chip>(R.id.cp_deleteRecipe).setOnClickListener { deleteRecipe() }
        view.findViewById<ImageView>(R.id.iv_favorite).setOnClickListener { toggleFavoriteStatus(it) }
    }

    private fun addRecipe() {
        viewModel.recipe.value?.apply {
            if (photoPath != null) {
                path = photoPath
            }
            daysOfWeek = Gson().toJson(langList)
            viewModel.updateRecord()
        }
        navigateTo(PreviewFragmentDirections.actionFragmentPreviewToFragmentRecipes())
    }

    private fun deleteRecipe() {
        viewModel.recipe.value?.let {
            viewModel.deleteRecord(it)
            navigateTo(PreviewFragmentDirections.actionFragmentPreviewToFragmentRecipes())
        }
    }

    private fun toggleFavoriteStatus(view: View) {
        val ivFavorite = view as ImageView
        val isFavorite = viewModel.recipe.value!!.favorite

        viewModel.recipe.value!!.favorite = !isFavorite
        ivFavorite.setBackgroundResource(if (isFavorite) R.drawable.heart_off else R.drawable.heart_on)
        viewModel.updateRecord()
    }

    private fun selectImageInAlbum() {
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
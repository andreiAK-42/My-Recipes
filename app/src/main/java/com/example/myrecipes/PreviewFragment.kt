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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myrecipes.viewModel.PreviewViewModel
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import java.util.Collections


class PreviewFragment : Fragment() {
    private lateinit var viewModel: PreviewViewModel
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 2
    var photoPath: String? = null

    var selectedLanguage: BooleanArray = booleanArrayOf(false, false, false, false, false, false, false)
    var langList: ArrayList<Int> = ArrayList()
    var langArray: Array<String> = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        viewModel = ViewModelProvider(this).get(PreviewViewModel::class.java)

        val args: PreviewFragmentArgs by navArgs()
        viewModel.getRecord(args.id)

        initUI(view)

        return view
    }

    fun initUI(view: View) {
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

        val textView = view.findViewById<TextView>(R.id.tv_weekDays)

        textView.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Select Days")
            builder.setCancelable(false)

            builder.setMultiChoiceItems(
                langArray,
                selectedLanguage
            ) { dialogInterface, i, b ->
                if (b) {
                    langList.add(i)
                    Collections.sort(langList)
                } else {
                    langList.remove(i)
                }
            }

            builder.setPositiveButton("OK") { dialogInterface, i ->
                val stringBuilder = StringBuilder()
                for (j in 0 until langList.size) {
                    stringBuilder.append(langArray[langList[j]])
                    if (j != langList.size - 1) {
                        stringBuilder.append(", ")
                    }
                }
                textView.text = stringBuilder.toString()
            }

            builder.setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }

            builder.setNeutralButton("Clear All") { dialogInterface, i ->
                for (j in selectedLanguage.indices) {
                    selectedLanguage[j] = false
                    langList.clear()
                    textView.text = ""
                }
            }
            builder.show()
        }

        view.findViewById<Chip>(R.id.cp_addRecipe).setOnClickListener {
            if (photoPath != null) {
                viewModel.recipe.value!!.path = photoPath
            }

            viewModel.recipe.value!!.daysOfWeek = Gson().toJson(langList)
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
            val iv_favorite = view.findViewById<ImageView>(R.id.iv_favorite)
            if (viewModel.recipe.value!!.favorite) {
                iv_favorite.background = resources.getDrawable(R.drawable.heart_off)
                viewModel.recipe.value!!.favorite = false
                viewModel.updateRecord()
            } else {
                iv_favorite.background = resources.getDrawable(R.drawable.heart_on)
                viewModel.recipe.value!!.favorite = true
                viewModel.updateRecord()
            }
        }
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
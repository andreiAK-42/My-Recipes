package com.example.myrecipes.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrecipes.R
import com.example.myrecipes.RecipesFragment
import database.RecipeEntity

class RecipesAdapter(
    private var recipeList: MutableList<RecipeEntity>, private val listener: RecipesFragment
) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_photo)
        val name: TextView = itemView.findViewById(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rview_recipes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipeList[position]

        holder.name.text = recipe.name

        var bitmap: Bitmap? = null

        try {
            val inputStream =
                holder.itemView.context.contentResolver.openInputStream(recipe.path!!.toUri())
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
        } finally {
            Glide.with(holder.itemView.context).load(bitmap)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image)
        }

        holder.image.setOnClickListener {
            listener.onViewRecipe(recipe)
        }
    }

    fun updateList(newList: List<RecipeEntity>) {
        recipeList = newList.toMutableList()
        notifyDataSetChanged()
    }

    interface OnRecipeAdapterListener {
        fun onViewRecipe(recipe: RecipeEntity)
    }


    override fun getItemCount(): Int {
        return recipeList.size
    }
}
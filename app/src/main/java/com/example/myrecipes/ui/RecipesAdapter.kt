package com.example.myrecipes.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrecipes.R
import com.example.myrecipes.RecipesFragment
import database.RecipeEntity

class RecipesAdapter(
    private var recipeList: MutableList<RecipeEntity>, private val listener: RecipesFragment
) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    var currentTagFilter: String? = null
    var currentDif: String? = null
    var currentTime: String? = null

    private var originalList: MutableList<RecipeEntity> = recipeList

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

        if (recipe.path != null) {
            try {
                val inputStream =
                    holder.itemView.context.contentResolver.openInputStream(recipe.path!!.toUri())
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {

            } finally {
                Glide.with(holder.itemView.context).load(bitmap!!.getRoundedCornerBitmap(20))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.image)
            }
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.phototo)
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

    fun initFilterList() {
        originalList = recipeList.toList().toMutableList()
    }

    interface OnRecipeAdapterListener {
        fun onViewRecipe(recipe: RecipeEntity)
    }


    override fun getItemCount(): Int {
        return recipeList.size
    }

    fun Bitmap.getRoundedCornerBitmap(pixels: Int): Bitmap {
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())

        val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader

        paint.isAntiAlias = true

        canvas.drawRoundRect(rectF, pixels.toFloat(), pixels.toFloat(), paint)

        return output
    }

    fun setTagFilter(tag: String?) {
        currentTagFilter = tag
        applyFilters()
    }

    fun setDif(newDif: String?) {
        currentDif = newDif
        applyFilters()
    }

    private fun applyFilters() {
        var filteredList = originalList.toList()

        if (currentTagFilter != null && currentTagFilter != "All") {
            if (currentTagFilter == "Favorite") {
                if (currentTagFilter!!.isNotEmpty()) {
                    filteredList = filteredList.filter { recipe ->
                        recipe.favorite
                    }
                }
            } else if (currentTagFilter!!.isNotEmpty()) {
                filteredList = filteredList.filter { recipe ->
                    recipe.tag.contains(currentTagFilter!!)
                }
            }
        }

        if (currentDif != null && currentDif != "All") {
            filteredList = filteredList.filter { recipe ->
                recipe.difficulty == currentDif!!
            }
        }

        if (currentTime != null) {
            filteredList = filteredList.filter { recipe ->
                recipe.time == currentTime!!.toInt()
            }
        }

        recipeList.clear()
        recipeList.addAll(filteredList)
        notifyDataSetChanged()
    }

    fun resetFilters() {
        currentTagFilter = null
        currentDif = null
        currentTime = null
        applyFilters()
    }
}
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
import com.example.myrecipes.WeekFragment
import database.RecipeEntity

class WeekRecipesAdapter(
    private var recipeList: MutableList<RecipeEntity>,
    private val listener: WeekFragment
) : RecyclerView.Adapter<WeekRecipesAdapter.ViewHolder>() {
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
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.image)
            }
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.phototo)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image)
        }
    }

    fun updateList(newList: List<RecipeEntity>) {
        recipeList = newList.toMutableList()
        notifyDataSetChanged()
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
}


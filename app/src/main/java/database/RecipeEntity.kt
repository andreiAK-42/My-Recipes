package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes_entity")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "path") var path: String?,
    @ColumnInfo(name = "favorite") var favorite: Boolean,
    @ColumnInfo(name = "ingredients") var ingredients: String,
    @ColumnInfo(name = "instructions") var instructions: String,
    @ColumnInfo(name = "time") var time: Int,
    @ColumnInfo(name = "tag") var tag: String,
    @ColumnInfo(name = "servings") var servings: Int,
    @ColumnInfo(name = "difficulty") var difficulty: String,
)
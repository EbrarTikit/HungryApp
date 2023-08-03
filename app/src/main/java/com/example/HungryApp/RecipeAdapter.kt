package com.example.HungryApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.HungryApp.databinding.ItemRecipeBinding



class RecipeAdapter(var recipes: List<Recipe> = emptyList()) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newRecipes: List<Recipe>?) {
        recipes = newRecipes ?: emptyList()
        notifyDataSetChanged()
    }


    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val recipe = recipes[position]
                    itemClickListener?.onItemClick(recipe.id)
                }
            }

            binding.fav.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val recipe = recipes[position]
                    itemClickListener?.onFavoriteButtonClick(recipe)
                }
            }
        }

        fun bind(recipe: Recipe) {
            binding.textTitle.text = recipe.title
            Glide.with(binding.root)
                .load(recipe.image)
                .into(binding.imageRecipe)

            // Updates the favorite button state based on the isFavorite flag
            binding.fav.isChecked = recipe.isFavorite
        }
    }

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
        fun onFavoriteButtonClick(recipe: Recipe)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}
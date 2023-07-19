package com.example.HungryApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.HungryApp.databinding.ItemRecipeBinding



class RecipeAdapter(private var recipes: List<Recipe> = emptyList()) :
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
        }

        fun bind(recipe: Recipe) {
            binding.textTitle.text = recipe.title
            Glide.with(binding.root)
                .load(recipe.image)
                .into(binding.imageRecipe)
        }
    }

    // Define the click listener interface
    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    // Create a variable to hold the click listener
    private var itemClickListener: OnItemClickListener? = null

    // Setter method to set the click listener from the activity
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}
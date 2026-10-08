package com.example.katalogresep.data.repository

import com.example.katalogresep.data.model.Meal
import com.example.katalogresep.network.MealApiClient
import com.example.katalogresep.network.MealApiService

class RecipeRepository(
    private val apiService: MealApiService = MealApiClient.instance
) {
    suspend fun searchRecipes(query: String): List<Meal> {
        val response = apiService.searchRecipes(query)
        return response.meals ?: emptyList()
    }

    suspend fun getRecipeDetail(id: String): Meal? {
        val response = apiService.getRecipeDetail(id)
        return response.meals?.firstOrNull()
    }
}

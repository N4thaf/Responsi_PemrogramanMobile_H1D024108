package com.example.katalogresep.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.katalogresep.data.model.Meal
import com.example.katalogresep.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RecipeDetailUiState {
    object Loading : RecipeDetailUiState
    data class Success(val recipe: Meal) : RecipeDetailUiState
    data class Error(val message: String) : RecipeDetailUiState
}

class RecipeDetailViewModel(
    private val repository: RecipeRepository = RecipeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeDetailUiState>(RecipeDetailUiState.Loading)
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun loadRecipeDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = RecipeDetailUiState.Loading
            try {
                val recipe = repository.getRecipeDetail(id)
                if (recipe != null) {
                    _uiState.value = RecipeDetailUiState.Success(recipe)
                } else {
                    _uiState.value = RecipeDetailUiState.Error("Resep tidak ditemukan")
                }
            } catch (e: Exception) {
                _uiState.value = RecipeDetailUiState.Error(e.localizedMessage ?: "Terjadi kesalahan")
            }
        }
    }
}

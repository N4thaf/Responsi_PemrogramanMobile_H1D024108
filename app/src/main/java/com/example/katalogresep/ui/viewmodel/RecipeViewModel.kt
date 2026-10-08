package com.example.katalogresep.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.katalogresep.data.model.Meal
import com.example.katalogresep.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RecipeListUiState {
    object Loading : RecipeListUiState
    data class Success(val recipes: List<Meal>) : RecipeListUiState
    data class Error(val message: String) : RecipeListUiState
}

class RecipeViewModel(
    private val repository: RecipeRepository = RecipeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeListUiState>(RecipeListUiState.Loading)
    val uiState: StateFlow<RecipeListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        searchRecipes("")
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        searchRecipes(newQuery)
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _uiState.value = RecipeListUiState.Loading
            try {
                val results = repository.searchRecipes(query)
                _uiState.value = RecipeListUiState.Success(results)
            } catch (e: Exception) {
                _uiState.value = RecipeListUiState.Error(e.localizedMessage ?: "Terjadi kesalahan")
            }
        }
    }
}

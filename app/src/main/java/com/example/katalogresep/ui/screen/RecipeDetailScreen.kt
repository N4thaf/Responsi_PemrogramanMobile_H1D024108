package com.example.katalogresep.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.katalogresep.R
import com.example.katalogresep.data.model.Meal
import com.example.katalogresep.data.model.getIngredientsList
import com.example.katalogresep.ui.viewmodel.RecipeDetailUiState
import com.example.katalogresep.ui.viewmodel.RecipeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    viewModel: RecipeDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = recipeId) {
        viewModel.loadRecipeDetail(recipeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Resep") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is RecipeDetailUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is RecipeDetailUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                is RecipeDetailUiState.Success -> {
                    val recipe = state.recipe
                    val ingredientsList = recipe.getIngredientsList()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = recipe.strMealThumb,
                            contentDescription = recipe.strMeal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = recipe.strMeal,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (!recipe.strCategory.isNullOrBlank()) {
                                    AssistChip(
                                        onClick = { },
                                        label = { Text(recipe.strCategory) }
                                    )
                                }
                                if (!recipe.strArea.isNullOrBlank()) {
                                    AssistChip(
                                        onClick = { },
                                        label = { Text("Asal: ${recipe.strArea}") }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Bahan-bahan & Takaran:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    ingredientsList.forEach { item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "• ${item.ingredient}",
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = item.measure,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Instruksi Memasak:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = recipe.strInstructions ?: "Tidak ada instruksi memasak.",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

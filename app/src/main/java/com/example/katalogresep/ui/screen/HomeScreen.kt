package com.example.katalogresep.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.katalogresep.data.model.Meal
import com.example.katalogresep.ui.viewmodel.RecipeListUiState
import com.example.katalogresep.ui.viewmodel.RecipeViewModel

@Composable
fun RecipeCard(
    meal: Meal,
    onRecipeClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onRecipeClick(meal.idMeal) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                if (!meal.strCategory.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = meal.strCategory,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = meal.strMeal,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!meal.strArea.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Asal: ${meal.strArea}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Katalog Resep Makanan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Cari Resep Makanan...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            when (val state = uiState) {
                is RecipeListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is RecipeListUiState.Error -> {
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
                is RecipeListUiState.Success -> {
                    if (state.recipes.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Resep tidak ditemukan.")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(all = 8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.recipes) { meal ->
                                RecipeCard(
                                    meal = meal,
                                    onRecipeClick = onRecipeClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

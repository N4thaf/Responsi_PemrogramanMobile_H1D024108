package com.example.katalogresep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.katalogresep.ui.screen.HomeScreen
import com.example.katalogresep.ui.screen.RecipeDetailScreen
import com.example.katalogresep.ui.viewmodel.RecipeDetailViewModel
import com.example.katalogresep.ui.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val recipeViewModel: RecipeViewModel = viewModel()
                    val recipeDetailViewModel: RecipeDetailViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable(route = "home") {
                            HomeScreen(
                                viewModel = recipeViewModel,
                                onRecipeClick = { recipeId ->
                                    navController.navigate("recipe_detail/$recipeId")
                                }
                            )
                        }
                        composable(
                            route = "recipe_detail/{recipeId}",
                            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                            RecipeDetailScreen(
                                recipeId = recipeId,
                                viewModel = recipeDetailViewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

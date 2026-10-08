package com.example.katalogresep.network

import com.example.katalogresep.data.model.MealResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("api/json/v1/1/search.php")
    suspend fun searchRecipes(@Query("s") query: String): MealResponse

    @GET("api/json/v1/1/lookup.php")
    suspend fun getRecipeDetail(@Query("i") id: String): MealResponse
}

object MealApiClient {
    private const val BASE_URL = "https://www.themealdb.com/"

    val instance: MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }
}

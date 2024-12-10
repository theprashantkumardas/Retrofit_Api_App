package com.example.retrofitapi.data

import com.example.retrofitapi.data.model.Product
import kotlinx.coroutines.flow.Flow


interface ProductsRepository {
    suspend fun getProductsList(): Flow<Result<List<Product>>>
}
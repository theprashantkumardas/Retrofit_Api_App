package com.example.retrofitapi.data

import com.example.retrofitapi.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import okio.IOException


class ProductsRepositoryImplementation(
    private val api: Api
) : ProductsRepository {
    override suspend fun getProductsList(): Flow<Result<List<Product>>> {
        return flow {
            // Emit loading state


           val productsFromApi = try {
                // Fetch data from API
                 api.getProductsList()

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error("Network Error: Unable to load products")) // Emit error state for network issues
               return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error("Server Error: Unable to load products")) // Emit error state for HTTP exceptions
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error("Unexpected Error")) // Emit error state for unexpected exceptions
               return@flow
            }

            emit(Result.Success(productsFromApi.products))
        }
    }
}

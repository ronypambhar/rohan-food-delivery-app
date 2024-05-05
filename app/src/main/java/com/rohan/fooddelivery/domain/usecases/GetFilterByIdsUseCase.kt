package com.rohan.fooddelivery.domain.usecases

import com.rohan.fooddelivery.data.di.IoDispatcher
import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFilterByIdsUseCase @Inject constructor(
    private val iRestaurantRepository: IRestaurantRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun invoke(filterIds: List<String>): List<FilterInfo> {
        return withContext(ioDispatcher) {
            filterIds.map { filterId ->
                async { iRestaurantRepository.getFilterByID(filterId) }
            }.awaitAll()
        }
    }
}
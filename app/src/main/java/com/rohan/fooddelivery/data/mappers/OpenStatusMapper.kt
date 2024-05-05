package com.rohan.fooddelivery.data.mappers

import com.rohan.fooddelivery.data.models.OpenStatusResponse
import com.rohan.fooddelivery.domain.models.OpenStatusInfo

class OpenStatusMapper : Mapper<OpenStatusResponse, OpenStatusInfo> {
    override fun map(input: OpenStatusResponse): OpenStatusInfo {
        return OpenStatusInfo(
            restaurantId = input.restaurant_id.orEmpty(),
            isOpen = input.is_currently_open ?: false
        )
    }
}
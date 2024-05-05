package com.rohan.fooddelivery.data.mappers

import com.rohan.fooddelivery.data.models.FilterResponse
import com.rohan.fooddelivery.domain.models.FilterInfo

class FilterInfoMapper : Mapper<FilterResponse, FilterInfo> {
    override fun map(input: FilterResponse): FilterInfo {
        return FilterInfo(
            input.id.orEmpty(),
            input.name.orEmpty(),
            input.image_url.orEmpty()
        )
    }
}
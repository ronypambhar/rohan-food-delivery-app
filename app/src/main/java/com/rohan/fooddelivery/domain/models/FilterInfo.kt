package com.rohan.fooddelivery.domain.models

data class FilterInfo(
    val id: String = "",
    val filterName: String = "",
    val imageUrl: String = ""
) {
    var isSelected: Boolean = false
}
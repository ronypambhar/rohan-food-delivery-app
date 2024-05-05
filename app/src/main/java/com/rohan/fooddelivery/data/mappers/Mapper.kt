package com.rohan.fooddelivery.data.mappers

interface Mapper<DATA, DOMAIN> {
    fun map(input: DATA) : DOMAIN
}
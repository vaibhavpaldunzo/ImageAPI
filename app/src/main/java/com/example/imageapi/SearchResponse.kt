package com.example.imageapi

data class SearchResponse(
    val `data`: List<Data>,
    val errormessage: String,
    val rowcount: Int,
    val success: Boolean,
    val word: String
)


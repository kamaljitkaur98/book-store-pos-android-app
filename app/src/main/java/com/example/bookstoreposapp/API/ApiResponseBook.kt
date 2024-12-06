package com.example.bookstoreposapp.API

data class ApiResponseBook(
    val id: String,
    val isbn: String,
    val currentPrice: Double,
    val transactionCount: Int,
    val title: String,
    val authors: String,
    val edition: String,
    val basePrice: Double,
    val imageURL: String,
    val availability: Boolean
)

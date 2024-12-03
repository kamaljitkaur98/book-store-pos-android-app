package com.example.bookstoreposapp.API.add

data class ApiBookDetailByIsbn(
    val isbn: String,
    val title: String,
    val authors: String,
    val edition: String,
    val basePrice: Double,
    val imageURL: String
)

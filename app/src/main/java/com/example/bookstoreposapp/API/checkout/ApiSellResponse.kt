package com.example.bookstoreposapp.API.checkout

data class ApiSellResponse(
    val id: String,
    val status: String,
    val message: String,
    val amount: Double,
    val transactionId: String
)

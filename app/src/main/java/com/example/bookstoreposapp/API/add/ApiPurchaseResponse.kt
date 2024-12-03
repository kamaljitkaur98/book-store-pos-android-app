package com.example.bookstoreposapp.API.add

data class PurchaseResponse(
    val id: String,
    val status: String,
    val message: String,
    val amount: Double,
    val transactionId: String
)

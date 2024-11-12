package com.example.bookstoreposapp

import java.io.Serializable

data class BookData(val title: String, val image: Int, val status: String, val originalPrice: String, val discountedPrice: String, val isbn: String, val id: String, val transactionCount: Int) :
    Serializable

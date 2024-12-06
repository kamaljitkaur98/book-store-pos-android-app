package com.example.bookstoreposapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: String,
    val itemName: String,
    val originalPrice: String,
    val discountedPrice: String,
    val status: String,
    val availability: Boolean
)

